package com.atzer.core.config;

import com.atzer.RPGInventory;
import com.atzer.core.item.NamespacedKeysEnum;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public final class Config {

    private static FileConfiguration get() {
        return RPGInventory.getInstance().getConfig();
    }

    public String getMenuCommand() {
        return get().getString("menu.command", "menu");
    }

    public boolean getMenuServerExecution() {
        return get().getBoolean("menu.server_execution", false);
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

    private Map<String, Object> checkConfig() {
        Map<String, Object> map = new HashMap<>();

        if (get().getConfigurationSection("menu.command") == null) {
            map.put("menu.command", "dm open main_menu {player}");
        }

        if (get().getConfigurationSection("menu.server_execution") == null) {
            map.put("menu.server_execution", true);
        }

        if (get().getConfigurationSection("menu.back_button") == null) {
            map.put("menu.back_button", "minecraft:cobblestone");
        }

        if (get().getConfigurationSection("menu.left_arrow_button") == null) {
            map.put("menu.left_arrow_button", "minecraft:acacia_boat");
        }

        if (get().getConfigurationSection("menu.not_equipped_button") == null) {
            map.put("menu.not_equipped_button", "minecraft:gray_dye");
        }

        if (get().getConfigurationSection("menu.equipped_button") == null) {
            map.put("menu.equipped_button", "minecraft:green_dye");
        }

        if (get().getConfigurationSection("menu.right_arrow_button") == null) {
            map.put("menu.right_arrow_button", "minecraft:oak_boat");
        }

        if (get().getConfigurationSection("menu.hidden_armor_icon") == null) {
            map.put("menu.hidden_armor_icon", "minecraft:bedrock");
        }

        return map;
    }

    public Map<String, Object> updateConfig() {
        Map<String, Object> checkMap = checkConfig();

        for (Map.Entry<String, Object> entry : checkMap.entrySet()) {
            get().set(entry.getKey(), entry.getValue());
        }
        RPGInventory.getInstance().saveConfig();
        return checkMap;
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
