package com.atzer;

import com.atzer.section.Section;
import com.atzer.section.Step;
import org.apache.commons.lang3.tuple.Pair;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.HumanEntity;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.Nullable;

public final class PluginPlayer {

    //The data format is id_section:id_step
    public static void setStep(HumanEntity player, RPGInventory plugin, Pair<Integer, Integer> step) {
        PersistentDataContainer data = player.getPersistentDataContainer();
        data.set(new NamespacedKey(plugin, "step"), PersistentDataType.STRING, step.getLeft() + ":" + step.getRight());
    }

    @Nullable
    public static Pair<Integer, Integer> getStepFromPersistentDataContainer(HumanEntity player, RPGInventory plugin) {
        PersistentDataContainer data = player.getPersistentDataContainer();
        String stepString = data.get(new NamespacedKey(plugin, "step"), PersistentDataType.STRING);
        return Section.getStep(stepString, plugin);
    }

    @Nullable
    public static Step getStep(RPGInventory plugin, Pair<Integer, Integer> step) {
        Section section = plugin.getSection(step.getLeft());
        if (section == null) return null;

        return section.getStep(step.getRight());
    }
}
