package dev.nierennakker.opmaak.screen;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.nierennakker.opmaak.Opmaak;
import dev.nierennakker.opmaak.api.IComponent;
import dev.nierennakker.opmaak.impl.OpmaakAPI;
import dev.nierennakker.opmaak.util.Alignment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.nbt.CompoundTag;
import org.lwjgl.glfw.GLFW;

public class ComponentWidget extends AbstractButton {
    private final IComponent component;

    private int offsetX = -1;
    private int offsetY = -1;

    public ComponentWidget(IComponent component, int x, int y) {
        super(x, y, 0, 0, component.getName());

        this.component = component;
    }

    @Override
    public void renderButton(PoseStack stack, int mouseX, int mouseY, float delta) {
        var mc = Minecraft.getInstance();
        var player = mc.gui.getCameraPlayer();
        var nbt = OpmaakAPI.INSTANCE.getComponentStorage(this.component);

        this.width = this.component.getWidth(nbt);
        this.height = this.component.getHeight(nbt);

        GuiComponent.fill(stack, this.x - 1, this.y - 1, this.x + this.width + 1, this.y + this.height + 1, 0x60000000);

        if (this.isHovered()) {
            this.vLine(stack, this.x - 1, this.y - 2, this.y + this.height + 1, 0xffffffff);
            this.vLine(stack, this.x + this.width, this.y - 2, this.y + this.height + 1, 0xffffffff);
            this.hLine(stack, this.x, this.x + this.width - 1, this.y - 1, 0xffffffff);
            this.hLine(stack, this.x, this.x + this.width - 1, this.y + this.height, 0xffffffff);
        }

        this.component.render(stack, nbt, player, this.x, this.y, delta);

        if (this.isHovered || this.isFocused()) {
            mc.font.drawShadow(stack, this.getMessage(), this.x, this.y <= 10 ? this.y + this.height + 3 : this.y - 10, 0xffffffff);
        }
    }

    @Override
    public void updateNarration(NarrationElementOutput output) {
        this.defaultButtonNarrationText(output);
    }

    @Override
    public void onPress() {
        Opmaak.LOGGER.info("press");
    }

    @Override
    public void onClick(double mouseX, double mouseY) {
        this.offsetX = (int) (this.x - mouseX);
        this.offsetY = (int) (this.y - mouseY);
    }

    @Override
    public void onDrag(double mouseX, double mouseY, double dragX, double dragY) {
        this.x = (int) (mouseX + this.offsetX);
        this.y = (int) (mouseY + this.offsetY);
    }

    @Override
    public void onRelease(double mouseX, double mouseY) {
        if (this.offsetX == -1 || this.offsetY == -1) {
            return;
        }

        var nbt = OpmaakAPI.INSTANCE.getComponentStorage(this.component);
        var position = Alignment.toAbsolute(nbt.getString("alignment"), nbt.getInt("x"), nbt.getInt("y"));

        if (position != null && this.x == position.getA() && this.y == position.getB()) {
            this.onPress();
        }

        this.savePosition(nbt);

        this.offsetX = -1;
        this.offsetY = -1;
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (this.active && this.visible) {
            var nbt = OpmaakAPI.INSTANCE.getComponentStorage(this.component);

            if (keyCode == GLFW.GLFW_KEY_UP) {
                this.y--;
            } else if (keyCode == GLFW.GLFW_KEY_DOWN) {
                this.y++;
            } else if (keyCode == GLFW.GLFW_KEY_LEFT) {
                this.x--;
            } else if (keyCode == GLFW.GLFW_KEY_RIGHT) {
                this.x++;
            }

            this.savePosition(nbt);
        }

        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    private void savePosition(CompoundTag nbt) {
        var position = Alignment.toRelative(this.x, this.y, nbt.getString("alignment"));

        if (position == null) {
            return;
        }

        nbt.putInt("x", position.getA());
        nbt.putInt("y", position.getB());
    }
}
