package com.atzer.armor;

import java.util.List;

public record ArmorZone(
        String title,
        int num,
        List<List<ArmorPiece>> armorPieces
) {
}
