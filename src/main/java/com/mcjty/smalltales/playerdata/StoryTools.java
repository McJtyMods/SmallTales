package com.mcjty.smalltales.playerdata;

import com.mcjty.smalltales.modules.story.data.Chapter;
import com.mcjty.smalltales.modules.story.data.Story;
import com.mcjty.smalltales.modules.story.network.PacketSendMessage;
import com.mcjty.smalltales.modules.story.network.PacketSyncStoryProgress;
import com.mcjty.smalltales.setup.Config;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.Util;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Map;

public class StoryTools {

    @CapabilityInject(PlayerStoryProgress.class)
    public static Capability<PlayerStoryProgress> PLAYER_STORY;

    public static void acquireKnowledge(PlayerEntity player, String chapter, String message, boolean reportAlreadyKnown) {
        Story story = Story.getStory(player.level);
        if (story == null) {
            return; // Nothing to do here
        }
        Map<String, Chapter> chapters = story.getChapters();
        Map<String, ITextComponent> messages = story.getMessages();
        if (chapter != null && !chapters.containsKey(chapter)) {
            chapter = null;
        }
        if (message != null && !messages.containsKey(message)) {
            message = null;
        }

        String finalChapter = chapter;
        String finalMessage = message;

        player.getCapability(StoryTools.PLAYER_STORY).ifPresent(progress -> {
            if (finalChapter != null) {
                if (progress.addDiscovered(finalChapter)) {
                    if (finalMessage != null) {
                        player.sendMessage(messages.get(finalMessage), Util.NIL_UUID);
                    } else {
                        player.sendMessage(new TranslationTextComponent("message.smalltales.discover", finalChapter), Util.NIL_UUID);
                    }
                    playSound(player, Config.CHAPTER_SOUND);
                } else {
                    if (reportAlreadyKnown) {
                        player.sendMessage(new TranslationTextComponent("message.smalltales.alreadyknow", finalChapter), Util.NIL_UUID);
                    }
                }
            } else if (finalMessage != null) {
                if (progress.addDiscovered(finalMessage)) {
                    PacketSendMessage.sendMessageToClient(finalMessage, player);
                    playSound(player, Config.MESSAGE_SOUND);
                }
            }
            PacketSyncStoryProgress.syncProgressToClient(progress, player);
        });
    }

    private static void playSound(PlayerEntity player, ForgeConfigSpec.ConfigValue<String> soundConfig) {
        if (!soundConfig.get().isEmpty()) {
            SoundEvent sound = ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation(soundConfig.get()));
            player.level.playSound(null, player, sound, SoundCategory.PLAYERS, 1.0f, 1.0f);
        }
    }
}
