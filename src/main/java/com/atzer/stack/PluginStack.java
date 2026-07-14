package com.atzer.stack;

import com.atzer.RPGInventory;
import com.atzer.menu.MenuHolder;
import dev.lone.itemsadder.api.CustomStack;
import org.bukkit.entity.HumanEntity;

public abstract class PluginStack {

    private final MenuHolder menuHolder;
    private final HumanEntity player;
    private final RPGInventory plugin;
    private final CustomStack clickedItem;

    public PluginStack(MenuHolder menuHolder, HumanEntity player, RPGInventory plugin, CustomStack clickedItem) {
        this.menuHolder = menuHolder;
        this.player = player;
        this.plugin = plugin;
        this.clickedItem = clickedItem;
    }

    public MenuHolder getMenuHolder() {
        return menuHolder;
    }

    public HumanEntity getPlayer() {
        return player;
    }

    public RPGInventory getPlugin() {
        return plugin;
    }

    public CustomStack getClickedItem() {
        return clickedItem;
    }

    public abstract void interact();
}
