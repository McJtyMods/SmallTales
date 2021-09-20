package com.mcjty.smalltales.modules.story.parser;

public class IntPair {

    protected int x;
    protected int y;

    public IntPair(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void setXY(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void nextLine(int left, int height) {
        x = left;
        y += height;
    }

    public void shift(int amount) {
        x += amount;
    }
}
