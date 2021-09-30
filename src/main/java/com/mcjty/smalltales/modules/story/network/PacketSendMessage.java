package com.mcjty.smalltales.modules.story.network;

import com.mcjty.smalltales.modules.story.data.Story;
import com.mcjty.smalltales.setup.Messages;
import mcjty.lib.McJtyLib;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.Util;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.network.PacketDistributor;

import java.util.Map;
import java.util.function.Supplier;

public class PacketSendMessage {

    private final String message;

    public PacketSendMessage(PacketBuffer buf) {
        message = buf.readUtf(32767);
    }

    public void toBytes(PacketBuffer buf) {
        buf.writeUtf(message);
    }

    public PacketSendMessage(String message) {
        this.message = message;
    }

    public boolean handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            Story story = Story.getStory(McJtyLib.proxy.getClientWorld());
            if (story != null) {
                Map<String, ITextComponent> messages = story.getMessages();
                McJtyLib.proxy.getClientPlayer().sendMessage(messages.get(message), Util.NIL_UUID);
            }
        });
        return true;
    }

    public static void sendMessageToClient(String message, PlayerEntity player) {
        Messages.INSTANCE.send(PacketDistributor.PLAYER.with(() -> (ServerPlayerEntity) player),
                new PacketSendMessage(message));
    }
}
