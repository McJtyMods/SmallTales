package com.mcjty.smalltales.modules.story.blocks;

import mcjty.lib.blocks.BaseBlock;
import mcjty.lib.blocks.RotationType;
import mcjty.lib.builder.BlockBuilder;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;

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

    public static StoryAnchorBlock createSolidAnchorPlate() {
        final VoxelShape SHAPE1 = VoxelShapes.box(0.0f, 0.0f, 1.0f*14/16, 1.0f, 1.0f, 1.0f);
        final VoxelShape SHAPE2 = VoxelShapes.box(0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f*2/16);
        final VoxelShape SHAPE3 = VoxelShapes.box(0.0f, 0.0f, 0.0f, 1.0f*2/16, 1.0f, 1.0f);
        final VoxelShape SHAPE4 = VoxelShapes.box(1.0f*14/16, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f);

        return new StoryAnchorBlock(Properties.of(Material.STONE).strength(-1.0F, 3600000.0F).sound(SoundType.STONE).noOcclusion()) {
            @Override
            public RotationType getRotationType() {
                return RotationType.HORIZROTATION;
            }
            @Override
            public VoxelShape getShape(BlockState state, IBlockReader reader, BlockPos pos, ISelectionContext context) {
                switch (state.getValue(BlockStateProperties.HORIZONTAL_FACING)) {
                    case NORTH: return SHAPE1;
                    case SOUTH: return SHAPE2;
                    case WEST: return SHAPE4;
                    case EAST: return SHAPE3;
                }
                return SHAPE1;
            }
        };
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
