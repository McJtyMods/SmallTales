package com.mcjty.smalltales.modules.story.blocks;

import com.mcjty.smalltales.modules.story.StoryModule;
import mcjty.lib.tileentity.GenericTileEntity;
import net.minecraft.entity.EntityPredicate;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;

import java.util.List;
import java.util.Optional;

public class StoryAnchorTile extends GenericTileEntity implements ITickableTileEntity {

    private String chapter = null;
    private AxisAlignedBB box = null;

    public StoryAnchorTile() {
        super(StoryModule.STORY_ANCHOR_TILE.get());
    }

    @Override
    public void tick() {
        if (!level.isClientSide()) {
            getChapter().ifPresent(c -> {
                List<PlayerEntity> players = level.getEntitiesOfClass(PlayerEntity.class, getBox());
                players.forEach(player -> {
                    if (!player.isCreative()) {

                    }
                });
            });
        }
    }

    private AxisAlignedBB getBox() {
        if (box == null) {
            box = new AxisAlignedBB(getBlockPos().offset(-4, -4, -4), getBlockPos().offset(4, 4, 4));
        }
        return box;
    }

    public Optional<String> getChapter() {
        return Optional.ofNullable(chapter);
    }

    public void setChapter(String chapter) {
        this.chapter = chapter;
        setChanged();
    }

    @Override
    protected void readInfo(CompoundNBT tagCompound) {
        CompoundNBT info = tagCompound.getCompound("Info");
        if (info.contains("chapter")) {
            chapter = info.getString("chapter");
        } else {
            chapter = null;
        }
        super.readInfo(tagCompound);
    }

    @Override
    protected void writeInfo(CompoundNBT tagCompound) {
        CompoundNBT info = getOrCreateInfo(tagCompound);
        if (chapter != null) {
            info.putString("chapter", chapter);
        }
        super.writeInfo(tagCompound);
    }
}
