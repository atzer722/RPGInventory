package com.atzer.core.config;

import com.atzer.RPGInventory;
import org.bukkit.configuration.file.FileConfiguration;

public final class Config {

    private static FileConfiguration get() {
        return RPGInventory.getInstance().getConfig();
    }
}
