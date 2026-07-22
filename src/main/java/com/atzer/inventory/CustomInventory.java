package com.atzer.inventory;

import com.atzer.RPGInventory;
import com.atzer.armor.ArmorPiece;
import com.atzer.armor.ArmorType;
import com.atzer.armor.ArmorZone;
import com.atzer.player.PlayerData;
import dev.lone.itemsadder.api.FontImages.FontImageWrapper;
import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import java.util.List;

@Getter
public final class CustomInventory implements InventoryHolder {

    private final Inventory inventory;
    private final ArmorZone armorZone;

    public CustomInventory(RPGInventory plugin, ArmorZone armorZone) {
        this.inventory = plugin.getServer().createInventory(this, 54, buildTitle(armorZone));
        this.armorZone = armorZone;
    }

    private static Component buildTitle(ArmorZone armorZone) {
        FontImageWrapper bg = FontImageWrapper.instance(RPGInventory.getInstance().getPluginConfig().getMenuInventoryTexture());

        Component background;
        if (bg != null && bg.exists()) {
            background = LegacyComponentSerializer.legacySection().deserialize(bg.getString());
        } else {
            background = Component.empty();
        }

        Component zoneName = Component.text(armorZone.title())
                .color(NamedTextColor.WHITE)
                .decoration(TextDecoration.ITALIC, false)
                .decoration(TextDecoration.BOLD, false);

        return background.append(zoneName);
    }

    public static void openInventory(Player player, ArmorZone zone) {
        CustomInventory inv = new CustomInventory(RPGInventory.getInstance(), zone);
        inv.setArmorZoneItems(player);
        player.openInventory(inv.getInventory());
    }

    public void setArmorZoneItems(Player player) {
        List<ArmorPiece> headerPieces =     this.armorZone.getPiecesOfType(ArmorType.HEADER);
        List<ArmorPiece> helmetPieces =     this.armorZone.getPiecesOfType(ArmorType.HELMET);
        List<ArmorPiece> chestplatePieces = this.armorZone.getPiecesOfType(ArmorType.CHESTPLATE);
        List<ArmorPiece> leggingPieces =    this.armorZone.getPiecesOfType(ArmorType.LEGGING);
        List<ArmorPiece> bootPieces =       this.armorZone.getPiecesOfType(ArmorType.BOOTS);

        PlayerData playerData = RPGInventory.getInstance().getPlayerDataManager().getPlayerData(player);

        if (playerData == null) {
            this.inventory.close();
            return;
        }

        this.setFirstLine();
        this.setSecondLine(headerPieces, playerData);
        this.setLastLine(
                helmetPieces,
                RPGInventory.getInstance().getPlayerDataManager().getHighestUnlockedTier(player, ArmorType.HELMET, this.armorZone),
                18
        );
        this.setLastLine(
                chestplatePieces,
                RPGInventory.getInstance().getPlayerDataManager().getHighestUnlockedTier(player, ArmorType.CHESTPLATE, this.armorZone),
                27
        );
        this.setLastLine(
                leggingPieces,
                RPGInventory.getInstance().getPlayerDataManager().getHighestUnlockedTier(player, ArmorType.LEGGING, this.armorZone),
                36
        );
        this.setLastLine(
                bootPieces,
                RPGInventory.getInstance().getPlayerDataManager().getHighestUnlockedTier(player, ArmorType.BOOTS, this.armorZone),
                45
        );
    }

    private void setFirstLine() {
        this.inventory.setItem(8, RPGInventory.getInstance().getPluginConfig().getMenuBackButton()); //Back button
    }

    private void setSecondLine(List<ArmorPiece> headerPieces, PlayerData playerData) {
        if (this.armorZone.num() != 1) { // Useless to render if there is no left armors.
            this.inventory.setItem(9, RPGInventory.getInstance().getPluginConfig().getMenuLeftArrowButton()); //Left arrow to get the left armor.
        }

        if (this.armorZone.num() == playerData.armorZoneId()) {
            this.inventory.setItem(10, RPGInventory.getInstance().getPluginConfig().getMenuEquippedButton());
        } else {
            this.inventory.setItem(10, RPGInventory.getInstance().getPluginConfig().getMenuNotEquippedButton());
        }

        if (this.armorZone.num() != RPGInventory.getInstance().getArmorZoneRegistry().getMaxZoneId()) {
            this.inventory.setItem(11, RPGInventory.getInstance().getPluginConfig().getMenuRightArrowButton());
        }

        // HEADER
        this.inventory.setItem(12, headerPieces.getFirst().toItemStack());
        this.inventory.setItem(13, headerPieces.get(1).toItemStack());
        this.inventory.setItem(14, headerPieces.get(2).toItemStack());
        this.inventory.setItem(15, headerPieces.get(3).toItemStack());
        this.inventory.setItem(16, headerPieces.get(4).toItemStack());
        this.inventory.setItem(17, headerPieces.getLast().toItemStack());
    }

    private void setLastLine(List<ArmorPiece> pieces, ArmorPiece highestPiece, int firstId) {
        int highestTier = (highestPiece != null) ? highestPiece.tier() : 0;

        if (highestPiece == null) {
            this.inventory.setItem(firstId + 1, RPGInventory.getInstance().getPluginConfig().getMenuHiddenArmorIcon());
        } else {
            this.inventory.setItem(firstId + 1, highestPiece.toItemStack());
        }

        for (int i = 0; i < pieces.size(); i++) {
            ArmorPiece piece = pieces.get(i);
            ItemStack display = (piece.tier() > highestTier)
                    ? RPGInventory.getInstance().getPluginConfig().getMenuHiddenArmorIcon()
                    : piece.toItemStack();
            this.inventory.setItem(firstId + 3 + i, display);
        }
    }
}