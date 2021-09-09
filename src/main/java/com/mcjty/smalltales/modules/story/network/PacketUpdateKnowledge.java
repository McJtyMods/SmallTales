package com.mcjty.smalltales.modules.story.network;

import com.mcjty.smalltales.modules.story.blocks.StoryAnchorTile;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class PacketUpdateKnowledge {

    private final BlockPos pos;
    private final String chapter;
    private final String message;

    public PacketUpdateKnowledge(PacketBuffer buf) {
        pos = buf.readBlockPos();
        chapter = buf.readUtf(32767);
        message = buf.readUtf(32767);
    }

    public void toBytes(PacketBuffer buf) {
        buf.writeBlockPos(pos);
        buf.writeUtf(chapter);
        buf.writeUtf(message);
    }

    public PacketUpdateKnowledge(BlockPos pos, String chapter, String message) {
        this.pos = pos;
        this.chapter = chapter;
        this.message = message;
    }

    public boolean handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayerEntity sender = ctx.get().getSender();
            TileEntity be = sender.getLevel().getBlockEntity(pos);
            if (be instanceof StoryAnchorTile) {
                ((StoryAnchorTile) be).setChapter(chapter);
                ((StoryAnchorTile) be).setMessage(message);
            }
        });
        return true;
    }
}
