package com.atzer;

import com.atzer.core.config.Config;
import com.atzer.core.database.Database;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public final class RPGInventory extends JavaPlugin {

    @Getter
    private static RPGInventory instance;

    private Config pluginConfig;
    private Database database;

    @Override
    public void onEnable() {
        instance = this;
        this.pluginConfig = new Config();
        this.saveDefaultConfig();

        this.database = new Database();
        this.database.init();

        this.getLogger().info("Plugin RPGInventory enabled!");
    }

    @Override
    public void onDisable() {
        this.getLogger().info("Plugin RPGInventory disabled!");
    }
}

