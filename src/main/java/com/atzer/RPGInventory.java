package com.atzer;

import com.atzer.armor.ArmorZoneRegistry;
import com.atzer.core.config.ArmorConfig;
import com.atzer.core.config.Config;
import com.atzer.player.PlayerJoinListener;
import com.atzer.player.PlayerDataManager;
import com.atzer.player.PlayerDataRepository;
import lombok.Getter;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.event.node.NodeMutateEvent;
import net.luckperms.api.model.user.User;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
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
    private LuckPerms luckPermsApi;

    @Override
    public void onEnable() {
        instance = this;
        this.pluginConfig = new Config();
        this.armorConfig = new ArmorConfig(YamlConfiguration.loadConfiguration(new File(this.getDataFolder() + "/armors.yml")));

        this.armorZoneRegistry = new ArmorZoneRegistry();
        this.armorZoneRegistry.loadRegistry();

        this.playerDataManager = new PlayerDataManager(new PlayerDataRepository());

        this.saveDefaultConfig();

        luckPermsApi = LuckPermsProvider.get();

        luckPermsApi.getEventBus().subscribe(this, NodeMutateEvent.class, event -> {
            if (!(event.getTarget() instanceof User user)) return;

            Player player = Bukkit.getPlayer(user.getUniqueId());
            if (player == null || !player.isOnline()) return;

            Bukkit.getScheduler().runTaskLater(this, () -> {
                playerDataManager.playerPermissionChangeEventHandler(player);
            }, 1L);
        });

        Bukkit.getPluginManager().registerEvents(new PlayerJoinListener(), this);

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

