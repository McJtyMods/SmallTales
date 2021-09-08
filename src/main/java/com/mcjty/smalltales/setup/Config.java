package com.mcjty.smalltales.setup;


import com.mcjty.smalltales.modules.story.StoryTextParser;
import net.minecraft.util.text.ITextComponent;
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

    private static ForgeConfigSpec.ConfigValue<List<? extends String>> STORY_PAGES;
    private static Map<String, ITextComponent> storyPages = null;

    public static void register() {
        registerClientConfigs();
        registerServerConfigs();
    }

    private static void registerClientConfigs() {
        Builder builder = new Builder();

        builder.comment("General settings").push("general");
        builder.pop();
        CLIENT_CONFIG = builder.build();

        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, CLIENT_CONFIG);
    }

    private static void registerServerConfigs() {
        Builder builder = new Builder();

        builder.comment("The story pages").push("story");
        STORY_PAGES = builder
                .comment("A list of story pages")
                .defineList("storyPages", Collections.emptyList(), s -> s instanceof String);
        builder.pop();
        SERVER_CONFIG = builder.build();

        ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, SERVER_CONFIG);
    }

    public static Map<String, ITextComponent> getStoryPages() {
        if (storyPages == null) {
            storyPages = new HashMap<>();
            for (String s : STORY_PAGES.get()) {
                String[] split = StringUtils.split(s, "=", 2);
                storyPages.put(split[0], StoryTextParser.parse(split[1]));
            }
        }
        return storyPages;
    }
}
