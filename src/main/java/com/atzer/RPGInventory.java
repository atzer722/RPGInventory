package com.atzer;

import com.atzer.armor.ArmorZoneRegistry;
import com.atzer.core.config.ArmorConfig;
import com.atzer.core.config.Config;
import com.atzer.player.PlayerDataManager;
import com.atzer.player.PlayerDataRepository;
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
    private ArmorZoneRegistry armorZoneRegistry;
    private PlayerDataManager playerDataManager;

    @Override
    public void onEnable() {
        instance = this;
        this.pluginConfig = new Config();
        this.armorConfig = new ArmorConfig(YamlConfiguration.loadConfiguration(new File(this.getDataFolder() + "/armors.yml")));

        this.armorZoneRegistry = new ArmorZoneRegistry();
        this.armorZoneRegistry.loadRegistry();

        this.playerDataManager = new PlayerDataManager(new PlayerDataRepository());

        this.saveDefaultConfig();

        this.getLogger().info("Plugin RPGInventory enabled!");
    }

    @Override
    public void onDisable() {
        this.getLogger().info("Plugin RPGInventory disabled!");
    }

    @Override
    public void reloadConfig() {
        super.reloadConfig();
        this.armorConfig = new ArmorConfig(YamlConfiguration.loadConfiguration(new File(this.getDataFolder() + "/armors.yml")));
        this.armorZoneRegistry.loadRegistry();
    }
}

