package com.atzer.listener;

import com.atzer.PluginPlayer;
import com.atzer.RPGInventory;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class OnPlayerJoinEvent implements Listener {

    private final RPGInventory plugin;

    public OnPlayerJoinEvent(RPGInventory plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(org.bukkit.event.player.PlayerJoinEvent event) {
        if (PluginPlayer.getStepFromPersistentDataContainer(event.getPlayer(), this.plugin) == null) {
            this.plugin.equipStep(event.getPlayer(), this.plugin.getBestUnlockedStepPair(event.getPlayer()));
            return;
        }

        this.plugin.equipStep(event.getPlayer(), PluginPlayer.getStepFromPersistentDataContainer(event.getPlayer(), this.plugin));
    }
}
