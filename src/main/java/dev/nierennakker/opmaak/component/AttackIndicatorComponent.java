package dev.nierennakker.opmaak.component;

import com.mojang.blaze3d.matrix.MatrixStack;
import dev.nierennakker.opmaak.api.IOpmaakAPI;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public class AttackIndicatorComponent extends HotbarComponent {
    @Override
    public ResourceLocation getID() {
        return new ResourceLocation(IOpmaakAPI.MOD_ID, "attack_indicator");
    }

    @Override
    public ITextComponent getName() {
        return new TranslationTextComponent("component.attack_indicator");
    }

    @Override
    public void render(MatrixStack stack, CompoundNBT nbt, PlayerEntity player, int x, int y, float delta) {
        Minecraft.getInstance().getTextureManager().bind(AbstractGui.GUI_ICONS_LOCATION);

        float strength = player.getAttackStrengthScale(0.0f);

        if (strength >= 1.0f) {
            return;
        }

        int height = (int) (strength * 18.0f);

        this.blit(stack, x + 2, y + 2, 0, 94, 18, 18);
        this.blit(stack, x + 2, y + 20 - height, 18, 112 - height, 18, height);
    }

    @Override
    public int getWidth(CompoundNBT nbt) {
        return 22;
    }

    @Override
    public int getHeight(CompoundNBT nbt) {
        return 22;
    }
}
