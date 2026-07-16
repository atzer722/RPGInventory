package com.atzer.armor;

import com.atzer.RPGInventory;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public final class ArmorZoneRegistry {

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

    public ArmorZone getZoneById(int zoneId) {
        for (ArmorZone zone : zones) {
            if (zone.num() == zoneId) return zone;
        }
        return null;
    }

    public int getMaxZoneId() {
        return this.zones.size();
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
}
