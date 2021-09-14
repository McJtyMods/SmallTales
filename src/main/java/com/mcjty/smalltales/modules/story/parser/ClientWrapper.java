package com.mcjty.smalltales.modules.story.parser;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IReorderingProcessor;
import net.minecraft.util.text.ITextComponent;

import java.util.ArrayList;
import java.util.List;

// To avoid needing client only FontRenderer in the api
public class ClientWrapper {

    private final FontRenderer font;
    private final MatrixStack matrixStack;
    private final ItemRenderer itemRenderer;

    public ClientWrapper(FontRenderer font, MatrixStack matrixStack, ItemRenderer itemRenderer) {
        this.font = font;
        this.matrixStack = matrixStack;
        this.itemRenderer = itemRenderer;
    }

    public static List<IStoryElement> split(List<IStoryElement> elements, ClientWrapper font, int width) {
        List<IStoryElement> result = new ArrayList<>();
        elements.forEach(e -> e.split(font, width).forEach(result::add));
        return result;
    }

    public void draw(IReorderingProcessor component, Cursor cursor) {
        font.draw(matrixStack, component, cursor.getX(), cursor.getY(), 0x000000);
    }

    public void draw(ITextComponent component, Cursor cursor) {
        font.draw(matrixStack, component, cursor.getX(), cursor.getY(), 0x000000);
    }

    public void draw(ItemStack stack, Cursor cursor) {
        itemRenderer.renderGuiItem(stack, cursor.getX(), cursor.getY());
    }

    public List<IReorderingProcessor> split(ITextComponent component, int width) {
        return font.split(component, width);
    }
}
