package com.mcjty.smalltales.datagen;

import com.mcjty.smalltales.SmallTales;
import com.mcjty.smalltales.modules.story.StoryModule;
import mcjty.lib.datagen.BaseBlockStateProvider;
import net.minecraft.data.DataGenerator;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.generators.BlockModelBuilder;
import net.minecraftforge.client.model.generators.ModelBuilder;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.client.model.generators.ModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

import static net.minecraftforge.client.model.generators.ModelProvider.BLOCK_FOLDER;

public class BlockStates extends BaseBlockStateProvider {

    public BlockStates(DataGenerator gen, ExistingFileHelper exFileHelper) {
        super(gen, SmallTales.MODID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        simpleBlock(StoryModule.STORY_ANCHOR_STONE.get(), models().cubeAll(StoryModule.STORY_ANCHOR_STONE.get().getRegistryName().getPath(), mcLoc("block/stone")));
        simpleBlock(StoryModule.STORY_ANCHOR_INVISIBLE.get(), models().cubeAll(StoryModule.STORY_ANCHOR_INVISIBLE.get().getRegistryName().getPath(), modLoc("block/invisible")));
        horizontalOrientedBlock(StoryModule.STORY_ANCHOR_PLATE.get(), flatModel("story_anchor_plate"));
    }

    private ModelFile flatModel(String modelName) {
        ResourceLocation side = mcLoc("block/oak_planks");
        ResourceLocation front = modLoc("block/story_anchor_plate");
        BlockModelBuilder model = models().getBuilder(BLOCK_FOLDER + "/" + modelName)
                .parent(models().getExistingFile(mcLoc("block")));
        model.element().from(0, 0, 14).to(16, 16, 16)
                .face(Direction.DOWN).cullface(Direction.DOWN).texture("#side").end()
                .face(Direction.UP).cullface(Direction.UP).texture("#side").end()
                .face(Direction.EAST).cullface(Direction.EAST).texture("#side").end()
                .face(Direction.WEST).cullface(Direction.WEST).texture("#side").end()
                .face(Direction.SOUTH).cullface(Direction.SOUTH).texture("#side").end()
                .face(Direction.NORTH).texture("#front").end()
                .end()
                .texture("side", side)
                .texture("front", front)
                .texture("particle", side);
        return model;
    }
}
