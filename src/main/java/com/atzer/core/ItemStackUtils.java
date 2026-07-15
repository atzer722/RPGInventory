package com.atzer.core;

import dev.lone.itemsadder.api.CustomStack;
import lombok.NoArgsConstructor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

@NoArgsConstructor
public class ItemStackUtils {

    public ItemStack stringToItemStack(String s) {
        if (s.startsWith("minecraft:")) {
            Material material = Material.getMaterial(s.substring(10));

            if (material == null) {
                throw new IllegalArgumentException("The item " + s + " is not a valid item!");
            }

            return ItemStack.of(material);
        }

        if (CustomStack.isInRegistry(s)) {
            return CustomStack.getInstance(s).getItemStack();
        }
        return null;
    }
}
