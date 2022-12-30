package dev.nierennakker.opmaak.api;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.nierennakker.opmaak.api.setting.WidgetSetting;
import dev.nierennakker.opmaak.api.setting.WidgetStorage;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;

public interface Widget {
    ResourceLocation getID();

    Component getName();

    VanillaGuiOverlay getOverlay();

    default WidgetSetting<?>[] getSettings() {
        return new WidgetSetting[]{};
    }

    void render(PoseStack stack, WidgetStorage storage, Player player, int x, int y, float delta);

    int getWidth(WidgetStorage storage);

    int getHeight(WidgetStorage storage);

    default void writeDefaultStorage(WidgetStorage storage) {
    }
}
