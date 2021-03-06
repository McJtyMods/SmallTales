package com.mcjty.smalltales.parser;

public class Cursor extends IntPair {

    public Cursor(int x, int y) {
        super(x, y);
    }

    @Override
    public void nextLine(int left, int height) {
        x = left;
        y += height;
    }

    @Override
    public void shift(int amount) {
        x += amount;
    }
}
