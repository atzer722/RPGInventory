package com.atzer.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import java.util.Arrays;

public class OnPlayerDieEvent implements Listener {

    @EventHandler
    public void onPlayerDieEvent(PlayerDeathEvent event) {
        event.getDrops().removeAll(Arrays.stream(event.getEntity().getInventory().getArmorContents()).toList());
    }

}
