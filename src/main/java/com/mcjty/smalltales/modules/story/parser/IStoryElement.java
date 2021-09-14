package com.mcjty.smalltales.modules.story.parser;

import java.util.List;

public interface IStoryElement {

    List<IStoryElement> split(ClientWrapper client, int width);

    void draw(ClientWrapper client, Cursor cursor);

    int getHeight();
}
