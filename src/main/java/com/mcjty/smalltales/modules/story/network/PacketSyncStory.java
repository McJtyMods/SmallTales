package com.mcjty.smalltales.modules.story.network;

import com.mcjty.smalltales.modules.story.data.Chapter;
import com.mcjty.smalltales.modules.story.data.Story;
import com.mcjty.smalltales.setup.Messages;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.network.PacketDistributor;

import java.util.Map;
import java.util.function.Supplier;

public class PacketSyncStory {

    private final String language;
    private final Story story;

    public PacketSyncStory(PacketBuffer buf) {
        language = buf.readUtf(32767);
        story = new Story();
        int size = buf.readInt();
        for (int i = 0 ; i < size ; i++) {
            story.addChapter(new Chapter(buf));
        }
        size = buf.readInt();
        for (int i = 0 ; i < size ; i++) {
            String name = buf.readUtf(32767);
            ITextComponent component = buf.readComponent();
            story.addMessage(name, component);
        }
    }

    public void toBytes(PacketBuffer buf) {
        buf.writeUtf(language);
        Map<String, Chapter> chapters = story.getChapters();
        buf.writeInt(chapters.size());
        for (Chapter chapter : chapters.values()) {
            chapter.toBytes(buf);
        }
        Map<String, ITextComponent> messages = story.getMessages();
        buf.writeInt(messages.size());
        for (Map.Entry<String, ITextComponent> entry : messages.entrySet()) {
            buf.writeUtf(entry.getKey());
            buf.writeComponent(entry.getValue());
        }
    }

    public PacketSyncStory(String language, Story story) {
        this.language = language;
        this.story = story;
    }

    public boolean handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> Story.register(language, story));
        return true;
    }

    public static void syncStoryToClient(String language, Story story, PlayerEntity player) {
        Messages.INSTANCE.send(PacketDistributor.PLAYER.with(() -> (ServerPlayerEntity) player),
                new PacketSyncStory(language, story));
    }
}
