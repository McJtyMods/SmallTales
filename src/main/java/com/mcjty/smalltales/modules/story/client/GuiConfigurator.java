package com.mcjty.smalltales.modules.story.client;

import com.mcjty.smalltales.modules.story.blocks.StoryAnchorTile;
import com.mcjty.smalltales.modules.story.network.PacketUpdateKnowledge;
import com.mcjty.smalltales.setup.Messages;
import com.mojang.blaze3d.matrix.MatrixStack;
import mcjty.lib.gui.*;
import mcjty.lib.gui.widgets.Panel;
import mcjty.lib.gui.widgets.TextField;
import mcjty.lib.gui.widgets.Widgets;
import net.minecraft.client.Minecraft;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;

import static mcjty.lib.gui.widgets.Widgets.positional;

public class GuiConfigurator extends GuiItemScreen implements IKeyReceiver {

    public static final int WIDTH = 340;
    public static final int HEIGHT = 200;

    private final BlockPos pos;
    private TextField chapterTextField;
    private TextField messageTextField;

    public GuiConfigurator(BlockPos pos) {
        super(Messages.INSTANCE, WIDTH, HEIGHT, ManualEntry.EMPTY);
        this.pos = pos;
    }

    @Override
    public void init() {
        super.init();

        chapterTextField = Widgets.textfield(70, 10, WIDTH-80, 20).event(this::update).addTextEnterEvent(this::updateAndExit);
        messageTextField = Widgets.textfield(70, 32, WIDTH-80, 20).event(this::update).addTextEnterEvent(this::updateAndExit);

        TileEntity be = Minecraft.getInstance().level.getBlockEntity(pos);
        if (be instanceof StoryAnchorTile) {
            ((StoryAnchorTile) be).getChapter().ifPresent(c -> chapterTextField.text(c));
            ((StoryAnchorTile) be).getMessage().ifPresent(c -> messageTextField.text(c));
        }

        Panel toplevel = positional().filledRectThickness(2).children(
                Widgets.label(10, 10, 55, 20, "Chapter:"), chapterTextField,
                Widgets.label(10, 32, 55, 20, "Message:"), messageTextField);

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
        Messages.INSTANCE.sendToServer(new PacketUpdateKnowledge(pos, chapterTextField.getText(), messageTextField.getText()));
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
        boolean rc = super.mouseClicked(x, y, button);
        window.mouseClicked(x, y, button);
        return rc;
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        // If not initialized yet we do nothing
        if (window == null) {
            return false;
        }
        boolean rc = super.mouseReleased(mouseX, mouseY, button);
        window.mouseReleased(mouseX, mouseY, button);
        return rc;
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
        manager.mouseClicked(x, y, button);
        return true;
    }

    @Override
    public boolean mouseReleasedFromEvent(double x, double y, int button) {
        WindowManager manager = getWindow().getWindowManager();
        manager.mouseReleased(x, y, button);
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
