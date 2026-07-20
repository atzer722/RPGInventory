package com.atzer.core.item;

import dev.lone.itemsadder.api.CustomStack;
import lombok.NoArgsConstructor;
import net.Indyuce.mmoitems.MMOItems;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.Nullable;

@NoArgsConstructor
public final class ItemStackUtils {

    public ItemStack stringToItemStack(String s) {
        if (s.startsWith("minecraft:")) {
            Material material = Material.getMaterial(s.substring(10).toUpperCase());

            if (material == null) {
                throw new IllegalArgumentException("The item " + s + " is not a valid item!");
            }

            return ItemStack.of(material);
        }

        if (CustomStack.isInRegistry(s)) {
            return CustomStack.getInstance(s).getItemStack();
        }

        String[] sArray = s.split(":");
        if (sArray.length == 2) {
            return MMOItems.plugin.getItem(sArray[0], sArray[1]);
        }

        throw new IllegalArgumentException("The item " + s + " is not a valid item!");
    }

    public void addPersistentDataString(ItemStack item, NamespacedKey key, String data) {
        item.editPersistentDataContainer(pdc -> {
            pdc.set(key, PersistentDataType.STRING, data);
        });
    }

    public @Nullable String getPersistentDataString(ItemStack item, NamespacedKey key) {
        return item.getPersistentDataContainer().get(key, PersistentDataType.STRING);
    }
}
