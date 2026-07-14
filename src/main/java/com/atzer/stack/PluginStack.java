package com.atzer.stack;

import com.atzer.RPGInventory;
import com.atzer.menu.MenuHolder;
import dev.lone.itemsadder.api.CustomStack;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.HumanEntity;

@Getter
@RequiredArgsConstructor
public abstract class PluginStack {

    private final MenuHolder menuHolder;
    private final HumanEntity player;
    private final RPGInventory plugin;
    private final CustomStack clickedItem;

    public abstract void interact();
}
