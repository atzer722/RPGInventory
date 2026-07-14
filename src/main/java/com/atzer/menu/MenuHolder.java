package com.atzer.menu;

import com.atzer.RPGInventory;
import com.atzer.section.Section;
import com.atzer.section.Step;
import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.jetbrains.annotations.NotNull;

public class MenuHolder implements InventoryHolder {

    private final Inventory inventory;
    private final Section section;
    private final Step pointedStep;

    public MenuHolder(RPGInventory plugin, Section section, HumanEntity player, Step pointedStep) {
        this.pointedStep = pointedStep;
        this.section = section;
        this.inventory = section.toInventory(player, plugin, this);
    }

    @Override
    public @NotNull Inventory getInventory() {
        return this.inventory;
    }

    public Section getSection() {
        return this.section;
    }

    public Step getPointedStep() {
        return this.pointedStep;
    }
}
