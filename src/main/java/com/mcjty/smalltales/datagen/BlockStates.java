package com.mcjty.smalltales.datagen;

import com.mcjty.smalltales.SmallTales;
import com.mcjty.smalltales.modules.story.StoryModule;
import mcjty.lib.datagen.BaseBlockStateProvider;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.common.data.ExistingFileHelper;

public class BlockStates extends BaseBlockStateProvider {

    public BlockStates(DataGenerator gen, ExistingFileHelper exFileHelper) {
        super(gen, SmallTales.MODID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        simpleBlock(StoryModule.STORY_ANCHOR.get(), models().cubeAll(StoryModule.STORY_ANCHOR.get().getRegistryName().getPath(), mcLoc("block/stone")));
    }
}
