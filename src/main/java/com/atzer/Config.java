package com.atzer;

import org.bukkit.configuration.file.FileConfiguration;

public enum Config {

    ENABLE(true, "enable"),
    MENUBUTTON("menuIcon", "menu_button"),
    LEFTARROWBUTTON("leftArrowIcon", "left_arrow_button"),
    EQUIPBUTTON("equipIcon", "equip_button"),
    RIGHTARROWBUTTON("rightArrowIcon", "right_arrow_button"),
    UNLOCKEDITEMBUTTON("unlockedItemIcon", "unlocked_item_button"),
    LOCKEDITEMBUTTON("lockedItemIcon", "locked_item_button"),
    UNDEFINEDITEM("undefinedItem", "undefined_item"),
    MENUCOMMAND("menuCommand", "menu_command");

    private final Object defaultValue;
    private final String path;

    Config(Object defaultValue, String path) {
        this.defaultValue = defaultValue;
        this.path = path;
    }

    public Object getDefaultValue() {
        return this.defaultValue;
    }

    public String getPath() {
        return this.path;
    }

    public Object get(FileConfiguration config) {
        Object result = config.get(this.path);

        if (result == null) {
            config.set(this.path, this.defaultValue);
            return this.defaultValue;
        }

        return result;
    }

    public String getString(FileConfiguration config) {
        return String.valueOf(this.get(config));
    }

    public int getInt(FileConfiguration config) {
        return Integer.parseInt(this.getString(config));
    }

    public boolean getBoolean(FileConfiguration config) {
        return Boolean.parseBoolean(this.getString(config));
    }
}
