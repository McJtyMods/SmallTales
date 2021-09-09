package com.mcjty.smalltales.playerdata;

import com.mcjty.smalltales.modules.story.network.PacketSyncStory;
import com.mcjty.smalltales.setup.Config;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Util;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;

import java.util.Map;

public class StoryTools {

    @CapabilityInject(PlayerStory.class)
    public static Capability<PlayerStory> PLAYER_STORY;

    public static void acquireKnowledge(PlayerEntity player, String chapter, String message, boolean reportAlreadyKnown) {
        Map<String, ITextComponent> chapters = Config.getChapters();
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
                if (story.addDiscoveredPage(finalChapter)) {
                    if (finalMessage != null) {
                        player.sendMessage(messages.get(finalMessage), Util.NIL_UUID);
                    } else {
                        player.sendMessage(new StringTextComponent("You discover " + finalChapter + "!"), Util.NIL_UUID);
                    }
                } else {
                    if (reportAlreadyKnown) {
                        player.sendMessage(new StringTextComponent("You already know " + finalChapter + "!"), Util.NIL_UUID);
                    }
                }
            } else if (finalMessage != null) {
                player.sendMessage(messages.get(finalMessage), Util.NIL_UUID);
            }
            PacketSyncStory.syncStory(story, player);
        });
    }
}
