package com.atzer.core.config;

import com.atzer.RPGInventory;
import org.bukkit.configuration.file.FileConfiguration;

public final class Config {

    private static FileConfiguration get() {
        return RPGInventory.getInstance().getConfig();
    }

    public String getDatabaseMotor() {
        return get().getString("database.motor", "sqlite");
    }

    public String getDatabaseHost() {
        return get().getString("database.host", "127.0.0.1");
    }

    public int getDatabasePort() {
        return get().getInt("database.port", 3306);
    }

    public String getDatabaseUsername() {
        return get().getString("database.username", "root");
    }

    public String getDatabasePassword() {
        return get().getString("database.password", "");
    }

    public String getDatabaseName() {
        return get().getString("database.name", "rpginventory");
    }
}
