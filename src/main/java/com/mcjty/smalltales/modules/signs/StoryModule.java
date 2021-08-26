package com.mcjty.smalltales.modules.signs;

import com.mcjty.smalltales.modules.signs.client.TheStoryGui;
import com.mcjty.smalltales.modules.signs.items.TheStoryItem;
import mcjty.lib.modules.IModule;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

import static com.mcjty.smalltales.setup.Registration.ITEMS;

public class StoryModule implements IModule {

    public static final RegistryObject<TheStoryItem> THE_STORY = ITEMS.register("the_story", TheStoryItem::new);


    @Override
    public void init(FMLCommonSetupEvent event) {

    }

    @Override
    public void initClient(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            TheStoryGui.register();
        });
    }

    @Override
    public void initConfig() {

    }
}
