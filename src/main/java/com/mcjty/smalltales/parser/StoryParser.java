package com.mcjty.smalltales.parser;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mcjty.smalltales.SmallTales;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StoryParser {

    public static final Map<String, String> MAPPING;

    static {
        MAPPING = new HashMap<>();
        MAPPING.put("{#}", TextFormatting.BOLD.toString());
        MAPPING.put("{/}", TextFormatting.ITALIC.toString());
        MAPPING.put("{_}", TextFormatting.UNDERLINE.toString());
        MAPPING.put("{-}", TextFormatting.STRIKETHROUGH.toString());
        MAPPING.put("{?}", TextFormatting.OBFUSCATED.toString());
        MAPPING.put("{}", TextFormatting.RESET.toString());
        MAPPING.put("{p}", "\n");

        MAPPING.put("{0}", TextFormatting.BLACK.toString());
        MAPPING.put("{1}", TextFormatting.DARK_BLUE.toString());
        MAPPING.put("{2}", TextFormatting.DARK_GREEN.toString());
        MAPPING.put("{3}", TextFormatting.DARK_AQUA.toString());
        MAPPING.put("{4}", TextFormatting.DARK_RED.toString());
        MAPPING.put("{5}", TextFormatting.DARK_PURPLE.toString());
        MAPPING.put("{6}", TextFormatting.GOLD.toString());
        MAPPING.put("{7}", TextFormatting.GRAY.toString());
        MAPPING.put("{8}", TextFormatting.DARK_GRAY.toString());
        MAPPING.put("{9}", TextFormatting.BLUE.toString());
        MAPPING.put("{10}", TextFormatting.GREEN.toString());
        MAPPING.put("{11}", TextFormatting.AQUA.toString());
        MAPPING.put("{12}", TextFormatting.RED.toString());
        MAPPING.put("{13}", TextFormatting.LIGHT_PURPLE.toString());
        MAPPING.put("{14}", TextFormatting.YELLOW.toString());
        MAPPING.put("{15}", TextFormatting.WHITE.toString());
    }


    public static ITextComponent parseComponent(String text) {
        for (Map.Entry<String, String> entry : MAPPING.entrySet()) {
            text = StringUtils.replace(text, entry.getKey(), entry.getValue());
        }
        return new StringTextComponent(text);
    }

    public static List<Token> tokenize(String text) {
        ParseBuffer buffer = new ParseBuffer(text);
        Token token = Token.fetch(buffer);
        List<Token> tokens = new ArrayList<>();
        while (token.getType() != TokenType.END) {
            tokens.add(token);
            token = Token.fetch(buffer);
        }
        return tokens;
    }

    public static void parseStoryJson(String path) {
        File directory = new File(path + File.separator + "smalltales");
        if (!directory.exists()) {
            directory.mkdir();
        }
        JsonElement element = JSonTools.getRootElement(path, "story.json", SmallTales.setup.getLogger());
        if (element == null || !element.isJsonObject()) {
            throw new RuntimeException("This is not a json object!");
        }
        JsonObject object = element.getAsJsonObject();
        JsonElement chapters = object.get("chapters");
        JsonElement messages = object.get("messages");
    }

}
