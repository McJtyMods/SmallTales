package com.mcjty.smalltales.modules.story.client;

import com.mcjty.smalltales.modules.story.blocks.StoryAnchorTile;
import com.mcjty.smalltales.modules.story.network.PacketUpdateKnowledge;
import com.mcjty.smalltales.setup.Messages;
import com.mojang.blaze3d.matrix.MatrixStack;
import mcjty.lib.gui.*;
import mcjty.lib.gui.widgets.*;
import net.minecraft.client.Minecraft;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;

import static mcjty.lib.gui.widgets.Widgets.*;

public class GuiConfigurator extends GuiItemScreen implements IKeyReceiver {

    public static final int WIDTH = 340;
    public static final int HEIGHT = 200;

    private final BlockPos pos;
    private TextField chapterTextField;
    private TextField messageTextField;
    private Slider rangeSlider;
    private ScrollableLabel range;
    private ToggleButton onActivateButton;

    public GuiConfigurator(BlockPos pos) {
        super(Messages.INSTANCE, WIDTH, HEIGHT, ManualEntry.EMPTY);
        this.pos = pos;
    }

    @Override
    public void init() {
        super.init();

        chapterTextField = textfield(70, 10, WIDTH-80, 20).event(this::update).addTextEnterEvent(this::updateAndExit);
        messageTextField = textfield(70, 32, WIDTH-80, 20).event(this::update).addTextEnterEvent(this::updateAndExit);
        range = new ScrollableLabel().hint(70, 54, 30, 20).event(i -> update("")).realMinimum(1).realMaximum(50).name("range");
        rangeSlider = slider(105, 54, WIDTH-80-35, 20).horizontal().scrollableName("range");
        onActivateButton = new ToggleButton().hint(70, 76, WIDTH-80, 20).event(() -> update("")).checkMarker(true);

        TileEntity be = Minecraft.getInstance().level.getBlockEntity(pos);
        if (be instanceof StoryAnchorTile) {
            StoryAnchorTile tile = (StoryAnchorTile) be;
            tile.getChapter().ifPresent(c -> chapterTextField.text(c));
            tile.getMessage().ifPresent(c -> messageTextField.text(c));
            range.setGenericValue(tile.getRange());
            onActivateButton.pressed(tile.isOnActivate());
        }

        Panel toplevel = positional().filledRectThickness(2).children(
                label(10, 10, 55, 20, "Chapter:"), chapterTextField,
                label(10, 32, 55, 20, "Message:"), messageTextField,
                label(10, 54, 55, 20, "Range:"), range, rangeSlider,
                label(10, 76, 55, 20, "Activate:"), onActivateButton
                );

        int x = (this.width - xSize) / 2;
        int y = (this.height - ySize) / 2;
        toplevel.bounds(x, y, xSize, ySize);
        window = new Window(this, toplevel);

        minecraft.keyboardHandler.setSendRepeatsToGui(true);
    }

    private void updateAndExit(String c) {
        update(c);
        onClose();
    }

    private void update(String _) {
        if (range == null || onActivateButton == null) {
            return;
        }
        Messages.INSTANCE.sendToServer(new PacketUpdateKnowledge(pos,
                chapterTextField.getText(), messageTextField.getText(),
                range.getRealValue(), onActivateButton.isPressed()));
    }

    @Override
    public void mouseMoved(double xx, double yy) {
        // If not initialized yet we do nothing
        if (window == null) {
            return;
        }
        window.mouseDragged(xx, yy, 0); // @todo 1.14 is this right? What button?
    }


    @Override
    public boolean mouseClicked(double x, double y, int button) {
        // If not initialized yet we do nothing
        if (window == null) {
            return false;
        }
        return super.mouseClicked(x, y, button);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        // If not initialized yet we do nothing
        if (window == null) {
            return false;
        }
        return super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        // If not initialized yet we do nothing
        if (window == null) {
            return false;
        }
        boolean rc = false;
        if (!window.keyTyped(keyCode, scanCode)) {
            rc = super.keyPressed(keyCode, scanCode, modifiers);
        }
        return rc;
    }

    @Override
    public Window getWindow() {
        return window;
    }

    @Override
    public void keyTypedFromEvent(int keyCode, int scanCode) {
        if (window != null) {
            if (window.keyTyped(keyCode, scanCode)) {
                super.keyPressed(keyCode, scanCode, 0); // @todo 1.14: modifiers?
            }
        }
    }

    @Override
    public void charTypedFromEvent(char codePoint) {
        if (window != null) {
            if (window.charTyped(codePoint)) {
                super.charTyped(codePoint, 0); // @todo 1.14: modifiers?
            }
        }
    }

    @Override
    public boolean mouseClickedFromEvent(double x, double y, int button) {
        WindowManager manager = getWindow().getWindowManager();
//        manager.mouseClicked(x, y, button);
        return true;
    }

    @Override
    public boolean mouseReleasedFromEvent(double x, double y, int button) {
        WindowManager manager = getWindow().getWindowManager();
//        manager.mouseReleased(x, y, button);
        return true;
    }

    @Override
    public boolean mouseScrolledFromEvent(double x, double y, double amount) {
        WindowManager manager = getWindow().getWindowManager();
        manager.mouseScrolled(x, y, amount);
        return true;
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float par3) {
        super.render(matrixStack, mouseX, mouseY, par3);
        drawWindow(matrixStack);
    }

    public static void open(BlockPos pos) {
        Minecraft.getInstance().setScreen(new GuiConfigurator(pos));
    }
}
