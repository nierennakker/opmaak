package dev.nierennakker.opmaak.screen;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.nierennakker.opmaak.api.Widget;
import dev.nierennakker.opmaak.api.WidgetScreen;
import dev.nierennakker.opmaak.impl.OpmaakAPIImpl;
import dev.nierennakker.opmaak.util.Alignment;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.TranslatableComponent;

public class OpmaakScreen extends Screen implements WidgetScreen {
    public OpmaakScreen() {
        super(new TranslatableComponent("screen.rearrange"));
    }

    @Override
    public void init() {
        for (Widget widget : OpmaakAPIImpl.INSTANCE.getWidgets()) {
            var nbt = OpmaakAPIImpl.INSTANCE.getWidgetStorage(widget);
            var position = Alignment.toAbsolute(nbt.getString("alignment"), nbt.getInt("x"), nbt.getInt("y"));

            if (position == null) {
                continue;
            }

            this.addRenderableWidget(new WidgetComponent(widget, position.getA(), position.getB()));
        }
    }

    @Override
    public void render(PoseStack stack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(stack);

        super.render(stack, mouseX, mouseY, partialTicks);
    }

    @Override
    public void onClose() {
        OpmaakAPIImpl.INSTANCE.writeStorage();

        super.onClose();
    }
}
