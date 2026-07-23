package com.atzer.player.repositories;

import com.atzer.PluginRepository;
import com.atzer.RPGInventory;
import com.atzer.player.PlayerData;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class PlayerDataRepositorySql extends PluginRepository<PlayerData, UUID> {

    private static Connection getConnection() throws SQLException {
        return RPGInventory.getInstance().getDatabase().getDataSource().getConnection();
    }

    private boolean isSQLite() {
        return RPGInventory.getInstance().getPluginConfig().getDataMotor().equalsIgnoreCase("sqlite");
    }

    @Override
    public void init() {
        String sql = isSQLite()
                ? "CREATE TABLE IF NOT EXISTS player_data (uuid TEXT PRIMARY KEY, zone_id INTEGER NOT NULL);"
                : "CREATE TABLE IF NOT EXISTS player_data (uuid VARCHAR(36) PRIMARY KEY, zone_id INT NOT NULL);";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.execute();
        } catch (SQLException e) {
            RPGInventory.getInstance().getErrorHandler().handleSqlError(sql, e);
        }
    }

    @Override
    public CompletableFuture<PlayerData> save(@NotNull PlayerData obj) {
        CompletableFuture<PlayerData> future = new CompletableFuture<>();

        Bukkit.getScheduler().runTaskAsynchronously(RPGInventory.getInstance(), () -> {
            String sql = isSQLite()
                    ? "INSERT OR REPLACE INTO player_data (uuid, zone_id) VALUES (?, ?);"
                    : "INSERT INTO player_data (uuid, zone_id) VALUES (?, ?) ON DUPLICATE KEY UPDATE zone_id = VALUES(zone_id);";

            try (Connection conn = getConnection();
                 PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, obj.uuid().toString());
                ps.setInt(2, obj.armorZoneId());
                ps.executeUpdate();
                future.complete(obj);
                return;
            } catch (SQLException e) {
                RPGInventory.getInstance().getErrorHandler().handleSqlError(sql, e);
            }
            future.complete(null);
        });
        return future;
    }

    @Override
    public CompletableFuture<Optional<PlayerData>> findById(UUID id) {
        CompletableFuture<Optional<PlayerData>> future = new CompletableFuture<>();

        Bukkit.getScheduler().runTaskAsynchronously(RPGInventory.getInstance(), () -> {
            String sql = "SELECT * FROM player_data WHERE uuid = ?;";

            try (Connection conn = getConnection();
                 PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, id.toString());
                try (ResultSet rs = ps.executeQuery()) {
                    future.complete(Optional.ofNullable(toPlayerData(rs)));
                    return;
                }
            } catch (SQLException e) {
                RPGInventory.getInstance().getErrorHandler().handleSqlError(sql, e);
            }
            future.complete(Optional.empty());
        });

        return future;
    }

    @Override
    public CompletableFuture<Boolean> delete(PlayerData obj) {
        CompletableFuture<Boolean> future = new CompletableFuture<>();

        Bukkit.getScheduler().runTaskAsynchronously(RPGInventory.getInstance(), () -> {
            String sql = "DELETE FROM player_data WHERE uuid = ?;";

            try (Connection conn = getConnection();
                 PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, obj.uuid().toString());
                future.complete(ps.executeUpdate() > 0);
                return;
            } catch (SQLException e) {
                RPGInventory.getInstance().getErrorHandler().handleSqlError(sql, e);
            }
            future.complete(false);
        });
        return future;
    }

    @Override
    public CompletableFuture<PlayerData> update(PlayerData obj) {
        CompletableFuture<PlayerData> future = new CompletableFuture<>();

        Bukkit.getScheduler().runTaskAsynchronously(RPGInventory.getInstance(), () -> {
            String sql = "UPDATE player_data SET zone_id = ? WHERE uuid = ?;";

            try (Connection conn = getConnection();
                 PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, obj.armorZoneId());
                ps.setString(2, obj.uuid().toString());
                ps.executeUpdate();
                future.complete(obj);
                return;
            } catch (SQLException e) {
                RPGInventory.getInstance().getErrorHandler().handleSqlError(sql, e);
            }
            future.complete(null);
        });

        return future;
    }

    private PlayerData toPlayerData(ResultSet rs) throws SQLException {
        if (rs.next()) {
            return new PlayerData(
                    UUID.fromString(rs.getString("uuid")),
                    rs.getInt("zone_id")
            );
        }
        return null;
    }
}
