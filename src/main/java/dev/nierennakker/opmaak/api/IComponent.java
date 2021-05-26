package dev.nierennakker.opmaak.api;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;

public interface IComponent {
    ResourceLocation getID();

    default ElementType getType() {
        return ElementType.HOTBAR;
    }

    void render(MatrixStack stack, int x, int y, float delta);
}
