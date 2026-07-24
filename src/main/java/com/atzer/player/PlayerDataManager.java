package com.atzer.player;

import com.atzer.PluginRepository;
import com.atzer.RPGInventory;
import com.atzer.armor.ArmorPiece;
import com.atzer.armor.ArmorType;
import com.atzer.armor.ArmorZone;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.LocalPlayer;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import com.sk89q.worldguard.protection.regions.RegionQuery;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.*;

@RequiredArgsConstructor
public final class PlayerDataManager {

    private final Map<UUID, PlayerData> cache = new HashMap<>();

    private static PluginRepository<PlayerData, UUID> getPlayerDataRepository() {
        return RPGInventory.getInstance().getPlayerDataRepository();
    }

    public @Nullable PlayerData getPlayerData(Player player) {
        PlayerData data = cache.get(player.getUniqueId());
        if (data == null) {
            RPGInventory.getInstance().getErrorHandler().handleNoPlayerDataError(player);
        }
        return data;
    }

    public void playerJoinEventHandler(Player player) {
        getPlayerDataRepository().findById(player.getUniqueId())
                .thenAccept(optional -> {
                    PlayerData data = optional.orElseGet(() -> {
                        PlayerData newData = new PlayerData(player.getUniqueId(), 1);
                        getPlayerDataRepository().save(newData);
                        return newData;
                    });

                    Bukkit.getScheduler().runTask(RPGInventory.getInstance(), () -> {
                        cache.put(player.getUniqueId(), data);
                        this.updateArmorForLocation(player, player.getLocation());
                    });
                });
    }

    // When the player equip a zone.
    public void onZoneSelected(Player player, int zoneId) {
        PlayerData updated = new PlayerData(player.getUniqueId(), zoneId);
        cache.put(player.getUniqueId(), updated);
        getPlayerDataRepository().update(updated);
        this.updateArmorForLocation(player, player.getLocation());
    }

    public void playerPermissionChangeEventHandler(Player player) {
        this.updateArmorForLocation(player, player.getLocation());
    }

    public void playerQuitEventHandler(Player player) {
        cache.remove(player.getUniqueId());
    }

    public void updateArmorForLocation(Player player, Location location) {
        if (this.canEquipArmorFromLocation(player, location)) {
            this.unequipArmor(player);
        } else {
            this.applyBestArmor(player);
        }
    }

    public boolean canEquipArmorFromLocation(Player player, Location location) {
        RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
        RegionQuery query = container.createQuery();
        LocalPlayer localPlayer = WorldGuardPlugin.inst().wrapPlayer(player);

        return query.getApplicableRegions(BukkitAdapter.adapt(location))
                .testState(localPlayer, RPGInventory.getInstance().getArmorDisabledFlag());
    }

    public @Nullable ArmorPiece getHighestUnlockedTier(Player player, ArmorType type, ArmorZone zone) {
        ArmorPiece highest = null;
        for (ArmorPiece piece : zone.getPiecesOfType(type)) {
            if (player.hasPermission(piece.permission())) highest = piece;
            else return highest;
        }
        return highest;
    }

    public void equipCurrentZone(Player player) {
        this.updateArmorForLocation(player,  player.getLocation());
    }

    private void applyBestArmor(Player player) {
        PlayerData data = this.getPlayerData(player);

        if  (data == null) {
            RPGInventory.getInstance().getErrorHandler().handleNoPlayerDataError(player);
            return;
        }

        ArmorZone zone = RPGInventory.getInstance()
                .getArmorZoneRegistry()
                .getZoneById(data.armorZoneId());

        if (zone == null) return;

        // Étape 1 : palier max débloqué par slot
        Map<ArmorType, Integer> highestUnlocked = new EnumMap<>(ArmorType.class);
        for (ArmorType type : ArmorType.values()) {
            if (type == ArmorType.HEADER) continue;

            int highest = 0;
            for (ArmorPiece piece : zone.getPiecesOfType(type)) {
                if (player.hasPermission(piece.permission())) {
                    highest = piece.tier(); // ArmorPiece doit exposer son palier
                }
            }
            highestUnlocked.put(type, highest);
        }

        // Étape 2 : contrainte d'écart → palier équipable réel
        Map<ArmorType, Integer> equippable = new EnumMap<>(ArmorType.class);
        for (ArmorType type : ArmorType.values()) {
            if (type == ArmorType.HEADER) continue;

            int minOthers = Arrays.stream(ArmorType.values())
                    .filter(t -> t != type)
                    .filter(t -> t != ArmorType.HEADER)
                    .mapToInt(highestUnlocked::get)
                    .min()
                    .orElse(0);

            equippable.put(type, Math.min(highestUnlocked.get(type), minOthers + 1));
        }

        // Étape 3 : équiper les pièces concrètes
        for (ArmorType type : ArmorType.values()) {
            if (type == ArmorType.HEADER) continue;

            int targetTier = equippable.get(type);
            if (targetTier == 0) {
                unequipSlot(player, type);
                continue;
            }

            zone.getPiecesOfType(type).stream()
                    .filter(p -> p.tier() == targetTier)
                    .findFirst().ifPresent(best -> equipPiece(player, best));

        }
    }

    private void unequipArmor(Player player) {
        player.getInventory().setHelmet(null);
        player.getInventory().setChestplate(null);
        player.getInventory().setLeggings(null);
        player.getInventory().setBoots(null);
    }

    private void unequipSlot(Player player, ArmorType type) {
        switch (type) {
            case HELMET     -> player.getInventory().setHelmet(null);
            case CHESTPLATE -> player.getInventory().setChestplate(null);
            case LEGGING    -> player.getInventory().setLeggings(null);
            case BOOTS      -> player.getInventory().setBoots(null);
            default -> {}
        }
    }

    private void equipPiece(Player player, ArmorPiece piece) {
        ItemStack item = piece.toItemStack();
        if (item == null) return;
        switch (piece.type()) {
            case HELMET     -> player.getInventory().setHelmet(item);
            case CHESTPLATE -> player.getInventory().setChestplate(item);
            case LEGGING    -> player.getInventory().setLeggings(item);
            case BOOTS      -> player.getInventory().setBoots(item);
            default -> {}
        }
    }
}
