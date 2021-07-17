package dev.nierennakker.opmaak.screen;

import com.mojang.blaze3d.matrix.MatrixStack;
import dev.nierennakker.opmaak.api.IComponent;
import dev.nierennakker.opmaak.api.IComponentScreen;
import dev.nierennakker.opmaak.impl.OpmaakAPI;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.text.TranslationTextComponent;

public class OpmaakScreen extends Screen implements IComponentScreen {
    public OpmaakScreen() {
        super(new TranslationTextComponent("screen.rearrange"));
    }

    @Override
    public void init() {
        for (IComponent component : OpmaakAPI.INSTANCE.getComponents()) {
            this.addButton(new ComponentWidget(component));
        }
    }

    @Override
    public void render(MatrixStack stack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(stack);

        super.render(stack, mouseX, mouseY, partialTicks);
    }
}
