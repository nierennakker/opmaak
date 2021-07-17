package dev.nierennakker.opmaak.components;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import dev.nierennakker.opmaak.api.IComponent;
import dev.nierennakker.opmaak.api.IOpmaakAPI;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.IngameGui;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;

public class HotbarComponent extends AbstractGui implements IComponent {
    @Override
    public ResourceLocation getID() {
        return new ResourceLocation(IOpmaakAPI.MOD_ID, "hotbar");
    }

    @Override
    public void render(MatrixStack stack, PlayerEntity player, int x, int y, float delta) {
        Minecraft mc = Minecraft.getInstance();

        RenderSystem.color4f(1.0f, 1.0f, 1.0f, 1.0f);
        RenderSystem.enableRescaleNormal();
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();

        mc.getTextureManager().bind(IngameGui.WIDGETS_LOCATION);

        this.blit(stack, x, y, 0, 0, 182, 22);
        this.blit(stack, x - 1 + player.inventory.selected * 20, y - 1, 0, 22, 24, 22);

        for (int i = 0; i < 9; i++) {
            int offsetX = x + i * 20 + 3;
            int offsetY = y + 3;

            mc.gui.renderSlot(offsetX, offsetY, delta, player, player.inventory.items.get(i));
        }

        RenderSystem.disableRescaleNormal();
        RenderSystem.disableBlend();
    }
}
