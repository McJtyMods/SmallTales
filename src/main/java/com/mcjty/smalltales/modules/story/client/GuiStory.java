package com.mcjty.smalltales.modules.story.client;

import com.mcjty.smalltales.SmallTales;
import com.mcjty.smalltales.modules.story.network.PackedMarkRead;
import com.mcjty.smalltales.playerdata.PlayerStory;
import com.mcjty.smalltales.playerdata.StoryTools;
import com.mcjty.smalltales.setup.Config;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.util.IReorderingProcessor;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;

import java.util.Collections;
import java.util.List;
import java.util.Set;

public class GuiStory extends Screen {

    private static final int WIDTH = 166;
    private static final int HEIGHT = 200;
    private static final ResourceLocation GUI = new ResourceLocation(SmallTales.MODID, "textures/gui/the_story.png");

    private static final int TOC_ENTRIES_PER_PAGE = 14;
    private static int currentPage = 0;

    private List<String> discoveredPages;
    private Set<String> readPages;
    private int contentsPages;

    public GuiStory() {
        super(new StringTextComponent("The Story"));
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    @Override
    public void init() {
        super.init();
        setupPages();
        findUnreadPage();
    }

    private void setupPages() {
        discoveredPages = minecraft.player.getCapability(StoryTools.PLAYER_STORY)
                .map(PlayerStory::getDiscovered)
                .orElse(Collections.emptyList());
        readPages = minecraft.player.getCapability(StoryTools.PLAYER_STORY)
                .map(PlayerStory::getRead)
                .orElse(Collections.emptySet());
        contentsPages = ((discoveredPages.size()-1) / TOC_ENTRIES_PER_PAGE) + 1;
    }

    private void findUnreadPage() {
        for (int i = 0 ; i < discoveredPages.size() ; i++) {
            String page = discoveredPages.get(i);
            if (!readPages.contains(page)) {
                currentPage = i;
                return;
            }
        }
        if (currentPage >= discoveredPages.size()) {
            currentPage = -contentsPages;       // Go to start of table of contents
        }
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

    private int getTocIndex(double mouseX, double mouseY) {
        int relY = (this.height - HEIGHT) / 2;
        if (mouseY > relY+32) {
            int idx = (int) ((mouseY - (relY+32)) / 10);
            if (idx < TOC_ENTRIES_PER_PAGE) {
                return idx;
            }
        }
        return -1;
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

        if (!discoveredPages.isEmpty()) {
            if (currentPage >= discoveredPages.size()) {
                currentPage = discoveredPages.size() - 1;
            }

            if (currentPage > -contentsPages) {
                int u = isHitLeft(mouseX, mouseY) ? 25 : 2;
                this.blit(matrixStack, relX+5, relY-15, u, 225, 20, 14);
            }
            if (currentPage < discoveredPages.size()-1) {
                int u = isHitRight(mouseX, mouseY) ? 25 : 2;
                this.blit(matrixStack, relX+25, relY-15, u, 212, 21, 14);
            }

            ITextComponent page = getPageComponent(mouseX, mouseY);
            if (page != null) {
                List<IReorderingProcessor> split = this.font.split(page, WIDTH - 30);
                split.forEach(line -> {
                    this.font.draw(matrixStack, line, left, yy[0], 0x000000);
                    yy[0] += 10;
                });
                markRead();
            } else {
                this.font.draw(matrixStack, new StringTextComponent("Invalid chapter!").withStyle(TextFormatting.RED), left, yy[0], 0x000000);
            }
        }
    }

    private ITextComponent getPageComponent(double mouseX, double mouseY) {
        if (currentPage < 0) {
            int cp = contentsPages + currentPage + 1;
            IFormattableTextComponent component = new StringTextComponent(TextFormatting.BOLD + "Contents (" + cp + "/" + contentsPages + ")\n" + TextFormatting.RESET);
            int tocIndex = getTocIndex(mouseX, mouseY);
            for (int i = 0 ; i < TOC_ENTRIES_PER_PAGE ; i++) {
                int index = (cp-1) * TOC_ENTRIES_PER_PAGE + i;
                if (index < discoveredPages.size()) {
                    String prefix = "";
                    if (!readPages.contains(discoveredPages.get(index))) {
                        prefix = TextFormatting.GRAY.toString();
                    }
                    if (tocIndex == i) {
                        prefix += TextFormatting.BOLD.toString();
                    }
                    component.append(new StringTextComponent("\n    " + prefix + discoveredPages.get(index)));
                } else {
                    break;
                }
            }
            return component;
        } else {
            return Config.getChapters().get(discoveredPages.get(currentPage));
        }
    }

    private void markRead() {
        if (currentPage >= 0) {
            String page = discoveredPages.get(currentPage);
            minecraft.player.getCapability(StoryTools.PLAYER_STORY).ifPresent(story -> {
                if (!story.getRead().contains(page)) {
                    story.addRead(page);
                    PackedMarkRead.markRead(story, page);
                }
            });
        }
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        if (isHitLeft(mouseX, mouseY)) {
            if (currentPage > -contentsPages) {
                currentPage--;
            }
        } else if (isHitRight(mouseX, mouseY)) {
            if (currentPage < discoveredPages.size()-1) {
                currentPage++;
            }
        } else {
            int index = getTocIndex(mouseX, mouseY);
            if (index != -1) {
                int cp = contentsPages + currentPage + 1;
                currentPage = (cp-1) * TOC_ENTRIES_PER_PAGE + index;
            }
        }
        return super.mouseReleased(mouseX, mouseY, button);
    }

    public static void open() {
        Minecraft.getInstance().setScreen(new GuiStory());
    }
}
