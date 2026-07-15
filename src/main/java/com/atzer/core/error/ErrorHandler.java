package com.atzer.core.error;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Player;

public class ErrorHandler {

    public void handleNoPlayerDataError(Player player) {
        player.kick(Component
                .text("An error occurred: internal server error. Please contact an administrator! id: RPGInventory null pointer exception.")
                .color(NamedTextColor.RED)
        );
        throw new NullPointerException("An error occurred while trying to get datas of " + player.getName());
    }
}
