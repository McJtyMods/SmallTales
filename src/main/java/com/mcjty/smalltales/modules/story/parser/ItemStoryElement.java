package com.mcjty.smalltales.modules.story.parser;

import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ItemStoryElement implements IStoryElement {

    private final List<ItemStack> items = new ArrayList<>();

    public void add(ItemStack stack) {
        items.add(stack);
    }

    @Override
    public List<IStoryElement> split(ClientWrapper client, int width) {
        return Collections.singletonList(this);
    }

    @Override
    public void draw(ClientWrapper client, Cursor cursor) {
        for (ItemStack item : items) {
            client.draw(item, cursor);
            cursor.shift(20);
        }
    }

    @Override
    public int getHeight() {
        return 20;
    }
}
