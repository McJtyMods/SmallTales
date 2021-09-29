package com.mcjty.smalltales.client;

import com.mcjty.smalltales.SmallTales;
import net.minecraft.util.ResourceLocation;

import java.util.HashMap;
import java.util.Map;

public class NamedImages {

    public static final Map<String, ImageSpec> IMAGES = new HashMap<>();

    public static void setupDefaultImages() {
        ResourceLocation icons = new ResourceLocation(SmallTales.MODID, "textures/gui/icons.png");
        IMAGES.put("star1",       new ImageSpec(icons,   0,   0, 64, 64));
        IMAGES.put("symbol1",     new ImageSpec(icons,  64,   0, 64, 64));
        IMAGES.put("hourglass",   new ImageSpec(icons, 128,   0, 64, 64));
        IMAGES.put("skull",       new ImageSpec(icons, 192,   0, 64, 64));
        IMAGES.put("symbol2",     new ImageSpec(icons,   0,  64, 64, 64));
        IMAGES.put("symbol3",     new ImageSpec(icons,  64,  64, 64, 64));
        IMAGES.put("symbol4",     new ImageSpec(icons, 128,  64, 64, 64));
        IMAGES.put("symbol5",     new ImageSpec(icons, 192,  64, 64, 64));
        IMAGES.put("symbol6",     new ImageSpec(icons,   0, 128, 64, 64));
        IMAGES.put("symbol7",     new ImageSpec(icons,  64, 128, 64, 64));
        IMAGES.put("book",        new ImageSpec(icons, 128, 128, 64, 64));
        IMAGES.put("symbol8",     new ImageSpec(icons, 192, 128, 64, 64));
        IMAGES.put("star2",       new ImageSpec(icons,   0, 192, 64, 64));
        IMAGES.put("ink",         new ImageSpec(icons,  64, 192, 64, 64));
        IMAGES.put("bomb",        new ImageSpec(icons, 128, 192, 64, 64));
        IMAGES.put("block",       new ImageSpec(icons, 192, 192, 64, 64));
    }
}
