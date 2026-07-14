package com.atzer.armor;

import com.atzer.RPGInventory;
import lombok.RequiredArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@RequiredArgsConstructor
public class ArmorZoneRegistry {

    private final Set<ArmorZone> zones = new HashSet<>();

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
}
