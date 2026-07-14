package com.atzer.stack;

import com.atzer.Config;
import com.atzer.RPGInventory;
import com.atzer.menu.MenuHolder;
import dev.lone.itemsadder.api.CustomStack;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;

public class MenuStack extends PluginStack {

    public MenuStack(MenuHolder menuHolder, HumanEntity player, RPGInventory plugin, CustomStack clickedItem) {
        super(menuHolder, player, plugin, clickedItem);
    }

    @Override
    public void interact() {
        this.getPlayer().closeInventory();
        ((Player) this.getPlayer()).performCommand(Config.MENUCOMMAND.getString(this.getPlugin().getConfig()));
    }
}
