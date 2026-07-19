package com.atzer.inventory.listener;

import com.atzer.RPGInventory;
import com.atzer.armor.ArmorZone;
import com.atzer.core.config.Config;
import com.atzer.core.item.NamespacedKeysEnum;
import com.atzer.inventory.CustomInventory;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;

public final class InventoryClickListener implements Listener {

    @EventHandler
    public void onInventoryClickEvent(InventoryClickEvent event) {
        if (event.getClickedInventory() == null) return;
        if (!(event.getWhoClicked() instanceof Player player)) return;
        if (!(event.getClickedInventory().getHolder() instanceof CustomInventory customInventory)) {
            handlePlayerInventoryInteraction(event);
            return;
        }
        event.setCancelled(true);

        ItemStack item = event.getCurrentItem();

        if (item == null) return;

        Config.InteractionType interactionType = Config.InteractionType.getMatch(
                RPGInventory.getInstance().getItemStackUtils().getPersistentDataString(item, NamespacedKeysEnum.CONFIG_TYPE.getNamespacedKey())
        );

        if (interactionType == null) return;

        switch (interactionType) {
            case MENU_BACK_BUTTON -> this.handleMenuBackButtonInteraction(player);
            case MENU_LEFT_ARROW_BUTTON -> this.handleMenuLeftArrowButtonInteraction(player, customInventory.getArmorZone());
            case MENU_NOT_EQUIPPED_BUTTON -> this.handleMenuNotEquippedButtonInteraction(player, customInventory.getArmorZone());
            case MENU_EQUIPPED_BUTTON -> this.handleMenuEquippedButtonInteraction();
            case MENU_RIGHT_ARROW_BUTTON -> this.handleMenuRightArrowButtonInteraction(player, customInventory.getArmorZone());
            case MENU_HIDDEN_ARMOR_BUTTON -> this.handleMenuHiddenArmorButtonInteraction();
        }
    }

    private void handleMenuBackButtonInteraction(Player player) {
        if (RPGInventory.getInstance().getPluginConfig().getMenuServerExecution()) {
            RPGInventory.getInstance().getServer().dispatchCommand(
                    RPGInventory.getInstance().getServer().getConsoleSender(),
                    RPGInventory.getInstance().getPluginConfig().getMenuCommand().replace("{player}", player.getName())
            );
            return;
        }

        player.performCommand(RPGInventory.getInstance().getPluginConfig().getMenuCommand());
    }

    private void handleMenuLeftArrowButtonInteraction(Player player, ArmorZone zone) {
        if (zone.num() <= 1) return;
        player.closeInventory();
        CustomInventory.openInventory(player, RPGInventory.getInstance().getArmorZoneRegistry().getZoneById(zone.num()-1));
    }

    private void handleMenuNotEquippedButtonInteraction(Player player, ArmorZone zone) {
        RPGInventory.getInstance().getPlayerDataManager().onZoneSelected(player, zone.num());
        player.closeInventory();
        CustomInventory.openInventory(player, zone);
    }

    private void handleMenuEquippedButtonInteraction() {}

    private void handleMenuRightArrowButtonInteraction(Player player, ArmorZone zone) {
        if (zone.num() >= RPGInventory.getInstance().getArmorZoneRegistry().getMaxZoneId()) return;
        player.closeInventory();
        CustomInventory.openInventory(player, RPGInventory.getInstance().getArmorZoneRegistry().getZoneById(zone.num()+1));
    }

    private void handleMenuHiddenArmorButtonInteraction() {}

    private void handlePlayerInventoryInteraction(InventoryClickEvent event) {
        if (event.getSlotType() == InventoryType.SlotType.ARMOR) {
            event.setCancelled(true);
        }
    }
}
