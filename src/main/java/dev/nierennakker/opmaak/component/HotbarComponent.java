package dev.nierennakker.opmaak.component;

import com.mojang.blaze3d.matrix.MatrixStack;
import dev.nierennakker.opmaak.api.IComponent;
import dev.nierennakker.opmaak.api.IOpmaakAPI;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.IngameGui;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public class HotbarComponent extends AbstractGui implements IComponent {
    @Override
    public ResourceLocation getID() {
        return new ResourceLocation(IOpmaakAPI.MOD_ID, "hotbar");
    }

    @Override
    public ITextComponent getName() {
        return new TranslationTextComponent("component.hotbar");
    }

    @Override
    public void render(MatrixStack stack, CompoundNBT nbt, PlayerEntity player, int x, int y, float delta) {
        Minecraft mc = Minecraft.getInstance();

        mc.getTextureManager().bind(IngameGui.WIDGETS_LOCATION);

        this.blit(stack, x, y, 0, 0, 182, 22);
        this.blit(stack, x - 1 + player.inventory.selected * 20, y - 1, 0, 22, 24, 22);

        for (int i = 0; i < 9; i++) {
            int offsetX = x + i * 20 + 3;
            int offsetY = y + 3;

            mc.gui.renderSlot(offsetX, offsetY, delta, player, player.inventory.items.get(i));
        }
    }

    @Override
    public int getWidth(CompoundNBT nbt) {
        return 182;
    }

    @Override
    public int getHeight(CompoundNBT nbt) {
        return 22;
    }
}
