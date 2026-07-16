package com.atzer.core.item;

import dev.lone.itemsadder.api.CustomStack;
import lombok.NoArgsConstructor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

@NoArgsConstructor
public final class ItemStackUtils {

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
        throw new IllegalArgumentException("The item " + s + " is not a valid item!");
    }

    public void addPersistentDataString(ItemStack item, NamespacedKey key, String data) {
        item.editPersistentDataContainer(pdc -> {
            pdc.set(key, PersistentDataType.STRING, data);
        });
    }

    public String getPersistentDataString(ItemStack item, NamespacedKey key) {
        return item.getPersistentDataContainer().get(key, PersistentDataType.STRING);
    }
}
