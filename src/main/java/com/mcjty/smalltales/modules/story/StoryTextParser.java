package com.mcjty.smalltales.modules.story;

import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

public class StoryTextParser {

    private static final Map<String, String> MAPPING;

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

    public static ITextComponent parse(String text) {
        for (Map.Entry<String, String> entry : MAPPING.entrySet()) {
            text = StringUtils.replace(text, entry.getKey(), entry.getValue());
        }
        return new StringTextComponent(text);
    }
}
