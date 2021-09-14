package com.mcjty.smalltales.playerdata;

import com.mcjty.smalltales.SmallTales;
import com.mcjty.smalltales.modules.story.network.PacketSyncStory;
import com.mcjty.smalltales.modules.story.parser.IStoryElement;
import com.mcjty.smalltales.setup.Config;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.*;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;
import java.util.Map;

public class StoryTools {

    @CapabilityInject(PlayerStory.class)
    public static Capability<PlayerStory> PLAYER_STORY;

    public static void acquireKnowledge(PlayerEntity player, String chapter, String message, boolean reportAlreadyKnown) {
        Map<String, List<IStoryElement>> chapters = Config.getChapters();
        Map<String, ITextComponent> messages = Config.getMessages();
        if (chapter != null && !chapters.containsKey(chapter)) {
            chapter = null;
        }
        if (message != null && !messages.containsKey(message)) {
            message = null;
        }

        String finalChapter = chapter;
        String finalMessage = message;

        player.getCapability(StoryTools.PLAYER_STORY).ifPresent(story -> {
            if (finalChapter != null) {
                if (story.addDiscovered(finalChapter)) {
                    if (finalMessage != null) {
                        player.sendMessage(messages.get(finalMessage), Util.NIL_UUID);
                    } else {
                        player.sendMessage(new StringTextComponent("You discover " + finalChapter + "!"), Util.NIL_UUID);
                    }
                    playSound(player, Config.CHAPTER_SOUND);
                } else {
                    if (reportAlreadyKnown) {
                        player.sendMessage(new StringTextComponent("You already know " + finalChapter + "!"), Util.NIL_UUID);
                    }
                }
            } else if (finalMessage != null) {
                if (story.addDiscovered(finalMessage)) {
                    player.sendMessage(messages.get(finalMessage), Util.NIL_UUID);
                    playSound(player, Config.MESSAGE_SOUND);
                }
            }
            PacketSyncStory.syncStory(story, player);
        });
    }

    private static void playSound(PlayerEntity player, ForgeConfigSpec.ConfigValue<String> soundConfig) {
        if (!soundConfig.get().isEmpty()) {
            SoundEvent sound = ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation(soundConfig.get()));
            player.level.playSound(null, player, sound, SoundCategory.PLAYERS, 1.0f, 1.0f);
        }
    }
}
