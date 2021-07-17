package dev.nierennakker.opmaak.screen;

import com.mojang.blaze3d.matrix.MatrixStack;
import dev.nierennakker.opmaak.api.IComponent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.entity.player.PlayerEntity;
import org.lwjgl.glfw.GLFW;

public class ComponentWidget extends Widget {
    private final IComponent component;

    private int offsetX = -1;
    private int offsetY = -1;

    public ComponentWidget(IComponent component) {
        super(0, 0, component.getWidth(), component.getHeight(), component.getName());

        this.component = component;
    }

    @Override
    public void renderButton(MatrixStack stack, int mouseX, int mouseY, float delta) {
        Minecraft mc = Minecraft.getInstance();
        PlayerEntity player = mc.gui.getCameraPlayer();

        AbstractGui.fill(stack, this.x - 1, this.y - 1, this.x + this.width + 1, this.y + this.height + 1, 0x60000000);

        if (this.isHovered()) {
            this.vLine(stack, this.x - 1, this.y - 2, this.y + this.height + 1, 0xffffffff);
            this.vLine(stack, this.x + this.width, this.y - 2, this.y + this.height + 1, 0xffffffff);
            this.hLine(stack, this.x, this.x + this.width - 1, this.y - 1, 0xffffffff);
            this.hLine(stack, this.x, this.x + this.width - 1, this.y + this.height, 0xffffffff);
        }

        this.component.render(stack, mc, player, this.x, this.y, delta);
        mc.font.drawShadow(stack, this.getMessage(), this.x, this.y - 10, 0xffffffff);
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
        this.offsetX = -1;
        this.offsetY = -1;
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (keyCode == GLFW.GLFW_KEY_UP) {
            this.y--;
        } else if (keyCode == GLFW.GLFW_KEY_DOWN) {
            this.y++;
        } else if (keyCode == GLFW.GLFW_KEY_LEFT) {
            this.x--;
        } else if (keyCode == GLFW.GLFW_KEY_RIGHT) {
            this.x++;
        }

        return false;
    }
}
