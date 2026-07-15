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

        yamlConfiguration.set("armor_zone_id", obj.armorZoneId());
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

        if (!Objects.equals(playerInData.get().armorZoneId(), obj.armorZoneId())) {
            yamlConfiguration.set("armor_zone_id", obj.armorZoneId());
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
                yaml.getInt("armor_zone_id", 1)
        );
    }
}
