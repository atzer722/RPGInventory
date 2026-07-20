package com.atzer.armor;

import com.atzer.RPGInventory;
import com.atzer.core.item.NamespacedKeysEnum;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

public record ArmorPiece(ArmorType type, String itemId, String permission, int tier) {

    public @Nullable ItemStack toItemStack() {
        return this.addData(RPGInventory.getInstance().getItemStackUtils().stringToItemStack(this.itemId));
    }

    private @Nullable ItemStack addData(ItemStack item) {
        if (item == null) return null;

        RPGInventory.getInstance().getItemStackUtils().addPersistentDataString(item, NamespacedKeysEnum.TYPE.getNamespacedKey(), this.type.name());
        RPGInventory.getInstance().getItemStackUtils().addPersistentDataString(item, NamespacedKeysEnum.ITEM_ID.getNamespacedKey(), this.itemId);
        RPGInventory.getInstance().getItemStackUtils().addPersistentDataString(item, NamespacedKeysEnum.PERMISSION.getNamespacedKey(), this.permission);
        RPGInventory.getInstance().getItemStackUtils().addPersistentDataString(item, NamespacedKeysEnum.TIER.getNamespacedKey(), String.valueOf(this.tier));
        return item;
    }
}