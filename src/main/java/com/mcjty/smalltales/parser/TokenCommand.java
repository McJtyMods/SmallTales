package com.mcjty.smalltales.parser;

import net.minecraft.util.text.TextFormatting;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

public enum TokenCommand {
    CMD_NONE("", null),
    CMD_PARAGRAPH("{p}", null),
    CMD_CENTER("{C}", null),
    CMD_BOLD("{#}", TextFormatting.BOLD),
    CMD_ITALIC("{/}", TextFormatting.ITALIC),
    CMD_UNDERLINE("{_}", TextFormatting.UNDERLINE),
    CMD_STRIKETHROUGH("{-}", TextFormatting.STRIKETHROUGH),
    CMD_OBFUSCATED("{?}", TextFormatting.OBFUSCATED),
    CMD_RESET("{}", TextFormatting.RESET),

    CMD_BLACK("{0}", TextFormatting.BLACK),
    CMD_DARK_BLUE("{1}", TextFormatting.DARK_BLUE),
    CMD_DARK_GREEN("{2}", TextFormatting.DARK_GREEN),
    CMD_DARK_AQUA("{3}", TextFormatting.DARK_AQUA),
    CMD_DARK_RED("{4}", TextFormatting.DARK_RED),
    CMD_DARK_PURPLE("{5}", TextFormatting.DARK_PURPLE),
    CMD_GOLD("{6}", TextFormatting.GOLD),
    CMD_GRAY("{7}", TextFormatting.GRAY),
    CMD_DARK_GRAY("{8}", TextFormatting.DARK_GRAY),
    CMD_BLUE("{9}", TextFormatting.BLUE),
    CMD_GREEN("{10}", TextFormatting.GREEN),
    CMD_AQUA("{11}", TextFormatting.AQUA),
    CMD_RED("{12}", TextFormatting.RED),
    CMD_LIGHT_PURPLE("{13}", TextFormatting.LIGHT_PURPLE),
    CMD_YELLOW("{14}", TextFormatting.YELLOW),
    CMD_WHITE("{15}", TextFormatting.WHITE);

    private final String prefix;
    private final TextFormatting format;

    private static final Map<String, TokenCommand> COMMANDS = new HashMap<>();

    static {
        for (TokenCommand value : values()) {
            COMMANDS.put(value.getPrefix(), value);
        }
    }

    TokenCommand(String text, TextFormatting format) {
        this.prefix = text;
        this.format = format;
    }

    public String getPrefix() {
        return prefix;
    }

    @Nullable
    public TextFormatting getFormat() {
        return format;
    }

    public static TokenCommand byPrefix(String prefix) {
        return COMMANDS.get(prefix);
    }
}
