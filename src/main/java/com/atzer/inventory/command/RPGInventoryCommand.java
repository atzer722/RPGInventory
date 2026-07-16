package com.atzer.inventory.command;

import com.atzer.RPGInventory;
import com.atzer.inventory.CustomInventory;
import com.atzer.player.PlayerData;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class RPGInventoryCommand implements TabExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        if (!(sender instanceof Player player)){
            return RPGInventory.getInstance().getErrorHandler().handleMustBeAPlayerToPerformCommandError(sender);
        }

        PlayerData playerData = RPGInventory.getInstance().getPlayerDataManager().getPlayerData(player);

        if (playerData == null) return false;

        CustomInventory.openInventory(player, RPGInventory.getInstance().getArmorZoneRegistry().getZoneById(playerData.armorZoneId()));
        return false;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        return List.of();
    }
}
