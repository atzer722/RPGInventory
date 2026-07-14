package com.atzer.stack;

import com.atzer.RPGInventory;
import com.atzer.menu.MenuHolder;
import dev.lone.itemsadder.api.CustomStack;
import org.bukkit.entity.HumanEntity;

public class LeggingsStack extends PluginStack {

    public LeggingsStack(MenuHolder menuHolder, HumanEntity player, RPGInventory plugin, CustomStack clickedItem) {
        super(menuHolder, player, plugin, clickedItem);
    }

    @Override
    public void interact() {
        this.getPlayer().closeInventory();
        this.getPlugin().openInventory(
                this.getPlayer(),
                this.getMenuHolder().getSection(),
                this.getMenuHolder().getSection().getFromLeggings(this.getClickedItem())
        );
    }
}
