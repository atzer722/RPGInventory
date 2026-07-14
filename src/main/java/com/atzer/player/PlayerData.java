package com.atzer.player;

import java.util.UUID;

public record PlayerData(
        UUID uuid,
        String equippedHelmetPermissionId,
        String equippedChestplatePermissionId,
        String equippedLeggingPermissionId,
        String equippedBootsPermissionId
){}
