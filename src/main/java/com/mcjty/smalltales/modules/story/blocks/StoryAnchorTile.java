package com.mcjty.smalltales.modules.story.blocks;

import com.mcjty.smalltales.client.RenderWorldLastEventHandler;
import com.mcjty.smalltales.modules.story.StoryModule;
import com.mcjty.smalltales.modules.story.items.ConfiguratorItem;
import com.mcjty.smalltales.playerdata.StoryTools;
import mcjty.lib.tileentity.GenericTileEntity;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.Util;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.text.StringTextComponent;

import java.util.List;
import java.util.Optional;

public class StoryAnchorTile extends GenericTileEntity implements ITickableTileEntity {

    private String chapter = null;
    private String message = null;
    private int range = 4;
    private boolean onActivate = false;

    private AxisAlignedBB box = null;

    public StoryAnchorTile() {
        super(StoryModule.STORY_ANCHOR_TILE.get());
    }

    @Override
    public void tick() {
        if (!level.isClientSide()) {
            if (!onActivate) {
                if (chapter != null || message != null) {
                    List<PlayerEntity> players = level.getEntitiesOfClass(PlayerEntity.class, getBox());
                    players.forEach(player -> {
                        if (!player.isCreative()) {
                            StoryTools.acquireKnowledge(player, chapter, message, false);
                        }
                    });
                }
            }
        } else {
            RenderWorldLastEventHandler.registerAnchor(getBlockPos());
        }
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, PlayerEntity player, Hand hand, BlockRayTraceResult result) {
        if (!(player.getMainHandItem().getItem() instanceof ConfiguratorItem)) {
            if (!player.level.isClientSide()) {
                if (onActivate) {
                    StoryTools.acquireKnowledge(player, chapter, message, true);
                } else {
                    player.sendMessage(new StringTextComponent("Nothing happens!"), Util.NIL_UUID);
                }
            }
            return ActionResultType.SUCCESS;
        }
        return super.onBlockActivated(state, player, hand, result);
    }

    private AxisAlignedBB getBox() {
        if (box == null) {
            box = new AxisAlignedBB(getBlockPos()).inflate(range);
        }
        return box;
    }

    public int getRange() {
        return range;
    }

    public void setRange(int range) {
        this.range = range;
        box = null;
        markDirtyClient();
    }

    public boolean isOnActivate() {
        return onActivate;
    }

    public void setOnActivate(boolean onActivate) {
        this.onActivate = onActivate;
        markDirtyClient();
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
        readClientDataFromNBT(tagCompound);
        super.readInfo(tagCompound);
    }

    @Override
    public void readClientDataFromNBT(CompoundNBT tagCompound) {
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
        onActivate = info.getBoolean("onactivate");
        range = info.getInt("range");
    }

    @Override
    protected void writeInfo(CompoundNBT tagCompound) {
        writeClientDataToNBT(tagCompound);
        super.writeInfo(tagCompound);
    }

    @Override
    public void writeClientDataToNBT(CompoundNBT tagCompound) {
        CompoundNBT info = getOrCreateInfo(tagCompound);
        if (chapter != null) {
            info.putString("chapter", chapter);
        }
        if (message != null) {
            info.putString("message", message);
        }
        info.putBoolean("onactivate", onActivate);
        info.putInt("range", range);
    }
}
