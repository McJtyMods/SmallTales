package com.mcjty.smalltales.modules.story.data;

import com.mcjty.smalltales.setup.ClientSetup;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

public class Story {

    /// Stories by language
    private static final Map<String, Story> STORIES = new HashMap<>();

    public static void register(String language, Story story) {
        STORIES.put(language, story);
    }

    /// Client side: get story based on current language
    @Nullable
    public static Story getStory(World level) {
        String code = level.isClientSide() ? ClientSetup.getLanguageCode() : "en_us";
        return STORIES.getOrDefault(code, STORIES.get("en_us"));
    }

    public static Map<String, Story> getStories() {
        return STORIES;
    }

    private final Map<String, Chapter> chapters = new HashMap<>();
    private final Map<String, ITextComponent> messages = new HashMap<>();

    public void addChapter(Chapter chapter) {
        chapters.put(chapter.getName(), chapter);
    }

    public void addMessage(String name, ITextComponent component) {
        messages.put(name, component);
    }

    public Map<String, Chapter> getChapters() {
        return chapters;
    }

    public Map<String, ITextComponent> getMessages() {
        return messages;
    }
}
