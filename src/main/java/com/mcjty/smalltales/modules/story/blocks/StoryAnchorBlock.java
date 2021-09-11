package com.mcjty.smalltales.modules.story.blocks;

import mcjty.lib.blocks.BaseBlock;
import mcjty.lib.blocks.RotationType;
import mcjty.lib.builder.BlockBuilder;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;

import static mcjty.lib.builder.TooltipBuilder.header;

public class StoryAnchorBlock extends BaseBlock {

    public StoryAnchorBlock(Properties properties) {
        super(new BlockBuilder()
                .properties(properties)
                .info(header())
                .tileEntitySupplier(StoryAnchorTile::new));
    }

    public static StoryAnchorBlock createSolidAnchorBlock() {
        return new StoryAnchorBlock(Properties.of(Material.STONE).strength(-1.0F, 3600000.0F).sound(SoundType.STONE));
    }

    public static StoryAnchorBlock createInvisibleAnchorBlock() {
        return new StoryAnchorBlock(Properties.of(Material.STONE).strength(-1.0F, 3600000.0F).sound(SoundType.STONE).noOcclusion()) {
            @Override
            public BlockRenderType getRenderShape(BlockState state) {
                return BlockRenderType.INVISIBLE;
            }
        };
    }

    @Override
    public RotationType getRotationType() {
        return RotationType.NONE;
    }
}
