package com.atzer;

import com.atzer.armor.ArmorZoneRegistry;
import com.atzer.core.Database;
import com.atzer.core.config.ArmorConfig;
import com.atzer.core.config.Config;
import com.atzer.core.error.ErrorHandler;
import com.atzer.core.item.ItemStackUtils;
import com.atzer.inventory.command.RPGInventoryCommand;
import com.atzer.inventory.listener.InventoryClickListener;
import com.atzer.player.PlayerData;
import com.atzer.player.PlayerDataManager;
import com.atzer.player.listener.PlayerDeathListener;
import com.atzer.player.listener.PlayerJoinListener;
import com.atzer.player.listener.PlayerSpawnListener;
import com.atzer.player.repositories.PlayerDataRepositorySql;
import com.atzer.player.repositories.PlayerDataRepositoryYaml;
import com.atzer.reload.command.ReloadCommand;
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
import java.util.Objects;
import java.util.UUID;

@Getter
public final class RPGInventory extends JavaPlugin {

    @Getter
    private static RPGInventory instance;

    private Config pluginConfig;
    private ArmorConfig armorConfig;
    private Database database;
    private ArmorZoneRegistry armorZoneRegistry;
    private PluginRepository<PlayerData, UUID> playerDataRepository;
    private PlayerDataManager playerDataManager;
    private LuckPerms luckPermsApi;
    private ItemStackUtils itemStackUtils;
    private ErrorHandler errorHandler;

    @Override
    public void onEnable() {
        instance = this;
        this.pluginConfig = new Config();
        this.database = new Database();
        this.database.init();
        this.saveResource("armors.yml", false);
        this.armorConfig = new ArmorConfig(YamlConfiguration.loadConfiguration(new File(this.getDataFolder() + "/armors.yml")));

        this.armorZoneRegistry = new ArmorZoneRegistry();
        this.armorZoneRegistry.loadRegistry();

        this.playerDataRepository = new PlayerDataRepositoryYaml();
        this.playerDataManager = new PlayerDataManager();
        this.itemStackUtils = new ItemStackUtils();
        this.errorHandler = new ErrorHandler();

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

        Objects.requireNonNull(this.getCommand("inventory")).setExecutor(new RPGInventoryCommand());
        Objects.requireNonNull(this.getCommand("reload")).setExecutor(new ReloadCommand());
        this.getServer().getPluginManager().registerEvents(new PlayerJoinListener(), this);
        this.getServer().getPluginManager().registerEvents(new InventoryClickListener(), this);
        this.getServer().getPluginManager().registerEvents(new PlayerDeathListener(), this);
        this.getServer().getPluginManager().registerEvents(new PlayerSpawnListener(), this);

        this.getLogger().info("Plugin RPGInventory enabled!");
    }

    @Override
    public void onDisable() {
        this.database.close();
        this.getLogger().info("Database disconnected!");

        this.getLogger().info("Plugin RPGInventory disabled!");
    }

    @Override
    public void reloadConfig() {
        super.reloadConfig();
        this.saveResource("armors.yml", false);
        this.armorConfig = new ArmorConfig(YamlConfiguration.loadConfiguration(new File(this.getDataFolder() + "/armors.yml")));
        this.armorZoneRegistry.loadRegistry();
    }

    private void setPlayerDataRepository() {
        if (this.pluginConfig.getDataMotor().equalsIgnoreCase("yaml")) {
            this.playerDataRepository = new PlayerDataRepositoryYaml();
        } else {
            this.playerDataRepository = new PlayerDataRepositorySql();
        }
    }
}

