package com.atzer.listener;

import com.atzer.PluginPlayer;
import com.atzer.RPGInventory;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class PlayerSpawnEvent implements Listener {

    private final RPGInventory plugin;

    public PlayerSpawnEvent(RPGInventory plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerSpawn(org.bukkit.event.player.PlayerRespawnEvent event) {
        this.plugin.equipStep(event.getPlayer(), PluginPlayer.getStepFromPersistentDataContainer(event.getPlayer(), this.plugin));
    }

}
