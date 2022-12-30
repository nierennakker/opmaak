package dev.nierennakker.opmaak.screen.component;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.nierennakker.opmaak.api.Widget;
import dev.nierennakker.opmaak.api.setting.WidgetSetting;
import dev.nierennakker.opmaak.api.setting.WidgetStorage;
import dev.nierennakker.opmaak.impl.OpmaakAPIImpl;
import dev.nierennakker.opmaak.screen.WidgetScreen;
import dev.nierennakker.opmaak.util.Alignment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import org.lwjgl.glfw.GLFW;

public class WidgetComponent extends AbstractButton {
    private final Widget widget;

    private int offsetX = -1;
    private int offsetY = -1;

    public WidgetComponent(Widget widget, int x, int y) {
        super(x, y, 0, 0, widget.getName());

        this.widget = widget;
    }

    @Override
    public void renderButton(PoseStack stack, int mouseX, int mouseY, float delta) {
        var mc = Minecraft.getInstance();
        var player = mc.gui.getCameraPlayer();
        var storage = OpmaakAPIImpl.INSTANCE.getWidgetStorage(this.widget);

        this.width = this.widget.getWidth(storage);
        this.height = this.widget.getHeight(storage);

        var x = this.getX();
        var y = this.getY();

        GuiComponent.fill(stack, x - 1, y - 1, x + this.width + 1, y + this.height + 1, 0x60000000);

        if (this.isHovered || this.isFocused()) {
            this.vLine(stack, x - 1, y - 2, y + this.height + 1, 0xffffffff);
            this.vLine(stack, x + this.width, y - 2, y + this.height + 1, 0xffffffff);
            this.hLine(stack, x, x + this.width - 1, y - 1, 0xffffffff);
            this.hLine(stack, x, x + this.width - 1, y + this.height, 0xffffffff);
        }

        this.widget.render(stack, storage, player, x, y, delta);

        if (this.isHovered || this.isFocused()) {
            mc.font.drawShadow(stack, this.getMessage(), x, y <= 10 ? y + this.height + 3 : y - 10, 0xffffffff);
        }
    }

    @Override
    public void updateWidgetNarration(NarrationElementOutput output) {
        this.defaultButtonNarrationText(output);
    }

    @Override
    public void onPress() {
        Minecraft.getInstance().pushGuiLayer(new WidgetScreen(this.widget));
    }

    @Override
    public void onClick(double mouseX, double mouseY) {
        this.offsetX = (int) (this.getX() - mouseX);
        this.offsetY = (int) (this.getY() - mouseY);
    }

    @Override
    public void onDrag(double mouseX, double mouseY, double dragX, double dragY) {
        this.setX((int) (mouseX + this.offsetX));
        this.setY((int) (mouseY + this.offsetY));
    }

    @Override
    public void onRelease(double mouseX, double mouseY) {
        if (this.offsetX == -1 || this.offsetY == -1) {
            return;
        }

        var storage = OpmaakAPIImpl.INSTANCE.getWidgetStorage(this.widget);
        var position = Alignment.toAbsolute(storage);

        if (position != null && this.getX() == position.getA() && this.getY() == position.getB()) {
            this.onPress();
        }

        this.savePosition(storage);

        this.offsetX = -1;
        this.offsetY = -1;
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (this.active && this.visible) {
            var storage = OpmaakAPIImpl.INSTANCE.getWidgetStorage(this.widget);

            if (keyCode == GLFW.GLFW_KEY_UP) {
                this.setY(this.getY() - 1);
            } else if (keyCode == GLFW.GLFW_KEY_DOWN) {
                this.setY(this.getY() + 1);
            } else if (keyCode == GLFW.GLFW_KEY_LEFT) {
                this.setX(this.getX() - 1);
            } else if (keyCode == GLFW.GLFW_KEY_RIGHT) {
                this.setX(this.getX() + 1);
            }

            this.savePosition(storage);
        }

        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    private void savePosition(WidgetStorage storage) {
        var position = Alignment.toRelative(storage.get(WidgetSetting.ALIGNMENT), this.getX(), this.getY());

        if (position == null) {
            return;
        }

        storage.set(WidgetSetting.X, position.getA());
        storage.set(WidgetSetting.Y, position.getB());
    }
}
