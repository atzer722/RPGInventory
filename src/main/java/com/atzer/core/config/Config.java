package com.atzer.core.config;

import com.atzer.RPGInventory;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;

public final class Config {

    private static FileConfiguration get() {
        return RPGInventory.getInstance().getConfig();
    }

    public String getMenuCommand() {
        return get().getString("menu.command", "menu");
    }

    public ItemStack getMenuBackButton() {
        return RPGInventory.getInstance().getItemStackUtils().stringToItemStack(get().getString("menu.back_button", "minecraft:cobblestone"));
    }

    public ItemStack getMenuLeftArrowButton() {
        return RPGInventory.getInstance().getItemStackUtils().stringToItemStack(get().getString("menu.left_arrow_button", "minecraft:acacia_boat"));
    }

    public ItemStack getMenuNotEquippedButton() {
        return RPGInventory.getInstance().getItemStackUtils().stringToItemStack(get().getString("menu.not_equipped_button", "minecraft:grey_dye"));
    }

    public ItemStack getMenuEquippedButton() {
        return RPGInventory.getInstance().getItemStackUtils().stringToItemStack(get().getString("menu.equipped_button", "minecraft:green_dye"));
    }

    public ItemStack getMenuRightArrowButton() {
        return RPGInventory.getInstance().getItemStackUtils().stringToItemStack(get().getString("menu.right_arrow_button", "minecraft:oak_boat"));
    }

    public ItemStack getMenuHiddenArmorIcon() {
        return RPGInventory.getInstance().getItemStackUtils().stringToItemStack(get().getString("menu.hidden_armor_icon", "minecraft:bedrock"));
    }
}
