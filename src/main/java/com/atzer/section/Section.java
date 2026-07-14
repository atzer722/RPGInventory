package com.atzer.section;

import com.atzer.RPGInventory;
import com.atzer.menu.MenuHolder;
import dev.lone.itemsadder.api.CustomStack;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.apache.commons.lang3.tuple.Pair;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.logging.Logger;

public class Section {

    private String title;
    private int id;
    private Step step1;
    private Step step2;
    private Step step3;
    private Step step4;
    private Step step5;
    private Step step6;
    private Section nextSection;

    public Section(@NotNull String title, int id, @Nullable Step step1, @Nullable Step step2, @Nullable Step step3, @Nullable Step step4, @Nullable Step step5, @Nullable Step step6, @Nullable Section nextSection) {
        this.title = title;
        this.id = id;
        this.step1 = step1;
        this.step2 = step2;
        this.step3 = step3;
        this.step4 = step4;
        this.step5 = step5;
        this.step6 = step6;
        this.nextSection = nextSection;
    }

    @NotNull
    public String getTitle() {
        return this.title;
    }

    public int getId() {
        return this.id;
    }

    @Nullable
    public Step getStep1() {
        return this.step1;
    }

    @Nullable
    public Step getStep2() {
        return this.step2;
    }

    @Nullable
    public Step getStep3() {
        return this.step3;
    }

    @Nullable
    public Step getStep4() {
        return this.step4;
    }

    @Nullable
    public Step getStep5() {
        return this.step5;
    }

    @Nullable
    public Step getStep6() {
        return this.step6;
    }

    @Nullable
    public Section getNextSection() {
        return this.nextSection;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTitle(@NotNull String title) {
        this.title = title;
    }

    public void setStep1(@Nullable Step step1) {
        this.step1 = step1;
    }

    public void setStep2(@Nullable Step step2) {
        this.step2 = step2;
    }

    public void setStep3(@Nullable Step step3) {
        this.step3 = step3;
    }

    public void setStep4(@Nullable Step step4) {
        this.step4 = step4;
    }

    public void setStep5(@Nullable Step step5) {
        this.step5 = step5;
    }

    public void setStep6(@Nullable Step step6) {
        this.step6 = step6;
    }

    public void setNextSection(@Nullable Section nextSection) {
        this.nextSection = nextSection;
    }

    public static Section loadSection(YamlConfiguration config, Logger LOGGER) {
        return loadSection(1, config, LOGGER);
    }

    private static Section loadSection(int id, FileConfiguration config, Logger LOGGER) {
        if (!config.contains(id + ".title")) return null;

        LOGGER.info("Loading section " + id);

        return new Section(
                Objects.requireNonNull(config.getString(id + ".title")),
                id,
                Step.loadStep(id, 1, config),
                Step.loadStep(id, 2, config),
                Step.loadStep(id, 3, config),
                Step.loadStep(id, 4, config),
                Step.loadStep(id, 5, config),
                Step.loadStep(id, 6, config),
                loadSection(id + 1, config, LOGGER)
        );
    }

    @Nullable
    public Step getBestUnlocked(HumanEntity player) {
        return this.getStep(this.getBestUnlockedId(player));
    }

    public int getBestUnlockedId(HumanEntity player) {
        if (!this.step1.isFullUnlocked(player)) return 0;
        if (!this.step2.isFullUnlocked(player)) return 1;
        if (!this.step3.isFullUnlocked(player)) return 2;
        if (!this.step4.isFullUnlocked(player)) return 3;
        if (!this.step5.isFullUnlocked(player)) return 4;
        if (!this.step6.isFullUnlocked(player)) return 5;
        return 6;
    }

    @NotNull
    public Section cloneSection() {
        return new Section(
                this.title,
                this.id,
                this.step1,
                this.step2,
                this.step3,
                this.step4,
                this.step5,
                this.step6,
                this.nextSection == null ? null : this.nextSection.cloneSection()
        );
    }

    @Nullable
    public Step getStep(int step) {
        return switch (step) {
            case 1 -> this.step1;
            case 2 -> this.step2;
            case 3 -> this.step3;
            case 4 -> this.step4;
            case 5 -> this.step5;
            case 6 -> this.step6;
            default -> null;
        };
    }

    @Nullable
    public static Pair<Integer, Integer> getStep(String stepString, RPGInventory plugin) {
        if (stepString == null) return null;

        String[] split = stepString.split(":");

        return Pair.of(Integer.parseInt(split[0]), Integer.parseInt(split[1]));
    }

    @Nullable
    public Inventory toInventory(HumanEntity player, RPGInventory plugin, MenuHolder holder) {
        Inventory inv = plugin.getServer().createInventory(holder, 54, MiniMessage.miniMessage().deserialize(this.getTitle()));

        inv.setItem(8, plugin.toItemStack(plugin.getMenuButton(), RPGInventory.CustomStackType.MENU));
        inv.setItem(9, plugin.toItemStack(plugin.getLeftArrowButton(), RPGInventory.CustomStackType.LEFT_ARROW));
        inv.setItem(10, plugin.toItemStack(plugin.getEquipButton(), RPGInventory.CustomStackType.EQUIP));
        inv.setItem(11, plugin.toItemStack(plugin.getRightArrowButton(), RPGInventory.CustomStackType.RIGHT_ARROW));

        if (this.step1 != null) {
            inv = this.step1.setStepToInventory(inv, 12, player, plugin);

            if (inv == null) return null;
        }

        if (this.step2 != null) {
            inv = this.step2.setStepToInventory(inv, 13, player, plugin);
            if (inv == null) return null;
        }

        if (this.step3 != null) {
            inv = this.step3.setStepToInventory(inv, 14, player, plugin);

            if (inv == null) return null;
        }

        if (this.step4 != null) {
            inv = this.step4.setStepToInventory(inv, 15, player, plugin);

            if (inv == null) return null;
        }

        if (this.step5 != null) {
            inv = this.step5.setStepToInventory(inv, 16, player, plugin);

            if (inv == null) return null;
        }

        if (this.step6 != null) {
            inv = this.step6.setStepToInventory(inv, 17, player, plugin);

            if (inv == null) return null;
        }

        Step pointer = holder.getPointedStep();

        if (pointer != null) {
            inv.setItem(19, plugin.toItemStack(pointer.getHelmet(), RPGInventory.CustomStackType.HELMET));
            inv.setItem(28, plugin.toItemStack(pointer.getChestplate(), RPGInventory.CustomStackType.CHESTPLATE));
            inv.setItem(37, plugin.toItemStack(pointer.getLeggings(), RPGInventory.CustomStackType.LEGGINGS));
            inv.setItem(46, plugin.toItemStack(pointer.getBoots(), RPGInventory.CustomStackType.BOOTS));
        }
        return inv;
    }

    @Nullable
    public Step getFromHeader(CustomStack header) {
        if (this.step1.getHeader().getNamespacedID().equals(header.getNamespacedID())) return this.step1;
        if (this.step2.getHeader().getNamespacedID().equals(header.getNamespacedID())) return this.step2;
        if (this.step3.getHeader().getNamespacedID().equals(header.getNamespacedID())) return this.step3;
        if (this.step4.getHeader().getNamespacedID().equals(header.getNamespacedID())) return this.step4;
        if (this.step5.getHeader().getNamespacedID().equals(header.getNamespacedID())) return this.step5;
        if (this.step6.getHeader().getNamespacedID().equals(header.getNamespacedID())) return this.step6;
        return null;
    }

    @Nullable
    public Step getFromHelmet(CustomStack helmet) {
        if (this.step1.getHelmet().getNamespacedID().equals(helmet.getNamespacedID())) return this.step1;
        if (this.step2.getHelmet().getNamespacedID().equals(helmet.getNamespacedID())) return this.step2;
        if (this.step3.getHelmet().getNamespacedID().equals(helmet.getNamespacedID())) return this.step3;
        if (this.step4.getHelmet().getNamespacedID().equals(helmet.getNamespacedID())) return this.step4;
        if (this.step5.getHelmet().getNamespacedID().equals(helmet.getNamespacedID())) return this.step5;
        if (this.step6.getHelmet().getNamespacedID().equals(helmet.getNamespacedID())) return this.step6;
        return null;
    }

    @Nullable
    public Step getFromChestplate(CustomStack chestplate) {
        if (this.step1.getChestplate().getNamespacedID().equals(chestplate.getNamespacedID())) return this.step1;
        if (this.step2.getChestplate().getNamespacedID().equals(chestplate.getNamespacedID())) return this.step2;
        if (this.step3.getChestplate().getNamespacedID().equals(chestplate.getNamespacedID())) return this.step3;
        if (this.step4.getChestplate().getNamespacedID().equals(chestplate.getNamespacedID())) return this.step4;
        if (this.step5.getChestplate().getNamespacedID().equals(chestplate.getNamespacedID())) return this.step5;
        if (this.step6.getChestplate().getNamespacedID().equals(chestplate.getNamespacedID())) return this.step6;
        return null;
    }

    @Nullable
    public Step getFromLeggings(CustomStack leggings) {
        if (this.step1.getLeggings().getNamespacedID().equals(leggings.getNamespacedID())) return this.step1;
        if (this.step2.getLeggings().getNamespacedID().equals(leggings.getNamespacedID())) return this.step2;
        if (this.step3.getLeggings().getNamespacedID().equals(leggings.getNamespacedID())) return this.step3;
        if (this.step4.getLeggings().getNamespacedID().equals(leggings.getNamespacedID())) return this.step4;
        if (this.step5.getLeggings().getNamespacedID().equals(leggings.getNamespacedID())) return this.step5;
        if (this.step6.getLeggings().getNamespacedID().equals(leggings.getNamespacedID())) return this.step6;
        return null;
    }

    @Nullable
    public Step getFromBoots(CustomStack boots) {
        if (this.step1.getBoots().getNamespacedID().equals(boots.getNamespacedID())) return this.step1;
        if (this.step2.getBoots().getNamespacedID().equals(boots.getNamespacedID())) return this.step2;
        if (this.step3.getBoots().getNamespacedID().equals(boots.getNamespacedID())) return this.step3;
        if (this.step4.getBoots().getNamespacedID().equals(boots.getNamespacedID())) return this.step4;
        if (this.step5.getBoots().getNamespacedID().equals(boots.getNamespacedID())) return this.step5;
        if (this.step6.getBoots().getNamespacedID().equals(boots.getNamespacedID())) return this.step6;
        return null;
    }

    public int getStepId(Step step) {
        if (step.equals(this.step1)) return 1;
        if (step.equals(this.step2)) return 2;
        if (step.equals(this.step3)) return 3;
        if (step.equals(this.step4)) return 4;
        if (step.equals(this.step5)) return 5;
        if (step.equals(this.step6)) return 6;
        return 0;
    }
}
