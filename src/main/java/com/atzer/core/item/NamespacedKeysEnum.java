package com.atzer.core.item;

import com.atzer.RPGInventory;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.NamespacedKey;

@RequiredArgsConstructor
public enum NamespacedKeysEnum {
    TYPE(new NamespacedKey(RPGInventory.getInstance(), "type")),
    ITEM_ID(new NamespacedKey(RPGInventory.getInstance(), "item_id")),
    PERMISSION(new NamespacedKey(RPGInventory.getInstance(), "permission")),
    TIER(new NamespacedKey(RPGInventory.getInstance(), "tier")),

    CONFIG_TYPE(new NamespacedKey(RPGInventory.getInstance(), "config_type"));

    @Getter
    private final NamespacedKey namespacedKey;
}