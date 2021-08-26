package com.mcjty.smalltales.datagen;

import com.mcjty.smalltales.SmallTales;
import com.mcjty.smalltales.modules.story.StoryModule;
import mcjty.lib.datagen.BaseItemModelProvider;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.ExistingFileHelper;

public class Items extends BaseItemModelProvider {

    public Items(DataGenerator generator, ExistingFileHelper existingFileHelper) {
        super(generator, SmallTales.MODID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        itemGenerated(StoryModule.THE_STORY.get(), "item/the_story");
    }

    @Override
    public String getName() {
        return "SmallTales Item Models";
    }
}
