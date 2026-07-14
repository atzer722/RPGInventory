package com.atzer;

import lombok.NoArgsConstructor;
import org.bukkit.configuration.file.FileConfiguration;

@NoArgsConstructor
public final class Config {

    private static FileConfiguration get() {
        return RPGInventory.getInstance().getConfig();
    }

    public boolean getPLuginEnable() {
        return get().getBoolean("enable", false);
    }

    public String getMenuCommand() {
        return get().getString("menu_command", "menu");
    }

    public String getMenuButton() {
        return get().getString("menu_button", "_iainternal:icon_next_orange");
    }

    public String getLeftArrowButton() {
        return get().getString("left_arrow_button", "_iainternal:icon_back_orange");
    }

    public String getEquipButton() {
        return get().getString("equip_button", "_iainternal:icon_next_orange");
    }

    public String getRightArrowButton() {
        return get().getString("right_arrow_button", "_iainternal:icon_right_blue");
    }

    public String getUnlockedItemButton() {
        return get().getString("unlocked_item_button", "_iainternal:icon_cancel");
    }

    public String getLOckedItemButton() {
        return get().getString("locked_item_button", "_iainternal:icon_cancel");
    }

    public String getUndefinedItem() {
        return get().getString("undefined_item", "_iainternal:icon_cancel");
    }
}