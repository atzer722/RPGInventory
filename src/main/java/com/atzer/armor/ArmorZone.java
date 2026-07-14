package com.atzer.armor;

import java.util.Set;

public record ArmorZone(
        String title,
        int num,
        Set<Set<ArmorPiece>> armorPieces
) {
}
