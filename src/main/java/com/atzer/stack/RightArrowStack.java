package com.atzer.stack;

import com.atzer.RPGInventory;
import com.atzer.menu.MenuHolder;
import com.atzer.section.Section;
import dev.lone.itemsadder.api.CustomStack;
import org.bukkit.entity.HumanEntity;

public class RightArrowStack extends PluginStack {

    public RightArrowStack(MenuHolder menuHolder, HumanEntity player, RPGInventory plugin, CustomStack clickedItem) {
        super(menuHolder, player, plugin, clickedItem);
    }

    @Override
    public void interact() {
        Section rightSection = this.getMenuHolder().getSection().getNextSection();
        if (rightSection == null) return;
        this.getPlugin().openInventory(this.getPlayer(), rightSection, rightSection.getBestUnlocked(this.getPlayer()));
    }
}
