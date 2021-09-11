package com.mcjty.smalltales.datagen;

import com.mcjty.smalltales.modules.story.StoryModule;
import mcjty.lib.datagen.BaseLootTableProvider;
import net.minecraft.data.DataGenerator;

public class LootTables extends BaseLootTableProvider {

    public LootTables(DataGenerator dataGeneratorIn) {
        super(dataGeneratorIn);
    }

    @Override
    protected void addTables() {
        addStandardTable(StoryModule.STORY_ANCHOR_STONE.get());
        addStandardTable(StoryModule.STORY_ANCHOR_INVISIBLE.get());
        addStandardTable(StoryModule.STORY_ANCHOR_PLATE.get());
    }

    @Override
    public String getName() {
        return "SmallTales LootTables";
    }
}
