package com.atzer.player.listener;

import com.atzer.RPGInventory;
import com.atzer.core.item.NamespacedKeysEnum;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;

public class PlayerDeathListener implements Listener {

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {

        // Remove the armor from drop.
        for (int i = 0; i < event.getDrops().size(); i++) {
            ItemStack item = event.getDrops().get(i);
            if (RPGInventory.getInstance().getItemStackUtils().getPersistentDataString(item, NamespacedKeysEnum.ITEM_ID.getNamespacedKey()) != null) {
                event.getDrops().remove(i);
                i--;
            }
        }
    }
}
