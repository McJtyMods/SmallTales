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
    private final int range;
    private final boolean onActivate;

    public PacketUpdateKnowledge(PacketBuffer buf) {
        pos = buf.readBlockPos();
        chapter = buf.readUtf(32767);
        message = buf.readUtf(32767);
        range = buf.readInt();
        onActivate = buf.readBoolean();
    }

    public void toBytes(PacketBuffer buf) {
        buf.writeBlockPos(pos);
        buf.writeUtf(chapter);
        buf.writeUtf(message);
        buf.writeInt(range);
        buf.writeBoolean(onActivate);
    }

    public PacketUpdateKnowledge(BlockPos pos, String chapter, String message, int range, boolean onActivate) {
        this.pos = pos;
        this.chapter = chapter;
        this.message = message;
        this.range = range;
        this.onActivate = onActivate;
    }

    public boolean handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayerEntity sender = ctx.get().getSender();
            TileEntity be = sender.getLevel().getBlockEntity(pos);
            if (be instanceof StoryAnchorTile) {
                StoryAnchorTile tile = (StoryAnchorTile) be;
                tile.setChapter(chapter);
                tile.setMessage(message);
                tile.setRange(range);
                tile.setOnActivate(onActivate);
            }
        });
        return true;
    }
}
