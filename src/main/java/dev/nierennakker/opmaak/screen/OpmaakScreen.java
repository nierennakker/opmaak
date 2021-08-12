package dev.nierennakker.opmaak.screen;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.nierennakker.opmaak.api.IComponent;
import dev.nierennakker.opmaak.api.IComponentScreen;
import dev.nierennakker.opmaak.impl.OpmaakAPI;
import dev.nierennakker.opmaak.util.Alignment;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.TranslatableComponent;

public class OpmaakScreen extends Screen implements IComponentScreen {
    public OpmaakScreen() {
        super(new TranslatableComponent("screen.rearrange"));
    }

    @Override
    public void init() {
        for (IComponent component : OpmaakAPI.INSTANCE.getComponents()) {
            var nbt = OpmaakAPI.INSTANCE.getComponentStorage(component);
            var position = Alignment.toAbsolute(nbt.getString("alignment"), nbt.getInt("x"), nbt.getInt("y"));

            if (position == null) {
                continue;
            }

            this.addRenderableWidget(new ComponentWidget(component, position.getA(), position.getB()));
        }
    }

    @Override
    public void render(PoseStack stack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(stack);

        super.render(stack, mouseX, mouseY, partialTicks);
    }

    @Override
    public void onClose() {
        OpmaakAPI.INSTANCE.writeStorage();

        super.onClose();
    }
}
