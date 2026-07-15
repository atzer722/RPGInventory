package com.atzer.armor;

import com.atzer.RPGInventory;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class ArmorZoneRegistry {

    private final List<ArmorZone> zones = new ArrayList<>();

    public void loadRegistry() {
        zones.clear();
        for (String zoneId : RPGInventory.getInstance().getArmorConfig().getZoneList()) {
            int zoneIdInt = Integer.parseInt(zoneId);
            zones.add(new ArmorZone(
                    RPGInventory.getInstance().getArmorConfig().getZoneTitle(zoneIdInt),
                    zoneIdInt,
                    RPGInventory.getInstance().getArmorConfig().getArmorPieceByZone(zoneIdInt)
            ));
        }
    }

    public ArmorZone getZone(int zoneId) {
        for (ArmorZone zone : zones) {
            if (zone.num() == zoneId) return zone;
        }
        return null;
    }

    public ArmorPiece getArmorPieceFromPermissionId(String id) {
        for (ArmorZone zone : zones) {
            for (List<ArmorPiece> armors : zone.armorPieces()) {
                for (ArmorPiece armorPiece : armors) {
                    if (armorPiece.permission().equals(id)) {
                        return armorPiece;
                    }
                }
            }
        }
        return null;
    }

    public int getHighestUnlockedTier(Player player, ArmorType type) {
        int highest = 0;
        for (ArmorZone zone : zones) {
            for (List<ArmorPiece> tierList : zone.armorPieces()) {
                for (ArmorPiece piece : tierList) {
                    if (piece.type() == type && player.hasPermission(piece.permission())) {
                        // On suppose que l'index dans armorPieces() = palier - 1
                        int tier = zone.armorPieces().indexOf(tierList) + 1;
                        if (tier > highest) highest = tier;
                    }
                }
            }
        }
        return highest;
    }

    public ArmorPiece getArmorPieceAtTier(Player player, ArmorType type, int tier) {
        for (ArmorZone zone : zones) {
            if (tier - 1 < zone.armorPieces().size()) {
                List<ArmorPiece> tierList = zone.armorPieces().get(tier - 1);
                for (ArmorPiece piece : tierList) {
                    if (piece.type() == type) return piece;
                }
            }
        }
        return null;
    }
}
