package com.atzer;

import com.atzer.armor.ArmorZoneRegistry;
import com.atzer.core.item.ItemStackUtils;
import com.atzer.core.config.ArmorConfig;
import com.atzer.core.config.Config;
import com.atzer.core.error.ErrorHandler;
import com.atzer.inventory.command.RPGInventoryCommand;
import com.atzer.inventory.listener.InventoryClickListener;
import com.atzer.player.listener.PlayerJoinListener;
import com.atzer.player.PlayerDataManager;
import com.atzer.player.PlayerDataRepository;
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

@Getter
public final class RPGInventory extends JavaPlugin {

    @Getter
    private static RPGInventory instance;

    private Config pluginConfig;
    private ArmorConfig armorConfig;
    private ArmorZoneRegistry armorZoneRegistry;
    private PlayerDataManager playerDataManager;
    private LuckPerms luckPermsApi;
    private ItemStackUtils itemStackUtils;
    private ErrorHandler errorHandler;

    @Override
    public void onEnable() {
        instance = this;
        this.pluginConfig = new Config();
        this.saveResource("armors.yml", false);
        this.armorConfig = new ArmorConfig(YamlConfiguration.loadConfiguration(new File(this.getDataFolder() + "/armors.yml")));

        this.armorZoneRegistry = new ArmorZoneRegistry();
        this.armorZoneRegistry.loadRegistry();

        this.playerDataManager = new PlayerDataManager(new PlayerDataRepository());

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

