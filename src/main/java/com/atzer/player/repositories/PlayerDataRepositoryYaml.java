package com.atzer.player.repositories;

import com.atzer.PluginRepository;
import com.atzer.RPGInventory;
import com.atzer.player.PlayerData;
import lombok.NoArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@NoArgsConstructor
public final class PlayerDataRepositoryYaml extends PluginRepository<PlayerData, UUID> {

    @Override
    public void init() {}

    @Override
    public CompletableFuture<PlayerData> save(PlayerData obj) {
        CompletableFuture<PlayerData> future = new CompletableFuture<>();

        Bukkit.getScheduler().runTaskAsynchronously(RPGInventory.getInstance(), () -> {
            File file = new File(RPGInventory.getInstance().getDataFolder() + "/data/" + obj.uuid().toString());
            YamlConfiguration yaml = YamlConfiguration.loadConfiguration(file);

            if (file.exists()) {
                future.complete(getPlayerDataFromYaml(obj.uuid(), yaml));
                return;
            }

            yaml.set("armor_zone_id", obj.armorZoneId());
            try {
                yaml.save(file);
                future.complete(obj);
            } catch (IOException e) {
                future.completeExceptionally(e);
            }
        });

        return future;
    }

    @Override
    public CompletableFuture<Optional<PlayerData>> findById(UUID id) {
        CompletableFuture<Optional<PlayerData>> future = new CompletableFuture<>();

        Bukkit.getScheduler().runTaskAsynchronously(RPGInventory.getInstance(), () -> {
            File file = new File(RPGInventory.getInstance().getDataFolder() + "/data/" + id.toString());

            if  (file.exists()) {
                future.complete(Optional.of(this.getPlayerDataFromYaml(id, YamlConfiguration.loadConfiguration(file))));
                return;
            }

            future.complete(Optional.empty());
        });
        return future;
    }

    @Override
    public CompletableFuture<Boolean> delete(PlayerData obj) {
        CompletableFuture<Boolean> future = new CompletableFuture<>();

        Bukkit.getScheduler().runTaskAsynchronously(RPGInventory.getInstance(), () -> {
            future.complete(new File(RPGInventory.getInstance().getDataFolder() + "/data/" + obj.uuid().toString()).delete());
        });
        return future;
    }

    @Override
    public CompletableFuture<PlayerData> update(PlayerData obj) {
        CompletableFuture<PlayerData> future = new CompletableFuture<>();

        Bukkit.getScheduler().runTaskAsynchronously(RPGInventory.getInstance(), () -> {
            Optional<PlayerData> playerInData = this.findById(obj.uuid()).join();

            if (playerInData.isEmpty()) {
                future.complete(this.save(obj).join());
                return;
            }

            File file = new File(RPGInventory.getInstance().getDataFolder() + "/data/" + obj.uuid().toString());
            YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration(file);

            if (!Objects.equals(playerInData.get().armorZoneId(), obj.armorZoneId())) {
                yamlConfiguration.set("armor_zone_id", obj.armorZoneId());
            }

            try {
                yamlConfiguration.save(file);
            } catch (IOException e) {
                future.completeExceptionally(e);
            }
            future.complete(obj);
        });
        return future;
    }

    private PlayerData getPlayerDataFromYaml(UUID uuid, YamlConfiguration yaml) {
        return new PlayerData(
                uuid,
                yaml.getInt("armor_zone_id", 1)
        );
    }
}
