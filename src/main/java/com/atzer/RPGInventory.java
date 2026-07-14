package com.atzer;

import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public class RPGInventory extends JavaPlugin {

    @Override
    public void onEnable() {
    }

    @Override
    public void onDisable() {
        this.getLogger().info("Plugin RPGInventory disabled!");
    }
}

