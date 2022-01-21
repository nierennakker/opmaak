package dev.nierennakker.opmaak.widget;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import dev.nierennakker.opmaak.api.Widget;
import dev.nierennakker.opmaak.api.OpmaakAPI;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.client.gui.ForgeIngameGui;
import net.minecraftforge.client.gui.IIngameOverlay;

public class OffhandWidget extends GuiComponent implements Widget {
    @Override
    public ResourceLocation getID() {
        return new ResourceLocation(OpmaakAPI.MOD_ID, "offhand");
    }

    @Override
    public Component getName() {
        return new TranslatableComponent("widget.offhand");
    }

    @Override
    public IIngameOverlay getOverlay() {
        return ForgeIngameGui.HOTBAR_ELEMENT;
    }

    @Override
    public void render(PoseStack stack, CompoundTag nbt, Player player, int x, int y, float delta) {
        var mc = Minecraft.getInstance();
        var item = player.getOffhandItem();

        if (item.isEmpty()) {
            return;
        }

        RenderSystem.setShaderTexture(0, Gui.WIDGETS_LOCATION);

        this.blit(stack, x, y - 1, 24, 22, 29, 24);
        mc.gui.renderSlot(x + 3, y + 3, delta, player, item, 10);
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
