package com.mcjty.smalltales.setup;

import com.mcjty.smalltales.modules.signs.StoryModule;
import mcjty.lib.setup.DefaultModSetup;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

public class ModSetup extends DefaultModSetup {

    public ModSetup() {
        createTab("smalltales", () -> new ItemStack(StoryModule.THE_STORY.get()));
    }

    @Override
    public void init(FMLCommonSetupEvent e) {
        super.init(e);

        MinecraftForge.EVENT_BUS.register(new ForgeEventHandlers());
        Messages.registerMessages("smalltales");
    }

    @Override
    protected void setupModCompat() {
    }
}
