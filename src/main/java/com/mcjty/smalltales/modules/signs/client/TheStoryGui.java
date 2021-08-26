package com.mcjty.smalltales.modules.signs.client;

import com.mcjty.smalltales.SmallTales;
import com.mcjty.smalltales.modules.signs.SignSettings;
import com.mcjty.smalltales.modules.signs.StoryModule;
import com.mcjty.smalltales.modules.signs.TextureType;
import com.mcjty.smalltales.modules.signs.blocks.AbstractSignTileEntity;
import com.mcjty.smalltales.modules.signs.network.PacketUpdateSignData;
import com.mcjty.smalltales.setup.Config;
import com.mcjty.smalltales.setup.Messages;
import com.mojang.blaze3d.matrix.MatrixStack;
import mcjty.lib.container.GenericContainer;
import mcjty.lib.gui.GenericGuiContainer;
import mcjty.lib.gui.ManualEntry;
import mcjty.lib.gui.Window;
import mcjty.lib.gui.widgets.*;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class TheStoryGui extends GenericGuiContainer<AbstractSignTileEntity, GenericContainer> {

    public static final ResourceLocation SIGNS_GUI = new ResourceLocation(SmallTales.MODID, "textures/varia/signs.png");
    private static final ResourceLocation BACKGROUND = new ResourceLocation(SmallTales.MODID, "textures/gui/signgui.png");
    public static final int WIDTH = 215;
    public static final int HEIGHT = 200;

    private TextField[] labels;
    private ToggleButton backColorButton;
    private ColorSelector backColorSelector;
    private ColorSelector textColorSelector;
    private ToggleButton fullBrightButton;
    private ToggleButton transparentButton;
    private ToggleButton largeButton;
    private ChoiceLabel textureTypeLabel;
    private ImageChoiceLabel imageLabel;

    public TheStoryGui(AbstractSignTileEntity tileEntity, GenericContainer container, PlayerInventory inventory) {
        super(tileEntity, container, inventory, ManualEntry.EMPTY);

        imageWidth = WIDTH;
        imageHeight = HEIGHT;
    }

    @Override
    public void init() {
        super.init();

        labels = new TextField[8];
        for (int i = 0 ; i < labels.length ; i++) {
            labels[i] = Widgets.textfield(10, 10+18*i, WIDTH-20, 16).event(s -> update());
        }
        SignSettings settings = tileEntity.getSettings();
        backColorButton = new ToggleButton()
                .hint(10, HEIGHT-40, 13, 16)
                .text("")
                .checkMarker(true)
                .pressed(settings.getBackColor() != null)
                .event(this::update);
        backColorSelector = new ColorSelector()
                .hint(23, HEIGHT-40, 45, 16)
                .text("Back")
                .currentColor(settings.getBackColor() == null ? 0 : settings.getBackColor())
                .enabled(settings.getBackColor() != null)
                .event(s -> update());
        textColorSelector = new ColorSelector()
                .hint(75, HEIGHT-40, 36, 16)
                .text("Txt")
                .currentColor(settings.getTextColor())
                .event(s -> update());
        textureTypeLabel = new ChoiceLabel()
                .hint(116, HEIGHT-40, 60, 16)
                .choices(Arrays.stream(TextureType.values()).sorted().map(s -> s.name().toLowerCase()).toArray(String[]::new))
                .choice(settings.getTextureType().name().toLowerCase())
                .event(s -> update());
        fullBrightButton = new ToggleButton()
                .hint(10, HEIGHT-20, 50, 16)
                .text("Bright")
                .checkMarker(true)
                .pressed(settings.isBright())
                .event(this::update);
        transparentButton = new ToggleButton()
                .hint(65, HEIGHT-20, 55, 16)
                .text("Transp")
                .checkMarker(true)
                .pressed(settings.isTransparent())
                .event(this::update);
        largeButton = new ToggleButton()
                .hint(125, HEIGHT-20, 50, 16)
                .text("Large")
                .checkMarker(true)
                .pressed(settings.isLarge())
                .event(this::updateLarge);
        imageLabel = new ImageChoiceLabel()
                .hint(178, HEIGHT-35, Config.ICON_SIZE.get(), Config.ICON_SIZE.get())
                .event(s -> update());
        for (int i = 0 ; i <= Config.ICONS.get() ; i++) {
            int u = 32*(i % Config.HORIZONTAL_ICONS.get());
            int v = 32*(i / Config.VERTICAL_ICONS.get());
            imageLabel.choice("" + i, "", SIGNS_GUI, u, v);
        }
        imageLabel.setCurrentChoice(settings.getIconIndex());

        Panel toplevel = Widgets.positional()
                .background(BACKGROUND)
                .children(labels)
                .children(backColorButton, backColorSelector, textColorSelector, fullBrightButton, transparentButton,
                        textureTypeLabel, largeButton, imageLabel);
        toplevel.bounds(leftPos, topPos, WIDTH, HEIGHT);

        window = new Window(this, toplevel);
        updateFromTE();
    }

    private void updateFromTE() {
        int linesSupported = tileEntity.getLinesSupported();
        if (largeButton.isPressed()) {
            linesSupported /= 2;
        }
        List<String> lines = tileEntity.getLines();
        for (int i = 0 ; i < labels.length ; i++) {
            if (i < lines.size()) {
                labels[i].text(lines.get(i));
            } else {
                labels[i].text("");
            }
            labels[i].enabled(i < linesSupported);
        }
    }

    private void updateLarge() {
        update();
        updateFromTE();
    }

    private void update() {
        Messages.INSTANCE.sendToServer(new PacketUpdateSignData(tileEntity.getBlockPos(),
                Arrays.stream(labels).map(TextField::getText).collect(Collectors.toList()),
                backColorButton.isPressed() ? backColorSelector.getCurrentColor() : null,
                textColorSelector.getCurrentColor(), fullBrightButton.isPressed(), transparentButton.isPressed(),
                largeButton.isPressed(),
                TextureType.getByName(textureTypeLabel.getCurrentChoice()),
                imageLabel.getCurrentChoiceIndex()));
    }

    @Override
    public void render(MatrixStack matrixStack, int xSize_lo, int ySize_lo, float par3) {
        super.render(matrixStack, xSize_lo, ySize_lo, par3);
        backColorSelector.enabled(backColorButton.isPressed());
        drawWindow(matrixStack);
    }

    public static void register() {
        register(StoryModule.CONTAINER_SIGN.get(), TheStoryGui::new);
    }
}
