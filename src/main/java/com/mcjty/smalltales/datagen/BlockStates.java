package com.mcjty.smalltales.datagen;

import com.mcjty.smalltales.SmallTales;
import mcjty.lib.datagen.BaseBlockStateProvider;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.ExistingFileHelper;

public class BlockStates extends BaseBlockStateProvider {

    public BlockStates(DataGenerator gen, ExistingFileHelper exFileHelper) {
        super(gen, SmallTales.MODID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
    }
}
