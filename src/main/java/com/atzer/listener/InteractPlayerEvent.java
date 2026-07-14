package com.atzer.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

public class InteractPlayerEvent implements Listener {

    @EventHandler
    public void onInteractPlayer(org.bukkit.event.player.PlayerInteractEvent event) {
        if (event.getItem() != null && isArmor(event.getItem()) && event.getAction().isRightClick()) event.setCancelled(true);
    }

    public static boolean isArmor(ItemStack item) {
        return item.getType().getEquipmentSlot().isArmor();
    }

}
