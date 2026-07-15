package com.atzer.player;

import com.atzer.RPGInventory;
import com.atzer.armor.ArmorPiece;
import com.atzer.armor.ArmorType;
import com.atzer.armor.ArmorZoneRegistry;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.EnumMap;
import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
public class PlayerDataManager {

    private final PlayerDataRepository playerDataRepository;

    public void playerJoinEventHandler(Player player) {
        Optional<PlayerData> playerDataInData = this.playerDataRepository.findById(player.getUniqueId());

        if (playerDataInData.isEmpty()) {
            PlayerData playerData = this.playerFirstJoinEventHandler(player);
            this.equipPlayerWithPlayerData(player, playerData);
            return;
        }

        this.equipPlayerWithPlayerData(player, playerDataInData.get());
    }

    public void playerPermissionChangeEventHandler(Player player) {
        Map<ArmorType, ArmorPiece> pieces = resolveEquippablePieces(player);

        PlayerData updated = new PlayerData(
                player.getUniqueId(),
                pieces.get(ArmorType.HELMET)     != null ? pieces.get(ArmorType.HELMET).permission()     : null,
                pieces.get(ArmorType.CHESTPLATE) != null ? pieces.get(ArmorType.CHESTPLATE).permission() : null,
                pieces.get(ArmorType.LEGGING)    != null ? pieces.get(ArmorType.LEGGING).permission()    : null,
                pieces.get(ArmorType.BOOTS)      != null ? pieces.get(ArmorType.BOOTS).permission()      : null
        );

        playerDataRepository.update(updated);
        equipPlayerWithPlayerData(player, updated);
    }

    /**
     * Calcule le palier que le joueur peut effectivement équiper pour chaque slot,
     * en respectant la règle : pour équiper le palier N, tous les autres slots
     * doivent être au moins N-1.
     *
     * @return Map<ArmorType, ArmorPiece> → la meilleure pièce équipable par slot,
     *         null si aucune armure disponible pour ce slot
     */
    private Map<ArmorType, ArmorPiece> resolveEquippablePieces(Player player) {
        ArmorZoneRegistry registry = RPGInventory.getInstance().getArmorZoneRegistry();

        // Étape 1 : palier max débloqué par slot
        Map<ArmorType, Integer> unlockedTiers = new EnumMap<>(ArmorType.class);
        for (ArmorType type : ArmorType.values()) {
            unlockedTiers.put(type, registry.getHighestUnlockedTier(player, type));
        }

        // Étape 2 : palier équipable réel en appliquant la contrainte d'écart
        // Pour équiper N sur un slot, tous les autres doivent être >= N-1
        // Donc : palier_équipable = min(palier_débloqué, min_des_autres + 1)
        Map<ArmorType, Integer> equippableTiers = new EnumMap<>(ArmorType.class);
        for (ArmorType type : ArmorType.values()) {
            int unlocked = unlockedTiers.get(type);

            int minOtherTiers = Arrays.stream(ArmorType.values())
                    .filter(t -> t != type)
                    .mapToInt(unlockedTiers::get)
                    .min()
                    .orElse(0);

            int equippable = Math.min(unlocked, minOtherTiers + 1);
            equippableTiers.put(type, Math.max(equippable, 0)); // jamais négatif
        }

        // Étape 3 : résolution de la pièce concrète pour chaque palier équipable
        Map<ArmorType, ArmorPiece> result = new EnumMap<>(ArmorType.class);
        for (ArmorType type : ArmorType.values()) {
            int targetTier = equippableTiers.get(type);
            if (targetTier == 0) {
                result.put(type, null);
                continue;
            }
            result.put(type, registry.getArmorPieceAtTier(player, type, targetTier));
        }

        return result;
    }

    private PlayerData playerFirstJoinEventHandler(Player player) {
        return this.playerDataRepository.save(new PlayerData(
                player.getUniqueId(),
                RPGInventory.getInstance().getArmorZoneRegistry().getZone(1).armorPieces().getFirst().getFirst().permission(),
                RPGInventory.getInstance().getArmorZoneRegistry().getZone(1).armorPieces().getFirst().get(1).permission(),
                RPGInventory.getInstance().getArmorZoneRegistry().getZone(1).armorPieces().getFirst().get(2).permission(),
                RPGInventory.getInstance().getArmorZoneRegistry().getZone(1).armorPieces().getFirst().get(3).permission()
        ));
    }

    private void equipPlayerWithPlayerData(Player player, PlayerData playerData) {
        player.getInventory().setHelmet(RPGInventory.getInstance().getArmorZoneRegistry().getArmorPieceFromPermissionId(playerData.equippedHelmetPermissionId()).toItemStack());
        player.getInventory().setChestplate(RPGInventory.getInstance().getArmorZoneRegistry().getArmorPieceFromPermissionId(playerData.equippedChestplatePermissionId()).toItemStack());
        player.getInventory().setLeggings(RPGInventory.getInstance().getArmorZoneRegistry().getArmorPieceFromPermissionId(playerData.equippedLeggingPermissionId()).toItemStack());
        player.getInventory().setBoots(RPGInventory.getInstance().getArmorZoneRegistry().getArmorPieceFromPermissionId(playerData.equippedBootsPermissionId()).toItemStack());
    }
}
