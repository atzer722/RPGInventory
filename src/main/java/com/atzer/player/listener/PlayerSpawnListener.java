package com.atzer.player.listener;

import com.atzer.RPGInventory;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;

public class PlayerSpawnListener implements Listener {

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        RPGInventory.getInstance().getPlayerDataManager().equipCurrentZone(event.getPlayer());
    }
}
