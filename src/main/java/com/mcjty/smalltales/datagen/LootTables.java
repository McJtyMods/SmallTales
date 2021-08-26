package com.mcjty.smalltales.datagen;

import com.mcjty.smalltales.modules.signs.StoryModule;
import mcjty.lib.datagen.BaseLootTableProvider;
import net.minecraft.data.DataGenerator;

public class LootTables extends BaseLootTableProvider {

    public LootTables(DataGenerator dataGeneratorIn) {
        super(dataGeneratorIn);
    }

    @Override
    protected void addTables() {
    }

    @Override
    public String getName() {
        return "SmallTales LootTables";
    }
}
