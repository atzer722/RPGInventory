package com.atzer;

import com.atzer.core.config.Config;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public final class RPGInventory extends JavaPlugin {

    @Getter
    private static RPGInventory instance;

    private Config pluginConfig;

    @Override
    public void onEnable() {
        instance = this;
        this.pluginConfig = new Config();
        this.saveDefaultConfig();

        this.getLogger().info("Plugin RPGInventory enabled!");
    }

    @Override
    public void onDisable() {
        this.getLogger().info("Plugin RPGInventory disabled!");
    }
}

