package com.atzer.player;

import com.atzer.PluginRepository;
import com.atzer.RPGInventory;
import lombok.NoArgsConstructor;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@NoArgsConstructor
public class PlayerDataRepository implements PluginRepository<PlayerData, UUID> {

    @Override
    public PlayerData save(PlayerData obj) {
        File file = new File(RPGInventory.getInstance().getDataFolder() + "/data/" + obj.uuid().toString());

        YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration(file);

        if (file.exists()) {
            return this.getPlayerDataFromYaml(obj.uuid(), yamlConfiguration);
        }

        yamlConfiguration.set("equipped_helmet_permission_id", obj.equippedHelmetPermissionId());
        yamlConfiguration.set("equipped_chestplate_permission_id", obj.equippedChestplatePermissionId());
        yamlConfiguration.set("equipped_legging_permission_id", obj.equippedLeggingPermissionId());
        yamlConfiguration.set("equipped_boots_permission_id", obj.equippedBootsPermissionId());
        try {
            yamlConfiguration.save(file);
        } catch (IOException e) {
            RPGInventory.getInstance().getLogger().severe("An error occurred while trying to save the player with uuid " + obj.uuid().toString());
            e.printStackTrace();
            return null;
        }
        return obj;
    }

    @Override
    public Optional<PlayerData> findById(UUID id) {
        File file = new File(RPGInventory.getInstance().getDataFolder() + "/data/" + id.toString());

        if (file.exists()) {
            return Optional.of(this.getPlayerDataFromYaml(id, YamlConfiguration.loadConfiguration(file)));
        }

        return Optional.empty();
    }

    @Override
    public boolean delete(PlayerData obj) {
        return new File(RPGInventory.getInstance().getDataFolder() + "/data/" + obj.uuid().toString()).delete();
    }

    @Override
    public PlayerData update(PlayerData obj) {
        Optional<PlayerData> playerInData = this.findById(obj.uuid());

        if(playerInData.isEmpty()) return this.save(obj);

        File file = new File(RPGInventory.getInstance().getDataFolder() + "/data/" + obj.uuid().toString());
        YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration(file);

        if (!Objects.equals(playerInData.get().equippedHelmetPermissionId(), obj.equippedHelmetPermissionId())) {
            yamlConfiguration.set("equipped_helmet_permission_id", obj.equippedHelmetPermissionId());
        }

        if (!Objects.equals(playerInData.get().equippedChestplatePermissionId(), obj.equippedChestplatePermissionId())) {
            yamlConfiguration.set("equipped_chestplate_permission_id", obj.equippedChestplatePermissionId());
        }

        if (!Objects.equals(playerInData.get().equippedLeggingPermissionId(), obj.equippedLeggingPermissionId())) {
            yamlConfiguration.set("equipped_legging_permission_id", obj.equippedLeggingPermissionId());
        }

        if (!Objects.equals(playerInData.get().equippedBootsPermissionId(), obj.equippedBootsPermissionId())) {
            yamlConfiguration.set("equipped_legging_permission_id", obj.equippedBootsPermissionId());
        }

        try {
            yamlConfiguration.save(file);
        } catch (IOException e) {
            RPGInventory.getInstance().getLogger().severe("An error occurred while trying to update the player with uuid " + obj.uuid().toString());
            e.printStackTrace();
            return null;
        }
        return obj;
    }

    private PlayerData getPlayerDataFromYaml(UUID uuid, YamlConfiguration yaml) {
        return new PlayerData(
                uuid,
                yaml.getString("equipped_helmet_permission_id"),
                yaml.getString("equipped_chestplate_permission_id"),
                yaml.getString("equipped_legging_permission_id"),
                yaml.getString("equipped_boots_permission_id")
        );
    }
}
