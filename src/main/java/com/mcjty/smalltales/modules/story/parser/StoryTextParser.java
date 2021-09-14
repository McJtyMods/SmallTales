package com.mcjty.smalltales.modules.story.parser;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.commons.lang3.StringUtils;

import java.util.*;

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

    public static ITextComponent parseComponent(String text) {
        for (Map.Entry<String, String> entry : MAPPING.entrySet()) {
            text = StringUtils.replace(text, entry.getKey(), entry.getValue());
        }
        return new StringTextComponent(text);
    }

    public static List<IStoryElement> parse(String text) {
        for (Map.Entry<String, String> entry : MAPPING.entrySet()) {
            text = StringUtils.replace(text, entry.getKey(), entry.getValue());
        }

        int idx = text.indexOf("{i:");
        if (idx != -1) {
            List<IStoryElement> result = new ArrayList<>();
            while (idx != -1) {
                String left = text.substring(0, idx);
                String itemsWithId = text.substring(idx);
                idx = itemsWithId.indexOf("}");
                if (idx == -1) {
                    return Collections.singletonList(new TextStoryElement(new StringTextComponent("Error parsing item!").withStyle(TextFormatting.RED)));
                }
                text = itemsWithId.substring(idx+1);
                itemsWithId = itemsWithId.substring(3, idx);
                result.add(new TextStoryElement(new StringTextComponent(left)));
                String[] items = StringUtils.split(itemsWithId, ",");
                ItemStoryElement element = new ItemStoryElement();
                for (String itemId : items) {
                    Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(itemId));
                    if (item != null && item != Items.AIR) {
                        element.add(new ItemStack(item));
                    } else {
                        result.add(new TextStoryElement(new StringTextComponent("Unknown: " + itemsWithId + "!").withStyle(TextFormatting.RED)));
                    }
                }
                result.add(element);
                idx = text.indexOf("{i:");
            }
            result.add(new TextStoryElement(new StringTextComponent(text)));
            return result;
        }

        return Collections.singletonList(new TextStoryElement(new StringTextComponent(text)));
    }
}
