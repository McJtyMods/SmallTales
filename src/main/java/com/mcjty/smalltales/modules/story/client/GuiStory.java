package com.mcjty.smalltales.modules.story.client;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.text.StringTextComponent;

public class GuiStory extends Screen {

    private static final int XSIZE = 166;
    private static final int YSIZE = 200;

    public GuiStory() {
        super(new StringTextComponent("The Story"));
    }

    @Override
    public void init() {
        super.init();
    }

    @Override
    public void render(MatrixStack matrixStack, int x, int y, float par3) {
        super.render(matrixStack, x, y, par3);
    }

    public static void open() {
        Minecraft.getInstance().setScreen(new GuiStory());
    }
}
