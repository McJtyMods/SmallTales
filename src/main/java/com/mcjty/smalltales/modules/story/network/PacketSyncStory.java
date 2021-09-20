package com.mcjty.smalltales.modules.story.network;

import com.mcjty.smalltales.playerdata.PlayerStoryProgress;
import com.mcjty.smalltales.playerdata.StoryTools;
import mcjty.lib.McJtyLib;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class PacketSyncStory {

    // @todo

    public PacketSyncStory(PacketBuffer buf) {
    }

    public void toBytes(PacketBuffer buf) {
    }

    public PacketSyncStory() {
    }

    public boolean handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            McJtyLib.proxy.getClientPlayer().getCapability(StoryTools.PLAYER_STORY).ifPresent(story -> {
            });
        });
        return true;
    }

    public static void syncStory(PlayerStoryProgress story, PlayerEntity player) {
//        Messages.INSTANCE.send(PacketDistributor.PLAYER.with(() -> (ServerPlayerEntity) player),
//                new PacketSyncStory(story.getDiscovered(), story.getRead()));
    }
}
