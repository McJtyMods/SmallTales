package com.mcjty.smalltales.modules.story.network;

import com.mcjty.smalltales.playerdata.StoryTools;
import com.mcjty.smalltales.playerdata.PlayerStoryProgress;
import com.mcjty.smalltales.setup.Messages;
import mcjty.lib.McJtyLib;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.network.PacketDistributor;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Supplier;

public class PacketSyncStoryProgress {

    private final List<String> discoveredPages = new ArrayList<>();
    private final Set<String> read = new HashSet<>();

    public PacketSyncStoryProgress(PacketBuffer buf) {
        int size = buf.readInt();
        discoveredPages.clear();
        for (int i = 0 ; i < size ; i++) {
            discoveredPages.add(buf.readUtf(32767));
        }
        size = buf.readInt();
        read.clear();
        for (int i = 0 ; i < size ; i++) {
            read.add(buf.readUtf(32767));
        }
    }

    public void toBytes(PacketBuffer buf) {
        buf.writeInt(discoveredPages.size());
        discoveredPages.forEach(buf::writeUtf);
        buf.writeInt(read.size());
        read.forEach(buf::writeUtf);
    }

    public PacketSyncStoryProgress(List<String> discoveredPages, Set<String> read) {
        this.discoveredPages.addAll(discoveredPages);
        this.read.addAll(read);
    }

    public boolean handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            McJtyLib.proxy.getClientPlayer().getCapability(StoryTools.PLAYER_STORY).ifPresent(story -> {
                story.reset();
                discoveredPages.forEach(story::addDiscovered);
                read.forEach(story::addRead);
            });
        });
        return true;
    }

    public static void syncStoryProgress(PlayerStoryProgress story, PlayerEntity player) {
        Messages.INSTANCE.send(PacketDistributor.PLAYER.with(() -> (ServerPlayerEntity) player),
                new PacketSyncStoryProgress(story.getDiscovered(), story.getRead()));
    }
}
