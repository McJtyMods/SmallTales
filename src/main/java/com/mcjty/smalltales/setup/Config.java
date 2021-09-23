package com.mcjty.smalltales.setup;


import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.Builder;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;

public class Config {

    public static ForgeConfigSpec SERVER_CONFIG;
    public static ForgeConfigSpec CLIENT_CONFIG;

    public static ForgeConfigSpec.ConfigValue<String> CHAPTER_SOUND;
    public static ForgeConfigSpec.ConfigValue<String> MESSAGE_SOUND;

    public static void register() {
        registerClientConfigs();
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
}
