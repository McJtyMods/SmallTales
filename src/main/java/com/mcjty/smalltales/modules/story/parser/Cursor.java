package com.mcjty.smalltales.modules.story.parser;

public class Cursor extends IntPair {

    public Cursor(int x, int y) {
        super(x, y);
    }

    public void nextLine(int left, int height) {
        x = left;
        y += height;
    }

    public void shift(int amount) {
        x += amount;
    }
}
