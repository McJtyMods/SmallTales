package com.mcjty.smalltales.datagen;

import com.mcjty.smalltales.SmallTales;
import com.mcjty.smalltales.modules.story.StoryModule;
import mcjty.lib.datagen.BaseBlockStateProvider;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.ExistingFileHelper;

public class BlockStates extends BaseBlockStateProvider {

    public BlockStates(DataGenerator gen, ExistingFileHelper exFileHelper) {
        super(gen, SmallTales.MODID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        simpleBlock(StoryModule.STORY_ANCHOR_STONE.get(), models().cubeAll(StoryModule.STORY_ANCHOR_STONE.get().getRegistryName().getPath(), mcLoc("block/stone")));
        simpleBlock(StoryModule.STORY_ANCHOR_INVISIBLE.get(), models().cubeAll(StoryModule.STORY_ANCHOR_INVISIBLE.get().getRegistryName().getPath(), modLoc("block/invisible")));
    }
}
