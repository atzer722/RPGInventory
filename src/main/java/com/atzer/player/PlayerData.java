package com.atzer.player;

import java.util.UUID;

public record PlayerData(
        UUID uuid,
        int armorZoneId
){}
