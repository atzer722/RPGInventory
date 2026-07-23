package com.atzer.core;

import com.atzer.RPGInventory;
import io.lumine.mythic.lib.sql.hikari.HikariConfig;
import io.lumine.mythic.lib.sql.hikari.HikariDataSource;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class Database {

    @Getter
    private HikariDataSource dataSource;

    public void init() {
        String motor = RPGInventory.getInstance().getPluginConfig().getDataMotor().toLowerCase();

        if (!motor.equals("mysql") && !motor.equals("mariadb") && !motor.equals("yaml") && !motor.equals("sqlite")) {
            RPGInventory.getInstance().getErrorHandler().handleWrongDataMotorEntry(motor);
            return;
        }

        String host = RPGInventory.getInstance().getPluginConfig().getDataHost().toLowerCase();
        int port = RPGInventory.getInstance().getPluginConfig().getDataPort();
        String databaseName = RPGInventory.getInstance().getPluginConfig().getDataName();
        String username = RPGInventory.getInstance().getPluginConfig().getDataUsername();
        String password = RPGInventory.getInstance().getPluginConfig().getDataPassword();

        if (motor.equals("yaml")) {
            dataSource = null;

            RPGInventory.getInstance().getLogger().info("Data storage set to YAML!");

            return;
        }

        HikariConfig hikariConfig = new HikariConfig();

        if (motor.equals("mysql") || motor.equals("mariadb")) {
            if (motor.equals("mysql")) {
                hikariConfig.setDriverClassName("com.mysql.cj.jdbc.Driver");
                RPGInventory.getInstance().getLogger().info("Setting data storage to Mysql...");
            } else {
                hikariConfig.setDriverClassName("org.mariadb.jdbc.Driver");
                RPGInventory.getInstance().getLogger().info("Setting data storage to MariaDB...");
            }

            hikariConfig.setJdbcUrl("jdbc:" + motor + "://" + host + ":" + port + "/" + databaseName + "?useSSL=false&allowPublicKeyRetrieval=true");
            hikariConfig.setUsername(username);
            hikariConfig.setPassword(password);

            hikariConfig.setMaximumPoolSize(10);
            hikariConfig.setMinimumIdle(2);
            hikariConfig.setConnectionTimeout(30_000);
            hikariConfig.setIdleTimeout(600_000);
            hikariConfig.setMaxLifetime(1_800_000);

            hikariConfig.addDataSourceProperty("cachePrepStmts", "true");
            hikariConfig.addDataSourceProperty("prepStmtCacheSize", "250");
            hikariConfig.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");

            hikariConfig.addDataSourceProperty("useServerPrepStmts", "true");
            hikariConfig.addDataSourceProperty("rewriteBatchedStatements", "true");

            RPGInventory.getInstance().getLogger().info("Data storage set!");
        }

        if (motor.equals("sqlite")) {
            RPGInventory.getInstance().getLogger().info("Setting data storage to SQLite...");
            hikariConfig.setDriverClassName("org.sqlite.JDBC");
            hikariConfig.setJdbcUrl("jdbc:sqlite:" + RPGInventory.getInstance().getDataFolder() + "/data.db");
            hikariConfig.setMaximumPoolSize(1);
            hikariConfig.setMinimumIdle(1);
            hikariConfig.setConnectionTimeout(30_000);
            hikariConfig.addDataSourceProperty("journal_mode", "WAL");
            hikariConfig.addDataSourceProperty("synchronous", "NORMAL");
            RPGInventory.getInstance().getLogger().info("Data storage set!");
        }

        this.dataSource = new HikariDataSource(hikariConfig);
    }

    public void close() {
        if (this.dataSource != null) {
            this.dataSource.close();
        }
        this.dataSource = null;
    }
}
