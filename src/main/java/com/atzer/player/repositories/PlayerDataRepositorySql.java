package com.atzer.player.repositories;

import com.atzer.PluginRepository;
import com.atzer.RPGInventory;
import com.atzer.player.PlayerData;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.UUID;

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
    public PlayerData save(@NotNull PlayerData obj) {
        String sql = isSQLite()
                ? "INSERT OR REPLACE INTO player_data (uuid, zone_id) VALUES (?, ?);"
                : "INSERT INTO player_data (uuid, zone_id) VALUES (?, ?) ON DUPLICATE KEY UPDATE zone_id = VALUES(zone_id);";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, obj.uuid().toString());
            ps.setInt(2, obj.armorZoneId());
            ps.executeUpdate();
            return obj;
        } catch (SQLException e) {
            RPGInventory.getInstance().getErrorHandler().handleSqlError(sql, e);
        }
        return null;
    }

    @Override
    public Optional<PlayerData> findById(UUID id) {
        String sql = "SELECT * FROM player_data WHERE uuid = ?;";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, id.toString());
            try (ResultSet rs = ps.executeQuery()) {
                return Optional.ofNullable(toPlayerData(rs));
            }
        } catch (SQLException e) {
            RPGInventory.getInstance().getErrorHandler().handleSqlError(sql, e);
        }
        return Optional.empty();
    }

    @Override
    public boolean delete(PlayerData obj) {
        String sql = "DELETE FROM player_data WHERE uuid = ?;";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, obj.uuid().toString());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            RPGInventory.getInstance().getErrorHandler().handleSqlError(sql, e);
        }
        return false;
    }

    @Override
    public PlayerData update(PlayerData obj) {
        String sql = "UPDATE player_data SET zone_id = ? WHERE uuid = ?;";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, obj.armorZoneId());
            ps.setString(2, obj.uuid().toString());
            ps.executeUpdate();
            return obj;
        } catch (SQLException e) {
            RPGInventory.getInstance().getErrorHandler().handleSqlError(sql, e);
        }
        return null;
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
