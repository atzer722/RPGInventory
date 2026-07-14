package com.atzer.section;

import com.atzer.PluginPlayer;
import com.atzer.RPGInventory;
import dev.lone.itemsadder.api.CustomStack;
import org.apache.commons.lang3.tuple.Pair;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class Step {

    private CustomStack header;
    private CustomStack helmet;
    private CustomStack chestplate;
    private CustomStack leggings;
    private CustomStack boots;

    public Step(@NotNull CustomStack header, @NotNull CustomStack helmet, @NotNull CustomStack chestplate, @NotNull CustomStack leggings, @NotNull CustomStack boots) {
        this.header = header;
        this.helmet = helmet;
        this.chestplate = chestplate;
        this.leggings = leggings;
        this.boots = boots;
    }

    @NotNull
    public CustomStack getHeader() {
        return this.header;
    }

    @NotNull
    public CustomStack getHelmet() {
        return this.helmet;
    }

    @NotNull
    public CustomStack getChestplate() {
        return this.chestplate;
    }

    @NotNull
    public CustomStack getLeggings() {
        return this.leggings;
    }

    @NotNull
    public CustomStack getBoots() {
        return this.boots;
    }

    public void setHeader(@NotNull CustomStack header) {
        this.header = header;
    }

    public void setHelmet(@NotNull CustomStack helmet) {
        this.helmet = helmet;
    }

    public void setChestplate(@NotNull CustomStack chestplate) {
        this.chestplate = chestplate;
    }

    public void setLeggings(@NotNull CustomStack leggings) {
        this.leggings = leggings;
    }

    public void setBoots(@NotNull CustomStack boots) {
        this.boots = boots;
    }

    @Nullable
    public static Step loadStep(int sectionId, int stepId, FileConfiguration config) {
        if (config.contains(sectionId + ".steps." + stepId)) {
            String headerString = config.getString(sectionId + ".steps." + stepId + ".header");
            if (headerString == null) return null;

            String helmetString = config.getString(sectionId + ".steps." + stepId + ".helmet");
            if (helmetString == null) return null;

            String chestplateString = config.getString(sectionId + ".steps." + stepId + ".chestplate");
            if (chestplateString == null) return null;

            String leggingsString = config.getString(sectionId + ".steps." + stepId + ".leggings");
            if (leggingsString == null) return null;

            String bootsString = config.getString(sectionId + ".steps." + stepId + ".boots");
            if (bootsString == null) return null;

            CustomStack header = CustomStack.getInstance(headerString);
            if (header == null) return null;

            CustomStack helmet = CustomStack.getInstance(helmetString);
            if (helmet == null) return null;

            CustomStack chestplate = CustomStack.getInstance(chestplateString);
            if (chestplate == null) return null;

            CustomStack leggings = CustomStack.getInstance(leggingsString);
            if (leggings == null) return null;

            CustomStack boots = CustomStack.getInstance(bootsString);
            if (boots == null) return null;

            return new Step(
                    header,
                    helmet,
                    chestplate,
                    leggings,
                    boots
            );
        }
        return null;
    }

    @Nullable
    public Inventory setStepToInventory(Inventory inv, int pointer, HumanEntity player, RPGInventory plugin) {
        ItemStack locked = plugin.getLockedItemButton().getItemStack();

        inv.setItem(pointer, plugin.toItemStack(this.getHeader(), RPGInventory.CustomStackType.HEADER));

        if (!player.hasPermission("rpginventory." + this.getHelmet().getNamespacedID()))
            inv.setItem(pointer + 9, locked);
        else
            inv.setItem(pointer + 9, plugin.toItemStack(this.getHelmet(), RPGInventory.CustomStackType.HELMET));

        if (!player.hasPermission("rpginventory." + this.getChestplate().getNamespacedID()))
            inv.setItem(pointer + 18, locked);
        else
            inv.setItem(pointer + 18, plugin.toItemStack(this.getChestplate(), RPGInventory.CustomStackType.CHESTPLATE));

        if (!player.hasPermission("rpginventory." + this.getLeggings().getNamespacedID()))
            inv.setItem(pointer + 27, locked);
        else
            inv.setItem(pointer + 27, plugin.toItemStack(this.getLeggings(), RPGInventory.CustomStackType.LEGGINGS));

        if (!player.hasPermission("rpginventory." + this.getBoots().getNamespacedID()))
            inv.setItem(pointer + 36, locked);
        else
            inv.setItem(pointer + 36, plugin.toItemStack(this.getBoots(), RPGInventory.CustomStackType.BOOTS));

        return inv;
    }

    public boolean isFullUnlocked(HumanEntity player) {
        return player.hasPermission("rpginventory." + this.helmet.getNamespacedID()) &&
                player.hasPermission("rpginventory." + this.chestplate.getNamespacedID()) &&
                player.hasPermission("rpginventory." + this.leggings.getNamespacedID()) &&
                player.hasPermission("rpginventory." + this.boots.getNamespacedID());
    }

    public void equip(HumanEntity player, RPGInventory plugin, Section section) {
        player.getInventory().setHelmet(this.helmet.getItemStack());
        player.getInventory().setChestplate(this.chestplate.getItemStack());
        player.getInventory().setLeggings(this.leggings.getItemStack());
        player.getInventory().setBoots(this.boots.getItemStack());
        Pair<Integer, Integer> step = plugin.getSectionIdPair(section, this);

        if (step == null) {
            plugin.getLogger().severe("Step (" + player.getName() + ") is null!");
            return;
        }

        PluginPlayer.setStep(player, plugin, step);
    }
}
