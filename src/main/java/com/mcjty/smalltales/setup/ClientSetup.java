package com.mcjty.smalltales.setup;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mcjty.smalltales.SmallTales;
import com.mcjty.smalltales.client.NamedImages;
import com.mcjty.smalltales.parser.JSonTools;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.Language;
import net.minecraft.client.resources.LanguageManager;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.loading.FMLPaths;

import java.nio.file.Files;
import java.nio.file.Path;

public class ClientSetup {

    public static String getLanguageCode() {
        LanguageManager lm = Minecraft.getInstance().getLanguageManager();
        Language language = lm.getSelected();
        if (language == null) {
            return "en_us";
        } else {
            return language.getCode();
        }
    }

    public static void init(FMLClientSetupEvent event) {
        Path path = FMLPaths.CONFIGDIR.get();
        Path file = path.resolve("smalltales_images.json");
        if (!Files.exists(file)) {
            createDefaultImages(file);
            return;
        }
        JsonElement element = JSonTools.getRootElement(file.toFile(), SmallTales.setup.getLogger());
        if (element == null || !element.isJsonObject()) {
            throw new RuntimeException("This is not a json object!");
        }
        String language = file.getFileName().toString().substring(6, file.getFileName().toString().indexOf(".json"));
        JsonObject object = element.getAsJsonObject();
        parseStory(language, object);

    }

    private static void createDefaultImages(Path file) {
        NamedImages.setupDefaultImages();
    }

}
