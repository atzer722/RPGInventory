package com.atzer.core.error;

import com.atzer.RPGInventory;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public final class ErrorHandler {

    public void handleNoPlayerDataError(Player player) {
        player.kick(Component
                .text("An error occurred: internal server error. Please contact an administrator! id: RPGInventory null pointer exception.")
                .color(NamedTextColor.RED)
        );
        throw new NullPointerException("An error occurred while trying to get datas of " + player.getName());
    }

    public boolean handleMustBeAPlayerToPerformCommandError(CommandSender sender) {
        sender.sendMessage(Component.text("You must be a player to perform this command!").color(NamedTextColor.RED));
        return false;
    }

    public void handleWrongDataMotorEntry(String motor) {
        RPGInventory.getInstance().getLogger().severe("A wrong data motor is set. " + motor + " is not supported at all.");
        RPGInventory.getInstance().getLogger().severe("Supported motors: MySQL, MariaDB, SQLite, YAML");
        throw new IllegalArgumentException("A wrong data motor is set. " + motor);
    }

    public void handleSqlError(String sql) {
        RPGInventory.getInstance().getLogger().severe("PLEASE CONTACT A DEVELOPER!!!!!!!");
        throw new RuntimeException("PLEASE CONTACT A DEVELOPER!!!!!!!");
    }
}
