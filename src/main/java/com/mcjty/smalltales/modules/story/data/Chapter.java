package com.mcjty.smalltales.modules.story.data;

import com.mcjty.smalltales.parser.Token;
import net.minecraft.network.PacketBuffer;

import java.util.ArrayList;
import java.util.List;

public class Chapter {

    private final String name;
    private final String title;
    private final List<Token> tokens;

    public Chapter(String name, String title, List<Token> tokens) {
        this.name = name;
        this.title = title;
        this.tokens = tokens;
    }

    public Chapter(PacketBuffer buf) {
        name = buf.readUtf(32767);
        title = buf.readUtf(32767);
        int tokenSize = buf.readInt();
        tokens = new ArrayList<>(tokenSize);
        for (int j = 0 ; j < tokenSize ; j++) {
            tokens.add(new Token(buf));
        }
    }

    public void toBytes(PacketBuffer buf) {
        buf.writeUtf(name);
        buf.writeUtf(title);
        buf.writeInt(tokens.size());
        for (Token token : tokens) {
            token.toBytes(buf);
        }
    }

    public String getName() {
        return name;
    }

    public String getTitle() {
        return title;
    }

    public List<Token> getTokens() {
        return tokens;
    }
}
