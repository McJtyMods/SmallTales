package com.mcjty.smalltales.setup;

import com.mcjty.smalltales.SmallTales;
import com.mcjty.smalltales.modules.story.network.PackedMarkRead;
import com.mcjty.smalltales.modules.story.network.PacketSyncStory;
import com.mcjty.smalltales.modules.story.network.PacketUpdateKnowledge;
import mcjty.lib.network.PacketHandler;
import mcjty.lib.network.PacketSendClientCommand;
import mcjty.lib.network.PacketSendServerCommand;
import mcjty.lib.typed.TypedMap;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

import javax.annotation.Nonnull;

public class Messages {
    public static SimpleChannel INSTANCE;

    private static int packetId = 0;
    private static int id() {
        return packetId++;
    }

    public static void registerMessages(String name) {
        SimpleChannel net = NetworkRegistry.ChannelBuilder
                .named(new ResourceLocation(SmallTales.MODID, name))
                .networkProtocolVersion(() -> "1.0")
                .clientAcceptedVersions(s -> true)
                .serverAcceptedVersions(s -> true)
                .simpleChannel();

        INSTANCE = net;

        net.messageBuilder(PacketSyncStory.class, id())
                .encoder(PacketSyncStory::toBytes)
                .decoder(PacketSyncStory::new)
                .consumer(PacketSyncStory::handle)
                .add();
        net.messageBuilder(PacketUpdateKnowledge.class, id())
                .encoder(PacketUpdateKnowledge::toBytes)
                .decoder(PacketUpdateKnowledge::new)
                .consumer(PacketUpdateKnowledge::handle)
                .add();
        net.messageBuilder(PackedMarkRead.class, id())
                .encoder(PackedMarkRead::toBytes)
                .decoder(PackedMarkRead::new)
                .consumer(PackedMarkRead::handle)
                .add();

        PacketHandler.registerStandardMessages(id(), net);
    }

    public static void sendToServer(String command, @Nonnull TypedMap.Builder argumentBuilder) {
        INSTANCE.sendToServer(new PacketSendServerCommand(SmallTales.MODID, command, argumentBuilder.build()));
    }

    public static void sendToServer(String command) {
        INSTANCE.sendToServer(new PacketSendServerCommand(SmallTales.MODID, command, TypedMap.EMPTY));
    }

    public static void sendToClient(PlayerEntity player, String command, @Nonnull TypedMap.Builder argumentBuilder) {
        INSTANCE.sendTo(new PacketSendClientCommand(SmallTales.MODID, command, argumentBuilder.build()), ((ServerPlayerEntity) player).connection.connection, NetworkDirection.PLAY_TO_CLIENT);
    }

    public static void sendToClient(PlayerEntity player, String command) {
        INSTANCE.sendTo(new PacketSendClientCommand(SmallTales.MODID, command, TypedMap.EMPTY), ((ServerPlayerEntity) player).connection.connection, NetworkDirection.PLAY_TO_CLIENT);
    }
}
