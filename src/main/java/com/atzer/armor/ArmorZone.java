package com.atzer.armor;

import java.util.List;

public record ArmorZone(
        String title,
        int num,
        List<List<ArmorPiece>> armorPieces
) {
    public List<ArmorPiece> getPiecesOfType(ArmorType type) {
        return armorPieces.stream()
                .flatMap(List::stream)
                .filter(p -> p.type() == type)
                .toList(); // déjà ordonné par palier puisque tiers est ordonné
    }
}
