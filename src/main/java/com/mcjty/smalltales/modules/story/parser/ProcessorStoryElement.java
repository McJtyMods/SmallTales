package com.mcjty.smalltales.modules.story.parser;

import net.minecraft.util.IReorderingProcessor;

import java.util.Collections;
import java.util.List;

public class ProcessorStoryElement implements IStoryElement {

    private final IReorderingProcessor component;

    public ProcessorStoryElement(IReorderingProcessor component) {
        this.component = component;
    }

    @Override
    public List<IStoryElement> split(ClientWrapper client, int width) {
        return Collections.singletonList(this);
    }

    @Override
    public void draw(ClientWrapper client, Cursor cursor) {
        client.draw(component, cursor);
    }

    @Override
    public int getHeight() {
        return 10;
    }
}
