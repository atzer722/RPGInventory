package com.atzer.reload.command;

import com.atzer.RPGInventory;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;

public class ReloadCommand implements TabExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        if (args.length == 1 && args[0].equalsIgnoreCase("update")) {
            Map<String, Object> map =  RPGInventory.getInstance().getPluginConfig().updateConfig();

            if (map.isEmpty()) {
                sender.sendMessage(Component.text("No parameters was updated!").color(NamedTextColor.GOLD));
            } else {
                Component msg = Component.text("The added parameters are the following:").color(NamedTextColor.DARK_GREEN);
                for (Map.Entry<String, Object> entry : map.entrySet()) {
                    msg = msg.appendNewline().append(Component.text("   - " +  entry.getKey() + ": " + entry.getValue())).color(NamedTextColor.GREEN);
                }

                sender.sendMessage(msg);
            }
        }

        RPGInventory.getInstance().reloadConfig();
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        return List.of();
    }
}
