package com.mcjty.smalltales.modules.story.client;

import com.mcjty.smalltales.SmallTales;
import com.mcjty.smalltales.playerdata.PlayerProperties;
import com.mcjty.smalltales.playerdata.PlayerStory;
import com.mcjty.smalltales.setup.Config;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.util.IReorderingProcessor;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;

import java.util.Collections;
import java.util.List;

public class GuiStory extends Screen {

    private static final int WIDTH = 166;
    private static final int HEIGHT = 200;
    private ResourceLocation GUI = new ResourceLocation(SmallTales.MODID, "textures/gui/the_story.png");

    private int currentPage = 0;

    public GuiStory() {
        super(new StringTextComponent("The Story"));
    }

    @Override
    public void init() {
        super.init();
    }

    private boolean isHitLeft(double mouseX, double mouseY) {
        int relX = (this.width - WIDTH) / 2;
        int relY = (this.height - HEIGHT) / 2;
        return (mouseX >= relX+5 && mouseX <= relX + 5 + 16) && (mouseY >= relY-17 && mouseY <= relY - 17 + 14);
    }

    private boolean isHitRight(double mouseX, double mouseY) {
        int relX = (this.width - WIDTH) / 2;
        int relY = (this.height - HEIGHT) / 2;
        return (mouseX >= relX+25 && mouseX <= relX + 25 + 16) && (mouseY >= relY-15 && mouseY <= relY - 15 + 14);
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        RenderHelper.setupForFlatItems();
        this.renderBackground(matrixStack);

        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.minecraft.getTextureManager().bind(GUI);
        int relX = (this.width - WIDTH) / 2;
        int relY = (this.height - HEIGHT) / 2;
        this.blit(matrixStack, relX, relY, 0, 0, WIDTH, HEIGHT);

        int left = relX + 12;
        int top = relY + 12;

        final int[] yy = {top};

        List<String> discoveredPages = getDiscoveredPages();

        if (!discoveredPages.isEmpty()) {
            if (currentPage >= discoveredPages.size()) {
                currentPage = discoveredPages.size() - 1;
            }

            if (currentPage > 0) {
                int u = isHitLeft(mouseX, mouseY) ? 25 : 2;
                this.blit(matrixStack, relX+5, relY-15, u, 225, 20, 14);
            }
            if (currentPage < discoveredPages.size()-1) {
                int u = isHitRight(mouseX, mouseY) ? 25 : 2;
                this.blit(matrixStack, relX+25, relY-15, u, 212, 21, 14);
            }

            ITextComponent page = Config.getStoryPages().get(discoveredPages.get(currentPage));
            List<IReorderingProcessor> split = this.font.split(page, WIDTH - 30);
            split.forEach(line -> {
                this.font.draw(matrixStack, line, left, yy[0], 0x000000);
                yy[0] += 10;
            });
        }
    }

    private List<String> getDiscoveredPages() {
        List<String> discoveredPages = Minecraft.getInstance().player.getCapability(PlayerProperties.PLAYER_STORY)
                .map(PlayerStory::getDiscoveredPages)
                .orElse(Collections.emptyList());
        return discoveredPages;
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        if (isHitLeft(mouseX, mouseY)) {
            if (currentPage > 0) {
                currentPage--;
            }
        } else if (isHitRight(mouseX, mouseY)) {
            List<String> discoveredPages = getDiscoveredPages();
            if (currentPage < discoveredPages.size()-1) {
                currentPage++;
            }
        }
        return super.mouseReleased(mouseX, mouseY, button);
    }

    public static void open() {
        Minecraft.getInstance().setScreen(new GuiStory());
    }
}
