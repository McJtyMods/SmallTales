package com.mcjty.smalltales.modules.story.network;

import com.mcjty.smalltales.playerdata.StoryTools;
import com.mcjty.smalltales.playerdata.PlayerStory;
import com.mcjty.smalltales.setup.Messages;
import mcjty.lib.McJtyLib;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.network.PacketDistributor;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class PacketSyncStory {

    private final List<String> discoveredPages = new ArrayList<>();

    public PacketSyncStory(PacketBuffer buf) {
        int size = buf.readInt();
        discoveredPages.clear();
        for (int i = 0 ; i < size ; i++) {
            discoveredPages.add(buf.readUtf(32767));
        }
    }

    public void toBytes(PacketBuffer buf) {
        buf.writeInt(discoveredPages.size());
        discoveredPages.forEach(buf::writeUtf);
    }

    public PacketSyncStory(List<String> discoveredPages) {
        this.discoveredPages.addAll(discoveredPages);
    }

    public boolean handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            McJtyLib.proxy.getClientPlayer().getCapability(StoryTools.PLAYER_STORY).ifPresent(story -> {
                story.reset();
                discoveredPages.forEach(story::addDiscoveredPage);
            });
        });
        return true;
    }

    public static void syncStory(PlayerStory story, PlayerEntity player) {
        Messages.INSTANCE.send(PacketDistributor.PLAYER.with(() -> (ServerPlayerEntity) player),
                new PacketSyncStory(story.getDiscoveredPages()));
    }
}
