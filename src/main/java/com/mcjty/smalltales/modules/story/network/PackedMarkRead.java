package com.mcjty.smalltales.modules.story.network;

import com.mcjty.smalltales.playerdata.PlayerStory;
import com.mcjty.smalltales.playerdata.StoryTools;
import com.mcjty.smalltales.setup.Messages;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class PackedMarkRead {

    private final String page;

    public PackedMarkRead(PacketBuffer buf) {
        page = buf.readUtf(32767);
    }

    public void toBytes(PacketBuffer buf) {
        buf.writeUtf(page);
    }

    public PackedMarkRead(String page) {
        this.page = page;
    }

    public boolean handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ctx.get().getSender().getCapability(StoryTools.PLAYER_STORY).ifPresent(story -> story.addRead(page));
        });
        return true;
    }

    public static void markRead(PlayerStory story, String page) {
        Messages.INSTANCE.sendToServer(new PackedMarkRead(page));
    }
}
