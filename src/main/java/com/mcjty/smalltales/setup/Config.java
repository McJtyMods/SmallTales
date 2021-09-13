package com.mcjty.smalltales.setup;


import com.mcjty.smalltales.modules.story.StoryTextParser;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.common.ForgeConfig;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.Builder;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;
import org.apache.commons.lang3.StringUtils;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Config {

    public static ForgeConfigSpec SERVER_CONFIG;
    public static ForgeConfigSpec CLIENT_CONFIG;

    private static ForgeConfigSpec.ConfigValue<List<? extends String>> CHAPTERS;
    private static Map<String, ITextComponent> chapters = null;

    private static ForgeConfigSpec.ConfigValue<List<? extends String>> MESSAGES;
    private static Map<String, ITextComponent> messages = null;

    public static ForgeConfigSpec.ConfigValue<String> CHAPTER_SOUND;
    public static ForgeConfigSpec.ConfigValue<String> MESSAGE_SOUND;

    public static void register() {
        registerClientConfigs();
        registerServerConfigs();
    }

    private static void registerClientConfigs() {
        Builder builder = new Builder();

        builder.comment("General settings").push("general");

        CHAPTER_SOUND = builder
                .comment("Sound to play when a new chapter is discovered (make empty to disable this sound)")
                .define("chapterSound", "smalltales:writing");
        MESSAGE_SOUND = builder
                .comment("Sound to play when a new message is discovered (make empty to disable this sound)")
                .define("messageSound", "smalltales:sonar");

        builder.pop();
        CLIENT_CONFIG = builder.build();

        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, CLIENT_CONFIG);
    }

    private static void registerServerConfigs() {
        Builder builder = new Builder();

        builder.comment("The chapters and messages").push("story");
        CHAPTERS = builder
                .comment("A list of chapters")
                .defineList("chapters", Collections.emptyList(), s -> s instanceof String);
        MESSAGES = builder
                .comment("A list of messages")
                .defineList("messages", Collections.emptyList(), s -> s instanceof String);
        builder.pop();
        SERVER_CONFIG = builder.build();

        ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, SERVER_CONFIG);
    }

    public static Map<String, ITextComponent> getChapters() {
        if (chapters == null) {
            chapters = new HashMap<>();
            for (String s : CHAPTERS.get()) {
                String[] split = StringUtils.split(s, "=", 2);
                chapters.put(split[0], StoryTextParser.parse(split[1]));
            }
        }
        return chapters;
    }

    public static Map<String, ITextComponent> getMessages() {
        if (messages == null) {
            messages = new HashMap<>();
            for (String s : MESSAGES.get()) {
                String[] split = StringUtils.split(s, "=", 2);
                messages.put(split[0], StoryTextParser.parse(split[1]));
            }
        }
        return messages;
    }
}
