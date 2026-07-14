package com.atzer.stack;

import com.atzer.RPGInventory;
import com.atzer.menu.MenuHolder;
import com.atzer.section.Step;
import dev.lone.itemsadder.api.CustomStack;
import org.bukkit.entity.HumanEntity;

public class EquipStack extends PluginStack {

    public EquipStack(MenuHolder menuHolder, HumanEntity player, RPGInventory plugin, CustomStack clickedItem) {
        super(menuHolder, player, plugin, clickedItem);
    }

    @Override
    public void interact() {
        Step step = this.getMenuHolder().getPointedStep();

        if (step == null) return;
        if (step.isFullUnlocked(this.getPlayer())) step.equip(this.getPlayer(), this.getPlugin(), this.getMenuHolder().getSection());
        this.getPlayer().closeInventory();
    }
}
