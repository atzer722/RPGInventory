package com.atzer;

import com.atzer.armor.ArmorZoneRegistry;
import com.atzer.core.config.ArmorConfig;
import com.atzer.core.config.Config;
import com.atzer.core.database.Database;
import lombok.Getter;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

@Getter
public final class RPGInventory extends JavaPlugin {

    @Getter
    private static RPGInventory instance;

    private Config pluginConfig;
    private ArmorConfig armorConfig;
    private Database database;
    private ArmorZoneRegistry armorZoneRegistry;

    @Override
    public void onEnable() {
        instance = this;
        this.pluginConfig = new Config();
        this.armorConfig = new ArmorConfig(YamlConfiguration.loadConfiguration(new File(this.getDataFolder() + "/data/armors.yml")));

        this.armorZoneRegistry = new ArmorZoneRegistry();
        this.armorZoneRegistry.loadRegistry();

        this.saveDefaultConfig();

        this.database = new Database();
        this.database.init();

        this.getLogger().info("Plugin RPGInventory enabled!");
    }

    @Override
    public void onDisable() {
        this.getLogger().info("Plugin RPGInventory disabled!");
    }

    @Override
    public void reloadConfig() {
        super.reloadConfig();
        this.armorConfig = new ArmorConfig(YamlConfiguration.loadConfiguration(new File(this.getDataFolder() + "/data/armors.yml")));
        this.armorZoneRegistry.loadRegistry();
    }
}

