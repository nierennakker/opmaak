package dev.nierennakker.opmaak.components;

import com.mojang.blaze3d.matrix.MatrixStack;
import dev.nierennakker.opmaak.api.IComponent;
import dev.nierennakker.opmaak.api.IOpmaakAPI;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.IngameGui;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class OffhandComponent extends AbstractGui implements IComponent {
    @Override
    public ResourceLocation getID() {
        return new ResourceLocation(IOpmaakAPI.MOD_ID, "offhand");
    }

    @Override
    public void render(MatrixStack stack, Minecraft mc, PlayerEntity player, int x, int y, float delta) {
        ItemStack item = player.getOffhandItem();

        if (item.isEmpty()) {
            return;
        }

        mc.getTextureManager().bind(IngameGui.WIDGETS_LOCATION);

        this.blit(stack, x, y - 1, 24, 22, 29, 24);
        mc.gui.renderSlot(x + 3, y + 3, delta, player, item);
    }
}
