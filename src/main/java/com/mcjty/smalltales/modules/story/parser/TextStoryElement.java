package com.mcjty.smalltales.modules.story.parser;

import net.minecraft.util.IReorderingProcessor;
import net.minecraft.util.text.ITextComponent;

import java.util.List;
import java.util.stream.Collectors;

public class TextStoryElement implements IStoryElement {

    private final ITextComponent component;

    public TextStoryElement(ITextComponent component) {
        this.component = component;
    }

    @Override
    public List<IStoryElement> split(ClientWrapper client, int width) {
        List<IReorderingProcessor> split = client.split(component, width);
        return split.stream().map(ProcessorStoryElement::new).collect(Collectors.toList());
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
