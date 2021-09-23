package com.mcjty.smalltales;

import com.mcjty.smalltales.client.RenderWorldLastEventHandler;
import com.mcjty.smalltales.modules.story.StoryModule;
import com.mcjty.smalltales.setup.ClientSetup;
import com.mcjty.smalltales.setup.Config;
import com.mcjty.smalltales.setup.ModSetup;
import com.mcjty.smalltales.setup.Registration;
import mcjty.lib.modules.Modules;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(SmallTales.MODID)
public class SmallTales {

    public static final String MODID = "smalltales";

    @SuppressWarnings("PublicField")
    public static ModSetup setup = new ModSetup();
    private Modules modules = new Modules();

    public SmallTales() {
        Config.register();
        setupModules();
        Registration.register();

        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        bus.addListener(setup::init);
        bus.addListener(modules::init);

        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
            bus.addListener(modules::initClient);
            bus.addListener(ClientSetup::init);
            MinecraftForge.EVENT_BUS.addListener(RenderWorldLastEventHandler::tick);
        });
    }

    private void setupModules() {
        modules.register(new StoryModule());
    }
}
