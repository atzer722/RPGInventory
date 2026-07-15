package com.atzer.armor;

import com.atzer.RPGInventory;
import dev.lone.itemsadder.api.CustomStack;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

public record ArmorPiece(ArmorType type, String itemId, String permission, int tier) {

    public @Nullable ItemStack toItemStack() {
        if (this.itemId.startsWith("minecraft:")) {
            Material material = Material.getMaterial(this.itemId.substring(10));

            if (material == null) return null;

            return this.addData(new ItemStack(material));
        }

        CustomStack stack = CustomStack.getInstance(this.itemId);

        return this.addData(stack.getItemStack());
    }

    private @Nullable ItemStack addData(ItemStack item) {
        if (item == null) return null;

        RPGInventory.getInstance().getItemStackUtils().addPersistentDataString(item, NamespacedKeysEnum.TYPE.getNamespacedKey(), this.type.name());
        RPGInventory.getInstance().getItemStackUtils().addPersistentDataString(item, NamespacedKeysEnum.ITEM_ID.getNamespacedKey(), this.itemId);
        RPGInventory.getInstance().getItemStackUtils().addPersistentDataString(item, NamespacedKeysEnum.PERMISSION.getNamespacedKey(), this.permission);
        RPGInventory.getInstance().getItemStackUtils().addPersistentDataString(item, NamespacedKeysEnum.TIER.getNamespacedKey(), String.valueOf(this.tier));
        return item;
    }

    @RequiredArgsConstructor
    public enum NamespacedKeysEnum {
        TYPE(new NamespacedKey(RPGInventory.getInstance(), "type")),
        ITEM_ID(new NamespacedKey(RPGInventory.getInstance(), "item_id")),
        PERMISSION(new NamespacedKey(RPGInventory.getInstance(), "permission")),
        TIER(new NamespacedKey(RPGInventory.getInstance(), "tier"));

        @Getter
        private final NamespacedKey namespacedKey;
    }
}
