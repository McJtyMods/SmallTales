package com.mcjty.smalltales.client;

import com.google.gson.*;
import com.mcjty.smalltales.SmallTales;
import com.mcjty.smalltales.parser.JSonTools;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.loading.FMLPaths;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.HashMap;
import java.util.Map;

public class NamedImages {

    public static final Map<String, ImageSpec> IMAGES = new HashMap<>();

    private static void setupDefaultImages() {
        ResourceLocation icons = new ResourceLocation(SmallTales.MODID, "textures/gui/icons.png");
        IMAGES.clear();
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

    public static void parseImages() {
        Path path = FMLPaths.CONFIGDIR.get();
        Path file = path.resolve("smalltales_images.json");
        if (!Files.exists(file)) {
            try {
                createDefaultImages(file);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return;
        }
        JsonElement element = JSonTools.getRootElement(file.toFile(), SmallTales.setup.getLogger());
        if (element == null || !element.isJsonArray()) {
            throw new RuntimeException("This is not a json array!");
        }
        IMAGES.clear();
        JsonArray array = element.getAsJsonArray();
        for (JsonElement el : array) {
            JsonObject object = el.getAsJsonObject();
            String name = object.getAsJsonPrimitive("name").getAsString();
            String texture = object.getAsJsonPrimitive("texture").getAsString();
            int x = object.getAsJsonPrimitive("x").getAsInt();
            int y = object.getAsJsonPrimitive("y").getAsInt();
            int w = object.getAsJsonPrimitive("w").getAsInt();
            int h = object.getAsJsonPrimitive("h").getAsInt();
            ImageSpec spec = new ImageSpec(new ResourceLocation(texture), x, y, w, h);
            IMAGES.put(name, spec);

        }
    }

    private static void createDefaultImages(Path file) throws IOException {
        setupDefaultImages();
        BufferedWriter writer = Files.newBufferedWriter(file, StandardOpenOption.CREATE);

        JsonArray array = new JsonArray();
        for (Map.Entry<String, ImageSpec> entry : IMAGES.entrySet()) {
            JsonObject object = new JsonObject();
            object.addProperty("name", entry.getKey());
            ImageSpec spec = entry.getValue();
            object.addProperty("texture", spec.getId().toString());
            object.addProperty("x", spec.getX());
            object.addProperty("y", spec.getY());
            object.addProperty("w", spec.getW());
            object.addProperty("h", spec.getH());
            array.add(object);
        }

        GsonBuilder builder = new GsonBuilder().setPrettyPrinting();
        Gson gson = builder.create();
        String json = gson.toJson(array);
        writer.write(json);
        writer.flush();
    }
}
