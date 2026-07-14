package com.atzer.menu;

import com.atzer.section.Section;
import com.atzer.section.Step;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

@Getter
@Setter
@AllArgsConstructor
public class MenuHolder implements InventoryHolder {

    private final Inventory inventory;
    private final Section section;
    private final Step pointedStep;
}
