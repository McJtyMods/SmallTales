package com.mcjty.smalltales.modules.story.parser;

import java.util.ArrayList;
import java.util.List;

public class ParsedStory {

//    private final List<IStoryElement> elements = new ArrayList<>();
//    private StringBuffer currentText = new StringBuffer("");

    public static List<Token> tokenize(String text) {
        ParseBuffer buffer = new ParseBuffer(text);
        Token token = Token.fetch(buffer);
        List<Token> tokens = new ArrayList<>();
        while (token.getType() != Token.Type.END) {
            tokens.add(token);
            token = Token.fetch(buffer);
        }
        return tokens;
    }

//    public static ParsedStory parse(String text) {
//        ParsedStory story = new ParsedStory();
//        tokenize(text).forEach(story::add);
//        return story;
//    }

//    private void flush() {
//        if (currentText.length() != 0) {
//            elements.add(new TextStoryElement(new StringTextComponent(currentText.toString())));
//            currentText = new StringBuffer();
//        }
//    }

//    public void add(Token token) {
//        switch (token.getType()) {
//            case COMMAND:
//                if (StoryTextParser.MAPPING.containsKey(token.getText())) {
//                    currentText.append(StoryTextParser.MAPPING.get(token.getText()));
//                } else {
//                    if ("{I}".equals(token.getText())) {
//                        flush();
//                    } else {
//                        // @todo error
//                    }
//                }
//                break;
//            case WORD:
//                currentText.append(token.getText());
//                break;
//            case CHARACTER:
//                currentText.append(token.getText());
//                break;
//            case SPACE:
//                currentText.append(" ");
//                break;
//            case ERROR:
//                throw new RuntimeException(token.getText());
//            case END:
//                break;
//        }
//    }
//
//    public List<IStoryElement> getElements() {
//        flush();
//        return elements;
//    }
}
