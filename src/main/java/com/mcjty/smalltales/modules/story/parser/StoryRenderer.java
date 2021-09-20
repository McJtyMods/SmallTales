package com.mcjty.smalltales.modules.story.parser;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.texture.SimpleTexture;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.registries.ForgeRegistries;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StoryRenderer {

    private final Screen screen;
    private final FontRenderer font;
    private final MatrixStack matrixStack;
    private final ItemRenderer itemRenderer;

    private List<TextFormatting> currentFormat = new ArrayList<>();
    private final Cursor cursor;
    private final int left;
    private final Map<String, IntPair> imageDimensionCache = new HashMap<>();
    private int maxHeight = 0;

    public StoryRenderer(Screen screen, FontRenderer font, MatrixStack matrixStack, ItemRenderer itemRenderer, int left, int top) {
        this.screen = screen;
        this.font = font;
        this.matrixStack = matrixStack;
        this.itemRenderer = itemRenderer;
        this.left = left;
        this.cursor = new Cursor(left, top);
    }

    private IntPair getImageDimension(String image) {
        if (!imageDimensionCache.containsKey(image)) {
            SimpleTexture.TextureData data = SimpleTexture.TextureData.load(Minecraft.getInstance().getResourceManager(), new ResourceLocation(image));
            try {
                int width = data.getImage().getWidth();
                int height = data.getImage().getHeight();
                imageDimensionCache.put(image, new IntPair(width, height));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return imageDimensionCache.get(image);
    }

    public void render(Token token, int width) {
        int w = getWidth(token);
        int h = getHeight(token);
        if (cursor.getX() + w >= left+width) {
            cursor.nextLine(left, maxHeight);
            maxHeight = 0;
        }
        if (h > maxHeight) {
            maxHeight = h;
        }
        switch (token.getType()) {
            case COMMAND:
                renderCommand(token.getCommand());
                break;
            case ITEM:
                renderItem(token.getText());
                cursor.shift(w);
                break;
            case IMAGE:
                draw(token.getText(), cursor);
                cursor.shift(getImageDimension(token.getText()).getX());
                break;
            case WORD:
                draw(createComponent(token.getText()), cursor);
                cursor.shift(w);
                break;
            case CHARACTER:
                draw(createComponent(token.getText()), cursor);
                cursor.shift(w);
                break;
            case SPACE:
                if (cursor.getX() != left) {
                    draw(createComponent(token.getText()), cursor);
                    cursor.shift(w);
                }
                break;
            case ERROR:
                break;
            case END:
                break;
        }
    }

    private void renderItem(String id) {
        Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(id));
        draw(new ItemStack(item), cursor);
    }

    private void renderCommand(TokenCommand command) {
        switch (command) {
            case CMD_NONE:
                break;
            case CMD_PARAGRAPH:
                cursor.nextLine(left, maxHeight);
                maxHeight = 0;
                break;
            case CMD_RESET:
                currentFormat.clear();
                break;
            default:
                currentFormat.add(command.getFormat());
                break;
        }
    }

    private ITextComponent createComponent(String text) {
        StringTextComponent component = new StringTextComponent(text);
        currentFormat.forEach(component::withStyle);
        return component;
    }

    private int getWidth(Token token) {
        switch (token.getType()) {
            case COMMAND:
                return 0;
            case ITEM:
                return 18;
            case IMAGE:
                return getImageDimension(token.getText()).getX();
            case WORD:
                return font.width(createComponent(token.getText()));
            case CHARACTER:
                return font.width(createComponent(token.getText()));
            case SPACE:
                return font.width(" ");
            case ERROR:
                return 0;
            case END:
                return 0;
        }
        return 0;
    }

    private int getHeight(Token token) {
        switch (token.getType()) {
            case COMMAND:
                if (token.getCommand() == TokenCommand.CMD_PARAGRAPH) {
                    return 10;
                }
                return 0;
            case ITEM:
                return 18;
            case IMAGE:
                return getImageDimension(token.getText()).getY();
            case WORD:
                return 10;
            case CHARACTER:
                return 10;
            case SPACE:
                return 10;
            case ERROR:
                return 0;
            case END:
                return 0;
        }
        return 0;
    }

    private void draw(String image, Cursor cursor) {
        Minecraft.getInstance().getTextureManager().bind(new ResourceLocation(image));
        IntPair dim = getImageDimension(image);
        AbstractGui.blit(matrixStack, cursor.getX(), cursor.getY(), 0, 0, 0, dim.getX(), dim.getY(), dim.getX(), dim.getY());
    }

    private void draw(ITextComponent component, Cursor cursor) {
        font.draw(matrixStack, component, cursor.getX(), cursor.getY(), 0x000000);
    }

    private void draw(ItemStack stack, Cursor cursor) {
        itemRenderer.renderGuiItem(stack, cursor.getX(), cursor.getY());
    }
}
