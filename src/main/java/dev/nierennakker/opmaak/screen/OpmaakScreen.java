package dev.nierennakker.opmaak.screen;

import com.mojang.blaze3d.matrix.MatrixStack;
import dev.nierennakker.opmaak.api.IComponent;
import dev.nierennakker.opmaak.api.IComponentScreen;
import dev.nierennakker.opmaak.impl.OpmaakAPI;
import dev.nierennakker.opmaak.util.Alignment;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Tuple;
import net.minecraft.util.text.TranslationTextComponent;

public class OpmaakScreen extends Screen implements IComponentScreen {
    public OpmaakScreen() {
        super(new TranslationTextComponent("screen.rearrange"));
    }

    @Override
    public void init() {
        for (IComponent component : OpmaakAPI.INSTANCE.getComponents()) {
            CompoundNBT nbt = OpmaakAPI.INSTANCE.getComponentStorage(component);
            Tuple<Integer, Integer> position = Alignment.toAbsolute(nbt.getString("alignment"), nbt.getInt("x"), nbt.getInt("y"));

            if (position == null) {
                continue;
            }

            this.addButton(new ComponentWidget(component, position.getA(), position.getB()));
        }
    }

    @Override
    public void render(MatrixStack stack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(stack);

        super.render(stack, mouseX, mouseY, partialTicks);
    }

    @Override
    public void onClose() {
        OpmaakAPI.INSTANCE.writeStorage();

        super.onClose();
    }
}
