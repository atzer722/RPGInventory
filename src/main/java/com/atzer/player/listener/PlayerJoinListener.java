package com.atzer.player.listener;

import com.atzer.RPGInventory;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public final class PlayerJoinListener implements Listener {

    @EventHandler
    public void onPlayerJoinEvent(PlayerJoinEvent event) {
        RPGInventory.getInstance().getPlayerDataManager().playerJoinEventHandler(event.getPlayer());
    }
}
