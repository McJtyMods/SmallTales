package com.mcjty.smalltales.datagen;

import com.mcjty.smalltales.modules.story.StoryModule;
import mcjty.lib.datagen.BaseRecipeProvider;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.data.ShapedRecipeBuilder;
import net.minecraft.item.Items;

import java.util.function.Consumer;

public class Recipes extends BaseRecipeProvider {

    public Recipes(DataGenerator generatorIn) {
        super(generatorIn);
    }

    @Override
    protected void buildShapelessRecipes(Consumer<IFinishedRecipe> consumer) {
        build(consumer, ShapedRecipeBuilder.shaped(StoryModule.THE_STORY.get())
                        .define('X', Items.BOOK)
                        .define('I', Items.INK_SAC)
                        .unlockedBy("book", has(Items.BOOK)),
                "IXI");
    }

}
