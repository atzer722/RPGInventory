package com.atzer.armor;

import dev.lone.itemsadder.api.CustomStack;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

public record ArmorPiece(ArmorType type, String itemId, String permission) {

    public @Nullable ItemStack toItemStack() {
        if (this.itemId.startsWith("minecraft:")) {
            Material material = Material.getMaterial(this.itemId.substring(10));

            if (material == null) return null;

            return new ItemStack(material);
        }

        CustomStack stack = CustomStack.getInstance(this.itemId);

        return stack.getItemStack();
    }
}
