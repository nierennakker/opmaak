package dev.nierennakker.opmaak.widget;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.nierennakker.opmaak.api.OpmaakAPI;
import dev.nierennakker.opmaak.api.setting.WidgetStorage;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

public class AttackIndicatorWidget extends HotbarWidget {
    @Override
    public ResourceLocation getID() {
        return new ResourceLocation(OpmaakAPI.MOD_ID, "attack_indicator");
    }

    @Override
    public Component getName() {
        return Component.translatable("widget.attack_indicator");
    }

    @Override
    public void render(PoseStack stack, WidgetStorage storage, Player player, int x, int y, float delta) {
        var strength = player.getAttackStrengthScale(0.0f);

        if (strength >= 1.0f) {
            return;
        }

        var height = (int) (strength * 18.0f);

        this.blit(stack, x + 2, y + 2, 0, 94, 18, 18);
        this.blit(stack, x + 2, y + 20 - height, 18, 112 - height, 18, height);
    }

    @Override
    public int getWidth(WidgetStorage storage) {
        return 22;
    }

    @Override
    public int getHeight(WidgetStorage storage) {
        return 22;
    }
}
