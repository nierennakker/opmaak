package dev.nierennakker.opmaak.widget;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import dev.nierennakker.opmaak.api.OpmaakAPI;
import dev.nierennakker.opmaak.api.Widget;
import dev.nierennakker.opmaak.api.setting.WidgetSetting;
import dev.nierennakker.opmaak.api.setting.WidgetStorage;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;

public class HotbarWidget extends GuiComponent implements Widget {
    @Override
    public ResourceLocation getID() {
        return new ResourceLocation(OpmaakAPI.MOD_ID, "hotbar");
    }

    @Override
    public Component getName() {
        return Component.translatable("widget.hotbar");
    }

    @Override
    public VanillaGuiOverlay getOverlay() {
        return VanillaGuiOverlay.HOTBAR;
    }

    @Override
    public void render(PoseStack stack, WidgetStorage storage, Player player, int x, int y, float delta) {
        var mc = Minecraft.getInstance();

        RenderSystem.setShaderTexture(0, Gui.WIDGETS_LOCATION);

        this.blit(stack, x, y, 0, 0, 182, 22);
        this.blit(stack, x - 1 + player.getInventory().selected * 20, y - 1, 0, 22, 24, 22);

        for (int i = 0; i < 9; i++) {
            int offsetX = x + i * 20 + 3;
            int offsetY = y + 3;

            mc.gui.renderSlot(offsetX, offsetY, delta, player, player.getInventory().items.get(i), i + 1);
        }
    }

    @Override
    public int getWidth(WidgetStorage storage) {
        return 182;
    }

    @Override
    public int getHeight(WidgetStorage storage) {
        return 22;
    }

    @Override
    public void writeDefaultStorage(WidgetStorage storage) {
        storage.set(WidgetSetting.X, this.getWidth(storage) / -2);
        storage.set(WidgetSetting.Y, -this.getHeight(storage));
        storage.set(WidgetSetting.ALIGNMENT, "bottom-center");
    }
}
