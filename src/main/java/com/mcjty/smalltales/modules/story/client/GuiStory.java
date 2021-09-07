package com.mcjty.smalltales.modules.story.client;

import com.mcjty.smalltales.SmallTales;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;

public class GuiStory extends Screen {

    private static final int WIDTH = 166;
    private static final int HEIGHT = 200;
    private ResourceLocation GUI = new ResourceLocation(SmallTales.MODID, "textures/gui/the_story.png");

    public GuiStory() {
        super(new StringTextComponent("The Story"));
    }


    @Override
    public void init() {
        super.init();
    }

    @Override
    public void render(MatrixStack matrixStack, int x, int y, float par3) {
        RenderHelper.setupForFlatItems();
        this.renderBackground(matrixStack);

        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.minecraft.getTextureManager().bind(GUI);
        int relX = (this.width - WIDTH) / 2;
        int relY = (this.height - HEIGHT) / 2;
        this.blit(matrixStack, relX, relY, 0, 0, WIDTH, HEIGHT);

        int left = relX + 12;
        int top = relY + 12;

        int yy = top;

        this.font.draw(matrixStack, new StringTextComponent("Line 1"), left, yy, 0x000000); yy += 10;
        this.font.draw(matrixStack, new StringTextComponent("Line 2").withStyle(TextFormatting.BLUE), left, yy, 0x000000); yy += 10;
//        drawCenteredString(matrixStack, this.font, this.title, relX + 10, relY + 10, 0xffffff);
    }



    public static void open() {
        Minecraft.getInstance().setScreen(new GuiStory());
    }
}
