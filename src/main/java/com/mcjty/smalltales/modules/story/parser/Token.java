package com.mcjty.smalltales.modules.story.parser;

import javax.annotation.Nonnull;

public class Token {

    public static final Token TOKEN_END = new Token(TokenType.END, "");
    public static final Token TOKEN_SPACE = new Token(TokenType.SPACE, " ");

    private final TokenType type;
    private final String text;
    private final TokenCommand command;

    public Token(TokenType type, String text) {
        this.type = type;
        this.text = text;
        this.command = TokenCommand.CMD_NONE;
    }

    public Token(TokenType type, String text, TokenCommand command) {
        this.type = type;
        this.text = text;
        this.command = command;
    }

    public TokenType getType() {
        return type;
    }

    public String getText() {
        return text;
    }

    public TokenCommand getCommand() {
        return command;
    }

    public static Token word(String word) {
        return new Token(TokenType.WORD, word);
    }

    public static Token character(String c) {
        return new Token(TokenType.CHARACTER, c);
    }

    public static Token command(String cmd, TokenCommand command) {
        return new Token(TokenType.COMMAND, cmd, command);
    }

    @Nonnull
    public static Token fetch(ParseBuffer buffer) {
        TokenType type = detectType(buffer);
        switch (type) {
            case COMMAND: {
                String command = buffer.skipUntil('}');
                if (command == null) {
                    return new Token(TokenType.ERROR, "Missing '}'");
                } else {
                    return new Token(type, command, TokenCommand.byPrefix(command));
                }
            }
            case ITEM: {
                buffer.skipUntil(':');
                String modid = buffer.skipWhile(Character::isJavaIdentifierPart);
                if (!buffer.skip(':')) {
                    return new Token(TokenType.ERROR, "Missing ':'");
                }
                String name = buffer.skipWhile(Character::isJavaIdentifierPart);
                if (!buffer.skip('}')) {
                    return new Token(TokenType.ERROR, "Missing '}'");
                }
                return new Token(type, modid + ":" + name);
            }
            case IMAGE: {
                buffer.skipUntil(':');
                String file = buffer.skipWhile(c -> Character.isJavaIdentifierPart(c) || c == '/' || c == ':' || c == '.');
                if (!buffer.skip('}')) {
                    return new Token(TokenType.ERROR, "Missing '}'");
                }
                return new Token(type, file);
            }
            case WORD: {
                String word = buffer.skipWhile(Token::isWordChar);
                return new Token(type, word);
            }
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
                return TOKEN_SPACE;
            case ERROR:
                return new Token(type, "Unknown error");
            case END:
                return TOKEN_END;
        }
        return TOKEN_END;
    }

    public static TokenType detectType(ParseBuffer buffer) {
        if (buffer.remaining() <= 0) {
            return TokenType.END;
        }
        char first = buffer.peek();
        if (first == '{') {
            String peek3 = buffer.peek(3);
            if ("{i:".equals(peek3)) {
                return TokenType.ITEM;
            } else if ("{I:".equals(peek3)) {
                return TokenType.IMAGE;
            }
            return TokenType.COMMAND;
        }
        if (Character.isWhitespace(first)) {
            return TokenType.SPACE;
        }
        if (isWordChar(first)) {
            return TokenType.WORD;
        }
        return TokenType.CHARACTER;
    }

    private static boolean isWordChar(char first) {
        return Character.isLetter(first) || Character.isDigit(first);
    }
}
