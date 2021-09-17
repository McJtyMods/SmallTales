package com.mcjty.smalltales.modules.story.parser;

import javax.annotation.Nullable;
import java.util.function.Predicate;

public class ParseBuffer {

    private final String text;
    private int cursor = 0;

    public ParseBuffer(String text) {
        this.text = text;
    }

    public char peek() {
        return text.charAt(cursor);
    }

    public String peek(int amount) {
        return text.substring(cursor, cursor+amount);
    }

    public int remaining() {
        return text.length() - cursor;
    }

    @Nullable
    public String skipUntil(char end) {
        int idx = text.indexOf(end, cursor);
        if (idx < 0) {
            return null;
        }
        idx++;
        int start = cursor;
        cursor = idx;
        return text.substring(start, idx);
    }

    public String skipWhile(Predicate<Character> test) {
        int start = cursor;
        while (cursor < text.length() && test.test(text.charAt(cursor))) {
            cursor++;
        }
        return text.substring(start, cursor);
    }

    public boolean skip(char c) {
        if (peek() == c) {
            cursor++;
            return true;
        } else {
            return false;
        }
    }

    public String skip(int amount) {
        String result = text.substring(cursor, cursor + amount);
        cursor += amount;
        return result;
    }
}
