package com.mcjty.smalltales.modules.story.parser;

public class Cursor {

    private int x;
    private int y;

    public Cursor(int x, int y) {
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
