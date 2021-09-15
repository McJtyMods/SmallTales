package com.mcjty.smalltales.modules.story.parser;

import javax.annotation.Nonnull;

public class Token {

    public static final Token TOKEN_END = new Token(Type.END, "");

    enum Type {
        COMMAND,
        WORD,
        CHARACTER,
        SPACE,
        ERROR,
        END
    }

    private final Type type;
    private final String text;

    public Token(Type type, String text) {
        this.type = type;
        this.text = text;
    }

    public Type getType() {
        return type;
    }

    public String getText() {
        return text;
    }

    @Nonnull
    public static Token fetch(ParseBuffer buffer) {
        Type type = detectType(buffer);
        switch (type) {
            case COMMAND:
                String command = buffer.skipUntil('}');
                if (command == null) {
                    return new Token(Type.ERROR, "Missing '}'");
                } else {
                    return new Token(type, command);
                }
            case WORD:
                String word = buffer.skipWhile(Token::isWordChar);
                return new Token(type, word);
            case CHARACTER:
                if (buffer.peek() == '\\') {
                    if (buffer.remaining() <= 1) {
                        return TOKEN_END;
                    } else {
                        String rc = buffer.skip(2);
                        return new Token(type, rc.substring(1));
                    }
                } else {
                    return new Token(type, buffer.skip(1));
                }
            case SPACE:
                buffer.skipWhile(Character::isWhitespace);
                return new Token(type, " ");
            case ERROR:
                return new Token(type, "Unknown error");
            case END:
                return TOKEN_END;
        }
        return TOKEN_END;
    }

    public static Type detectType(ParseBuffer buffer) {
        if (buffer.remaining() <= 0) {
            return Type.END;
        }
        char first = buffer.peek();
        if (first == '{') {
            return Type.COMMAND;
        }
        if (Character.isWhitespace(first)) {
            return Type.SPACE;
        }
        if (isWordChar(first)) {
            return Type.WORD;
        }
        return Type.CHARACTER;
    }

    private static boolean isWordChar(char first) {
        return Character.isLetter(first) || Character.isDigit(first);
    }
}
