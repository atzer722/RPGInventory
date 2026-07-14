package com.atzer.listener;

import com.atzer.RPGInventory;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class PlayerJoinEvent implements Listener {

    private final RPGInventory plugin;

    public PlayerJoinEvent(RPGInventory plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(org.bukkit.event.player.PlayerJoinEvent event) {
        this.plugin.equipBestUnlockedStep(event.getPlayer());
    }

}
