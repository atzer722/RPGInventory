package com.atzer.core.database;

import com.atzer.RPGInventory;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public final class Database {

    private HikariDataSource dataSource;

    public void init() {
        this.dataSource = new HikariDataSource(this.getDataSource());
    }

    public void close() {
        if (this.dataSource != null) {
            this.dataSource.close();
        }
        this.dataSource = null;
    }

    private HikariConfig getHikariConfig() {
        final HikariConfig config = new HikariConfig();

        switch (RPGInventory.getInstance().getPluginConfig().getDatabaseMotor()) {
            case "sqlite" -> {
                RPGInventory.getInstance().saveResource("database.db", false);

                config.setJdbcUrl("jdbc:sqlite:" + RPGInventory.getInstance().getDataFolder() + "/database.db");
                config.setDriverClassName("org.sqlite.JDBC");
            }
            case "mysql" -> {
                config.setJdbcUrl("jdbc:mysql://" +
                        RPGInventory.getInstance().getPluginConfig().getDatabaseHost() + ":" +
                        RPGInventory.getInstance().getPluginConfig().getDatabasePort() + "/" +
                        RPGInventory.getInstance().getPluginConfig().getDatabaseName()
                );

                config.setDriverClassName("com.mysql.cj.jdbc.Driver");
            }
            case "mariadb" -> {
                config.setJdbcUrl("jdbc:mariadb://" +
                        RPGInventory.getInstance().getPluginConfig().getDatabaseHost() + ":" +
                        RPGInventory.getInstance().getPluginConfig().getDatabasePort() + "/" +
                        RPGInventory.getInstance().getPluginConfig().getDatabaseName()
                );

                config.setDriverClassName("org.mariadb.jdbc.Driver");
            }
            default -> throw new IllegalArgumentException(
                    RPGInventory.getInstance().getPluginConfig().getDatabaseMotor()
                            + " must be oen of the following: sqlite, mysql or mariadb."
            );
        }
        config.setUsername(RPGInventory.getInstance().getPluginConfig().getDatabaseUsername());
        config.setPassword(RPGInventory.getInstance().getPluginConfig().getDatabasePassword());
        return config;
    }
}
