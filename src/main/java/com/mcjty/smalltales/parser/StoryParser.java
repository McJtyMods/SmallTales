package com.mcjty.smalltales.parser;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mcjty.smalltales.SmallTales;
import com.mcjty.smalltales.modules.story.data.Chapter;
import com.mcjty.smalltales.modules.story.data.Story;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
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

    public static void parseStoryJson(Path defaultConfigPath, Path path) throws IOException {
        Path destinationDir = path.resolve("smalltales");
        if (!Files.exists(destinationDir)) {
            Files.createDirectory(destinationDir);
        }

        copyDefaultStories(defaultConfigPath, destinationDir);

        Files.newDirectoryStream(destinationDir, StoryParser::isStoryFile).forEach(file -> {
            JsonElement element = JSonTools.getRootElement(file.toFile(), SmallTales.setup.getLogger());
            if (element == null || !element.isJsonObject()) {
                throw new RuntimeException("This is not a json object!");
            }
            String language = file.getFileName().toString().substring(6, file.getFileName().toString().indexOf(".json"));
            JsonObject object = element.getAsJsonObject();
            parseStory(language, object);
        });
    }

    private static void copyDefaultStories(Path defaultConfigPath, Path destinationDir) throws IOException {
        Path sourceDir = defaultConfigPath.resolve("smalltales");
        if (!Files.exists(sourceDir)) {
            return;
        }

        Files.newDirectoryStream(sourceDir, StoryParser::isStoryFile).forEach(file -> {
            Path destination = destinationDir.resolve(file.getFileName());
            if (!Files.exists(destination, LinkOption.NOFOLLOW_LINKS)) {
                try {
                    Files.copy(file, destination, StandardCopyOption.COPY_ATTRIBUTES);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    private static boolean isStoryFile(Path name) {
        return name.getFileName().toString().startsWith("story_") && name.getFileName().toString().endsWith(".json");
    }

    private static void parseStory(String language, JsonObject object) {
        Story story = new Story();
        parseChapters(story, object);
        parseMessages(story, object);
        Story.register(language, story);
    }

    private static void parseMessages(Story story, JsonObject object) {
        JsonElement messages = object.get("messages");
        for (JsonElement messageElement : messages.getAsJsonArray()) {
            JsonObject messageObject = messageElement.getAsJsonObject();
            String name = messageObject.get("name").getAsString();
            String text = messageObject.get("text").getAsString();
            story.addMessage(name, StoryParser.parseComponent(text));
        }
    }

    private static void parseChapters(Story story, JsonObject object) {
        JsonElement chapters = object.get("chapters");
        for (JsonElement chapterElement : chapters.getAsJsonArray()) {
            JsonObject chapterObject = chapterElement.getAsJsonObject();
            String name = chapterObject.get("name").getAsString();
            String title = chapterObject.get("title").getAsString();
            JsonElement textTokens = chapterObject.get("text");
            String text;
            if (textTokens.isJsonPrimitive()) {
                text = textTokens.getAsString();
            } else {
                text = "";
                JsonArray array = textTokens.getAsJsonArray();
                for (JsonElement element : array) {
                    text += element.getAsString();
                }
            }
            story.addChapter(new Chapter(name, title, StoryParser.tokenize(text)));
        }
    }

}
