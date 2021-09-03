package com.mcjty.smalltales.playerdata;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;

public class PlayerProperties {

    @CapabilityInject(PlayerStory.class)
    public static Capability<PlayerStory> PLAYER_STORY;
}
