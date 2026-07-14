package com.atzer.stack;

import com.atzer.RPGInventory;
import com.atzer.menu.MenuHolder;
import com.atzer.section.Section;
import dev.lone.itemsadder.api.CustomStack;
import org.bukkit.entity.HumanEntity;

public class LeftArrowStack extends PluginStack {

    public LeftArrowStack(MenuHolder menuHolder, HumanEntity player, RPGInventory plugin, CustomStack clickedItem) {
        super(menuHolder, player, plugin, clickedItem);
    }

    @Override
    public void interact() {
        Section leftSection = this.getPlugin().getLeftSection(this.getMenuHolder().getSection());
        if (leftSection == null) return;
        this.getPlugin().openInventory(this.getPlayer(), leftSection, leftSection.getStep1());
    }
}
