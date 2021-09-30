package com.mcjty.smalltales.parser;

import com.mcjty.smalltales.client.ImageSpec;
import com.mcjty.smalltales.client.NamedImages;
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
import java.util.function.BiConsumer;

public class StoryRenderer {

    private final Screen screen;
    private final FontRenderer font;
    private final MatrixStack matrixStack;
    private final ItemRenderer itemRenderer;

    private List<BiConsumer<Integer, Integer>> currentLine = new ArrayList<>();

    private List<TextFormatting> currentFormat = new ArrayList<>();
    private final Cursor cursor;
    private final int left;
    private final Map<String, IntPair> imageDimensionCache = new HashMap<>();
    private int maxHeight = 0;
    private boolean doCenter = false;

    public StoryRenderer(Screen screen, FontRenderer font, MatrixStack matrixStack, ItemRenderer itemRenderer, int left, int top) {
        this.screen = screen;
        this.font = font;
        this.matrixStack = matrixStack;
        this.itemRenderer = itemRenderer;
        this.left = left;
        this.cursor = new Cursor(left, top);
    }

    private ImageSpec resolveImage(String image) {
        if (NamedImages.IMAGES.containsKey(image)) {
            return NamedImages.IMAGES.get(image);
        } else {
            IntPair dim = getActualImageDimension(image);
            return new ImageSpec(new ResourceLocation(image), 0, 0, dim.x, dim.y);
        }
    }

    private IntPair getActualImageDimension(String image) {
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

    public void flush(int width) {
        int xOffset = getXOffset(width);
        int yOffset = getYOffset();

        currentLine.forEach(code -> code.accept(xOffset, yOffset));
        currentLine.clear();
        cursor.nextLine(left, maxHeight);
        maxHeight = 0;
        doCenter = false;
    }

    private int getXOffset(int width) {
        int xOffset = 0;
        if (doCenter) {
            int w = cursor.getX() - left;
            xOffset = (width-w) / 2;
        }
        return xOffset;
    }

    private int getYOffset() {
        return 0;
    }

    public void render(Token token, int width) {
        int w = getWidth(token);
        int h = getHeight(token);
        if (cursor.getX() + w >= left+width) {
            flush(width);
        }
        if (h > maxHeight) {
            maxHeight = h;
        }
        int cx = cursor.getX();
        int cy = cursor.getY();
        switch (token.getType()) {
            case COMMAND:
                renderCommand(token.getCommand(), width);
                break;
            case ITEM:
                currentLine.add((x, y) -> renderItem(token.getText(), cx +x, cy +y));
                cursor.shift(w);
                break;
            case IMAGE: {
                ImageSpec image = resolveImage(token.getText());
                currentLine.add((x, y) -> draw(image, cx + x, cy + y));
                cursor.shift(image.getW());
                break;
            }
            case WORD: {
                ITextComponent component = createComponent(token.getText());
                currentLine.add((x, y) -> draw(component, cx + x, cy + y));
                cursor.shift(w);
                break;
            }
            case CHARACTER: {
                ITextComponent component = createComponent(token.getText());
                currentLine.add((x, y) -> draw(component, cx + x, cy + y));
                cursor.shift(w);
                break;
            }
            case SPACE:
                if (cx != left) {
                    ITextComponent component = createComponent(token.getText());
                    currentLine.add((x, y) -> draw(component, cx +x, cy +y));
                    cursor.shift(w);
                }
                break;
            case ERROR:
                break;
            case END:
                break;
        }
    }

    private void renderItem(String id, int x, int y) {
        Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(id));
        draw(new ItemStack(item), x, y);
    }

    private void renderCommand(TokenCommand command, int width) {
        switch (command) {
            case CMD_NONE:
                break;
            case CMD_PARAGRAPH:
                flush(width);
                break;
            case CMD_CENTER:
                doCenter = true;
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
                return resolveImage(token.getText()).getW();
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
                return resolveImage(token.getText()).getH();
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

    private void draw(ImageSpec image, int x, int y) {
        Minecraft.getInstance().getTextureManager().bind(image.getId());
        IntPair dim = getActualImageDimension(image.getId().toString());
        AbstractGui.blit(matrixStack, x, y, 0, image.getX(), image.getY(), image.getW(), image.getH(), dim.getX(), dim.getY());
    }

    private void draw(ITextComponent component, int x, int y) {
        font.draw(matrixStack, component, x, y, 0x000000);
    }

    private void draw(ItemStack stack, int x, int y) {
        itemRenderer.renderGuiItem(stack, x, y);
    }
}
