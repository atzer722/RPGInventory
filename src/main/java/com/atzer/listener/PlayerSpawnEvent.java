package com.atzer.listener;

import com.atzer.PluginPlayer;
import com.atzer.RPGInventory;
import lombok.RequiredArgsConstructor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

@RequiredArgsConstructor
public class PlayerSpawnEvent implements Listener {

    private final RPGInventory plugin;

    @EventHandler
    public void onPlayerSpawn(org.bukkit.event.player.PlayerRespawnEvent event) {
        this.plugin.equipStep(event.getPlayer(), PluginPlayer.getStepFromPersistentDataContainer(event.getPlayer(), this.plugin));
    }
}
