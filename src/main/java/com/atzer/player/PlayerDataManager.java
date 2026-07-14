package com.atzer.player;

import com.atzer.RPGInventory;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;

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
