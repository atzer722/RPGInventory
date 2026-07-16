package com.atzer.core.config;

import com.atzer.RPGInventory;
import com.atzer.core.item.NamespacedKeysEnum;
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
        return this.getConfigItem("menu.back_button", "minecraft:cobblestone", InteractionType.MENU_BACK_BUTTON);
    }

    public ItemStack getMenuLeftArrowButton() {
        return this.getConfigItem("menu.left_arrow_button", "minecraft:acacia_boat", InteractionType.MENU_LEFT_ARROW_BUTTON);
    }

    public ItemStack getMenuNotEquippedButton() {
        return this.getConfigItem("menu.not_equipped_button", "minecraft:grey_dye", InteractionType.MENU_NOT_EQUIPPED_BUTTON);
    }

    public ItemStack getMenuEquippedButton() {
        return this.getConfigItem("menu.equipped_button", "minecraft:green_dye", InteractionType.MENU_EQUIPPED_BUTTON);
    }

    public ItemStack getMenuRightArrowButton() {
        return this.getConfigItem("menu.right_arrow_button", "minecraft:oak_boat", InteractionType.MENU_RIGHT_ARROW_BUTTON);
    }

    public ItemStack getMenuHiddenArmorIcon() {
        return this.getConfigItem("menu.hidden_armor_icon", "minecraft:bedrock", InteractionType.MENU_HIDDEN_ARMOR_BUTTON);
    }

    private ItemStack getConfigItem(String path, String defaultValue, InteractionType type) {
        ItemStack item = RPGInventory.getInstance().getItemStackUtils().stringToItemStack(get().getString(path, defaultValue));
        RPGInventory.getInstance().getItemStackUtils().addPersistentDataString(item, NamespacedKeysEnum.CONFIG_TYPE.getNamespacedKey(), type.name());
        return item;
    }

    public enum InteractionType {
        MENU_BACK_BUTTON,
        MENU_LEFT_ARROW_BUTTON,
        MENU_NOT_EQUIPPED_BUTTON,
        MENU_EQUIPPED_BUTTON,
        MENU_RIGHT_ARROW_BUTTON,
        MENU_HIDDEN_ARMOR_BUTTON;

        public static InteractionType getMatch(String name) {
            for (InteractionType v : values()) {
                if (v.name().equalsIgnoreCase(name)) return v;
            }
            return null;
        }
    }
}
