package com.atzer;

import com.atzer.command.OpenCommand;
import com.atzer.command.ReloadCommand;
import com.atzer.listener.*;
import com.atzer.menu.MenuHolder;
import com.atzer.section.Section;
import com.atzer.section.Step;
import com.atzer.stack.*;
import dev.lone.itemsadder.api.CustomStack;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.tuple.Pair;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.Objects;

@Getter
public class RPGInventory extends JavaPlugin {

    @Getter
    private static RPGInventory instance;

    @Setter
    private Section section;
    private YamlConfiguration sectionConfiguration;
    private Config config;

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();

        this.config = new Config();

        FileManager.createResourceFile(new File("sections.yml"), this);

        if (!this.config.getPLuginEnable()) {
            this.getLogger().warning("Plugin RPGInventory is disabled in config.yml! The plugin will not be loaded anymore!");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }

        this.sectionConfiguration = YamlConfiguration.loadConfiguration(new File(this.getDataFolder(), "sections.yml"));

        this.section = Section.loadSection(this.sectionConfiguration, this.getLogger());

        Objects.requireNonNull(this.getCommand("reload")).setExecutor(new ReloadCommand(this));
        Objects.requireNonNull(this.getCommand("open")).setExecutor(new OpenCommand(this));
        this.getServer().getPluginManager().registerEvents(new InventoryClickEvent(this), this);
        this.getServer().getPluginManager().registerEvents(new OnPlayerJoinEvent(this), this);
        this.getServer().getPluginManager().registerEvents(new InteractPlayerEvent(), this);
        this.getServer().getPluginManager().registerEvents(new OnPlayerDieEvent(), this);
        this.getServer().getPluginManager().registerEvents(new PlayerSpawnEvent(this), this);
        this.getServer().getPluginManager().registerEvents(new PlayerJoinEvent(this), this);

        this.getLogger().info("Plugin RPGInventory enabled!");
    }

    @Override
    public void onDisable() {
        this.getLogger().info("Plugin RPGInventory disabled!");
    }

    public CustomStack getMenuButton() {
        return CustomStack.getInstance(this.config.getMenuButton());
    }

    public CustomStack getLeftArrowButton() {
        return CustomStack.getInstance(this.config.getLeftArrowButton());
    }

    public CustomStack getEquipButton() {
        return CustomStack.getInstance(this.config.getEquipButton());
    }

    public CustomStack getRightArrowButton() {
        return CustomStack.getInstance(this.config.getRightArrowButton());
    }

    public CustomStack getUnlockedItemButton() {
        return CustomStack.getInstance(this.config.getUnlockedItemButton());
    }

    public CustomStack getLockedItemButton() {
        return CustomStack.getInstance(this.config.getLOckedItemButton());
    }

    @Nullable
    public Step getBestUnlockedStep(Player player) {
        return this.getBestUnlockedStep(player, this.getBestUnlockedStepPair(player));
    }

    @Nullable
    public Step getBestUnlockedStep(Player player, Pair<Integer, Integer> sectionIdPair) {
        if (sectionIdPair == null) return null;

        Section section = this.getSection(sectionIdPair.getLeft());

        if (section == null) return null;

        return section.getStep(sectionIdPair.getRight());
    }

    @Nullable
    public Pair<Integer, Integer> getBestUnlockedStepPair(Player player) {
        Section section1 = this.section.cloneSection();

        while (Objects.equals(section1.getBestUnlocked(player), section1.getStep6())) {
            Section nextSection = section1.getNextSection();
            if (nextSection == null) break;

            if (nextSection.getBestUnlocked(player) == null) break;

            section1 = nextSection;
        }

        return Pair.of(section1.getId(), section1.getBestUnlockedId(player));
    }

    @Nullable
    public Pair<Integer, Integer> getSectionIdPair(Section section, Step step) {
        int stepId = section.getStepId(step);
        if (stepId == 0) return null;
        return Pair.of(section.getId(), stepId);
    }

    @Nullable
    public Section getSection(int id) {
        Section section1 = section.cloneSection();
        while (section1.getId() != id) {
            if (section1.getNextSection() == null) return null;
            section1 = section1.getNextSection();
        }
        return section1;
    }

    public void reload() {
        this.reloadConfig();
        this.sectionConfiguration = YamlConfiguration.loadConfiguration(new File(this.getDataFolder(), "sections.yml"));
        this.setSection(Section.loadSection(this.sectionConfiguration, this.getLogger()));
        this.getLogger().info("Plugin RPGInventory reloaded!");
    }

    public void openInventory(HumanEntity player) {
        this.openInventory(player, this.section, this.section.getBestUnlocked(player));
    }

    public void openInventory(@NotNull HumanEntity player, Section section, Step pointedStep) {
        MenuHolder menu = new MenuHolder(this, section, player, pointedStep);

        if (pointedStep == null) this.getLogger().warning("Step is null!");

        player.openInventory(menu.getInventory());
    }

    @Nullable
    public Section getLeftSection(Section section) {
        Section prec = null;
        Section curr = this.section;
        if (this.section.getId() == section.getId()) return null;

        while(curr.getId() != section.getId()) {
            if (curr.getNextSection() == null) return null;
            prec = curr;
            curr = curr.getNextSection();
        }
        return prec;
    }

    public ItemStack toItemStack(CustomStack stack, CustomStackType type) {
        ItemStack item = stack.getItemStack();
        item.editPersistentDataContainer(pdc -> {
           pdc.set(new NamespacedKey(this, "type"), PersistentDataType.STRING, type.name());
           pdc.set(new NamespacedKey(this, "customstack"), PersistentDataType.STRING, stack.getNamespacedID());
        });
        return item;
    }

    public Pair<CustomStack, String> toCustomStack(ItemStack item) {
        String data = item.getPersistentDataContainer().get(new NamespacedKey(this, "type"), PersistentDataType.STRING);
        String id = item.getPersistentDataContainer().get(new NamespacedKey(this, "customstack"), PersistentDataType.STRING);

        if (data == null || id == null) return null;

        return Pair.of(CustomStack.getInstance(id), data);
    }

    public void equipBestUnlockedStep(Player player) {
        this.equipStep(player, this.getBestUnlockedStepPair(player));
    }

    public void equipStep(HumanEntity player, @Nullable Pair<Integer, Integer> stepPair) {
        if (stepPair == null) return;

        Section section = this.getSection(stepPair.getLeft());
        if (section == null) return;

        Step step = section.getStep(stepPair.getRight());
        if (step == null) return;

        step.equip(player, this, section);
    }

    public enum CustomStackType {
        MENU(MenuStack.class),
        LEFT_ARROW(LeftArrowStack.class),
        RIGHT_ARROW(RightArrowStack.class),
        EQUIP(EquipStack.class),
        HEADER(HeaderStack.class),
        HELMET(HelmetStack.class),
        CHESTPLATE(ChestplateStack.class),
        LEGGINGS(LeggingsStack.class),
        BOOTS(BootsStack.class);

        private static final RPGInventory plugin = RPGInventory.getInstance();
        private final Class<? extends PluginStack> stack;

        CustomStackType(Class<? extends PluginStack> stack) {
            this.stack = stack;
        }

        public Class<? extends PluginStack> getStack() {
            return this.stack;
        }

        public void interact(MenuHolder holder, HumanEntity player, RPGInventory plugin, CustomStack clickedItem)
                throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {

            this.stack.getConstructor(MenuHolder.class, HumanEntity.class, RPGInventory.class, CustomStack.class)
                    .newInstance(holder, player, plugin, clickedItem).interact();
        }
    }
}

