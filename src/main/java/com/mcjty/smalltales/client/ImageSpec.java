package com.mcjty.smalltales.client;

import net.minecraft.util.ResourceLocation;

import java.util.HashMap;
import java.util.Map;

public class ImageSpec {

    private final ResourceLocation id;
    private final int x;
    private final int y;
    private final int w;
    private final int h;

    public ImageSpec(ResourceLocation id, int x, int y, int w, int h) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
    }

    public ResourceLocation getId() {
        return id;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getW() {
        return w;
    }

    public int getH() {
        return h;
    }
}
