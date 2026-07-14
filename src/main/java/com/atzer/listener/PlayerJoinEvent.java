package com.atzer.listener;

import com.atzer.RPGInventory;
import lombok.RequiredArgsConstructor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

@RequiredArgsConstructor
public class PlayerJoinEvent implements Listener {

    private final RPGInventory plugin;

    @EventHandler
    public void onPlayerJoin(org.bukkit.event.player.PlayerJoinEvent event) {
        this.plugin.equipBestUnlockedStep(event.getPlayer());
    }

}
