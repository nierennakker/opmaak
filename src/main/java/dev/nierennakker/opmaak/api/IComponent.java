package dev.nierennakker.opmaak.api;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;

public interface IComponent {
    ResourceLocation getID();

    ITextComponent getName();

    default ElementType getType() {
        return ElementType.HOTBAR;
    }

    void render(MatrixStack stack, Minecraft mc, PlayerEntity player, int x, int y, float delta);

    int getWidth();

    int getHeight();
}
