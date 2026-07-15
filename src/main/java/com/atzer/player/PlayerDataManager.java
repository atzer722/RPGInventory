package com.atzer.player;

import com.atzer.RPGInventory;
import com.atzer.armor.ArmorPiece;
import com.atzer.armor.ArmorType;
import com.atzer.armor.ArmorZone;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.EnumMap;
import java.util.Map;

@RequiredArgsConstructor
public class PlayerDataManager {

    private final PlayerDataRepository playerDataRepository;

    public void playerJoinEventHandler(Player player) {
        if (this.playerDataRepository.findById(player.getUniqueId()).isEmpty()) {
            this.playerFirstJoinEventHandler(player);
        }
        this.applyBestArmor(player); // gère les deux cas
    }

    // Quand le joueur change de zone dans le menu
    public void onZoneSelected(Player player, int zoneId) {
        PlayerData updated = new PlayerData(player.getUniqueId(), zoneId);
        this.playerDataRepository.update(updated);
        this.applyBestArmor(player);
    }

    // Quand LuckPerms modifie les permissions (NodeMutateEvent)
    public void playerPermissionChangeEventHandler(Player player) {
        this.applyBestArmor(player); // pas besoin de toucher PlayerData, la zone ne change pas
    }

    private void applyBestArmor(Player player) {
        PlayerData data = playerDataRepository.findById(player.getUniqueId())
                .orElse(new PlayerData(player.getUniqueId(), 1)); // zone 1 par défaut

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

            ArmorPiece best = zone.getPiecesOfType(type).stream()
                    .filter(p -> p.tier() == targetTier)
                    .findFirst()
                    .orElse(null);

            if (best != null) equipPiece(player, best);
        }
    }

    private PlayerData playerFirstJoinEventHandler(Player player) {
        return this.playerDataRepository.save(new PlayerData(
                player.getUniqueId(),
                1 // zone 1 par défaut
        ));
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
