package com.mcjty.smalltales.setup;

import com.mcjty.smalltales.SmallTales;
import com.mcjty.smalltales.commands.ModCommands;
import com.mcjty.smalltales.modules.story.data.Story;
import com.mcjty.smalltales.modules.story.network.PacketSyncStory;
import com.mcjty.smalltales.modules.story.network.PacketSyncStoryProgress;
import com.mcjty.smalltales.parser.StoryParser;
import com.mcjty.smalltales.playerdata.PlayerStoryProgress;
import com.mcjty.smalltales.playerdata.PropertiesDispatcher;
import com.mcjty.smalltales.playerdata.StoryTools;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.FolderName;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.server.FMLServerStartedEvent;
import net.minecraftforge.fml.loading.FMLConfig;
import net.minecraftforge.fml.loading.FMLPaths;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Map;

public class ForgeEventHandlers {

    @SubscribeEvent
    public void registerCommands(RegisterCommandsEvent event) {
        ModCommands.register(event.getDispatcher());
    }

    @SubscribeEvent
    public void onWorldLoad(FMLServerStartedEvent event) {
        Path path = event.getServer().getWorldPath(FolderName.ROOT);
        Path defaultConfigPath = FMLPaths.GAMEDIR.get().resolve(FMLConfig.defaultConfigPath());
        try {
            StoryParser.parseStoryJson(defaultConfigPath, path);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @SubscribeEvent
    public void onEntityConstructing(AttachCapabilitiesEvent<Entity> event){
        if (event.getObject() instanceof PlayerEntity) {
            if (!event.getObject().getCapability(StoryTools.PLAYER_STORY).isPresent()) {
                event.addCapability(new ResourceLocation(SmallTales.MODID, "story"), new PropertiesDispatcher());
            }
        }
    }

    @SubscribeEvent
    public void onPlayerCloned(PlayerEvent.Clone event) {
        if (event.isWasDeath()) {
            // We need to copyFrom the capabilities
            LazyOptional<PlayerStoryProgress> capability = event.getOriginal().getCapability(StoryTools.PLAYER_STORY);
            capability.ifPresent(oldStore -> {
                event.getPlayer().getCapability(StoryTools.PLAYER_STORY).ifPresent(newStore -> {
                    newStore.copyFrom(oldStore);
                });
            });
        }
    }

    @SubscribeEvent
    public void onPlayerLogon(PlayerEvent.PlayerLoggedInEvent event) {
        event.getPlayer().getCapability(StoryTools.PLAYER_STORY).ifPresent(story -> PacketSyncStoryProgress.syncProgressToClient(story, event.getPlayer()));
        for (Map.Entry<String, Story> entry : Story.getStories().entrySet()) {
            PacketSyncStory.syncStoryToClient(entry.getKey(), entry.getValue(), event.getPlayer());
        }
    }
}

