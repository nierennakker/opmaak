package dev.nierennakker.opmaak.component;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import dev.nierennakker.opmaak.api.IOpmaakAPI;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

public class AttackIndicatorComponent extends HotbarComponent {
    @Override
    public ResourceLocation getID() {
        return new ResourceLocation(IOpmaakAPI.MOD_ID, "attack_indicator");
    }

    @Override
    public Component getName() {
        return new TranslatableComponent("component.attack_indicator");
    }

    @Override
    public void render(PoseStack stack, CompoundTag nbt, Player player, int x, int y, float delta) {
        RenderSystem.setShaderTexture(0, GuiComponent.GUI_ICONS_LOCATION);

        var strength = player.getAttackStrengthScale(0.0f);

        if (strength >= 1.0f) {
            return;
        }

        var height = (int) (strength * 18.0f);

        this.blit(stack, x + 2, y + 2, 0, 94, 18, 18);
        this.blit(stack, x + 2, y + 20 - height, 18, 112 - height, 18, height);
    }

    @Override
    public int getWidth(CompoundTag nbt) {
        return 22;
    }

    @Override
    public int getHeight(CompoundTag nbt) {
        return 22;
    }
}
