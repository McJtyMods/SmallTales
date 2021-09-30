package com.mcjty.smalltales.setup;

import com.mcjty.smalltales.client.NamedImages;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.Language;
import net.minecraft.client.resources.LanguageManager;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

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
        NamedImages.parseImages();
    }

}
