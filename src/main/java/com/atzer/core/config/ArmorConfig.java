package com.atzer.core.config;

import com.atzer.armor.ArmorPiece;
import com.atzer.armor.ArmorType;
import lombok.RequiredArgsConstructor;
import org.bukkit.configuration.file.YamlConfiguration;

import java.util.List;
import java.util.Set;

@RequiredArgsConstructor
public final class ArmorConfig {

    private final YamlConfiguration configuration;

    private YamlConfiguration get() {
        return this.configuration;
    }

    public Set<String> getZoneList() {
        return get().getKeys(false);
    }

    public String getZoneTitle(int zoneId) {
        return get().getString(zoneId + ".title");
    }

    public String getArmorPieceId(int zoneId, int step, ArmorType type) {
        return get().getString(zoneId + ".steps." + step + "." + type.name() + ".id", "minecraft:bedrock");
    }

    public String getArmorPiecePermissionId(int zoneId, int step, ArmorType type) {
        return get().getString(
                zoneId + ".steps." + step + "." + type.name() + ".permission_id",
                "zone_" + zoneId + "_step_" + step + "_" + type.name()
        );
    }

    public List<ArmorPiece> getArmorPieceByStep(int zoneId, int step) {
        return List.of(
                new ArmorPiece(
                        ArmorType.HEADER,
                        this.getArmorPieceId(zoneId, step, ArmorType.HEADER),
                        this.getArmorPiecePermissionId(zoneId, step, ArmorType.HEADER),
                        step
                ),
                new ArmorPiece(
                        ArmorType.HELMET,
                        this.getArmorPieceId(zoneId, step, ArmorType.HELMET),
                        this.getArmorPiecePermissionId(zoneId, step, ArmorType.HELMET),
                        step
                ),
                new ArmorPiece(
                        ArmorType.CHESTPLATE,
                        this.getArmorPieceId(zoneId, step, ArmorType.CHESTPLATE),
                        this.getArmorPiecePermissionId(zoneId, step, ArmorType.CHESTPLATE),
                        step
                ),
                new ArmorPiece(
                        ArmorType.LEGGING,
                        this.getArmorPieceId(zoneId, step, ArmorType.LEGGING),
                        this.getArmorPiecePermissionId(zoneId, step, ArmorType.LEGGING),
                        step
                ),
                new ArmorPiece(
                        ArmorType.BOOTS,
                        this.getArmorPieceId(zoneId, step, ArmorType.BOOTS),
                        this.getArmorPiecePermissionId(zoneId, step, ArmorType.BOOTS),
                        step
                )
        );
    }

    public List<List<ArmorPiece>> getArmorPieceByZone(int zoneId) {
        return List.of(
                this.getArmorPieceByStep(zoneId, 1),
                this.getArmorPieceByStep(zoneId, 2),
                this.getArmorPieceByStep(zoneId, 3),
                this.getArmorPieceByStep(zoneId, 4),
                this.getArmorPieceByStep(zoneId, 5),
                this.getArmorPieceByStep(zoneId, 6)
        );
    }
}
