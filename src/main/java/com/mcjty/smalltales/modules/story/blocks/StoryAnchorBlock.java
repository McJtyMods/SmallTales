package com.mcjty.smalltales.modules.story.blocks;

import mcjty.lib.blocks.BaseBlock;
import mcjty.lib.blocks.RotationType;
import mcjty.lib.builder.BlockBuilder;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;

import static mcjty.lib.builder.TooltipBuilder.header;

public class StoryAnchorBlock extends BaseBlock {

    public StoryAnchorBlock() {
        super(new BlockBuilder()
                .properties(Properties.of(Material.STONE).strength(1.0F).sound(SoundType.STONE))
                .info(header())
                .tileEntitySupplier(StoryAnchorTile::new));
    }

    @Override
    public RotationType getRotationType() {
        return RotationType.NONE;
    }
}
