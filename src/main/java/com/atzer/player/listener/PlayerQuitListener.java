package com.atzer.player.listener;

import com.atzer.RPGInventory;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuitListener implements Listener {

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        RPGInventory.getInstance().getPlayerDataManager().playerQuitEventHandler(event.getPlayer());
    }
}
