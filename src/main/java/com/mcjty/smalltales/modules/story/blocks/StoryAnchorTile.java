package com.mcjty.smalltales.modules.story.blocks;

import com.mcjty.smalltales.modules.story.StoryModule;
import com.mcjty.smalltales.playerdata.StoryTools;
import mcjty.lib.tileentity.GenericTileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.util.math.AxisAlignedBB;

import java.util.List;
import java.util.Optional;

public class StoryAnchorTile extends GenericTileEntity implements ITickableTileEntity {

    private String chapter = null;
    private String message = null;
    private AxisAlignedBB box = null;

    public StoryAnchorTile() {
        super(StoryModule.STORY_ANCHOR_TILE.get());
    }

    @Override
    public void tick() {
        if (!level.isClientSide()) {
            if (chapter != null || message != null) {
                List<PlayerEntity> players = level.getEntitiesOfClass(PlayerEntity.class, getBox());
                players.forEach(player -> {
                    if (!player.isCreative()) {
                        StoryTools.acquireKnowledge(player, chapter, message, false);
                    }
                });
            }
        }
    }

    private AxisAlignedBB getBox() {
        if (box == null) {
            box = new AxisAlignedBB(getBlockPos()).inflate(3.0);
        }
        return box;
    }

    public Optional<String> getChapter() {
        return Optional.ofNullable(chapter);
    }

    public Optional<String> getMessage() {
        return Optional.ofNullable(message);
    }

    public void setChapter(String chapter) {
        if (chapter.isEmpty()) {
            chapter = null;
        }
        this.chapter = chapter;
        markDirtyClient();
    }

    public void setMessage(String message) {
        if (message.isEmpty()) {
            message = null;
        }
        this.message = message;
        markDirtyClient();
    }

    @Override
    protected void readInfo(CompoundNBT tagCompound) {
        CompoundNBT info = tagCompound.getCompound("Info");
        if (info.contains("chapter")) {
            chapter = info.getString("chapter");
        } else {
            chapter = null;
        }
        if (info.contains("message")) {
            message = info.getString("message");
        } else {
            message = null;
        }
        super.readInfo(tagCompound);
    }

    @Override
    protected void writeInfo(CompoundNBT tagCompound) {
        CompoundNBT info = getOrCreateInfo(tagCompound);
        if (chapter != null) {
            info.putString("chapter", chapter);
        }
        if (message != null) {
            info.putString("message", message);
        }
        super.writeInfo(tagCompound);
    }
}
