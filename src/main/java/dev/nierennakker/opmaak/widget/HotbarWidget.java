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

public class HotbarWidget extends GuiComponent implements Widget {
    @Override
    public ResourceLocation getID() {
        return new ResourceLocation(OpmaakAPI.MOD_ID, "hotbar");
    }

    @Override
    public Component getName() {
        return new TranslatableComponent("widget.hotbar");
    }

    @Override
    public IIngameOverlay getOverlay() {
        return ForgeIngameGui.HOTBAR_ELEMENT;
    }

    @Override
    public void render(PoseStack stack, CompoundTag nbt, Player player, int x, int y, float delta) {
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
    public int getWidth(CompoundTag nbt) {
        return 182;
    }

    @Override
    public int getHeight(CompoundTag nbt) {
        return 22;
    }

    @Override
    public void writeDefaultStorage(CompoundTag nbt) {
        nbt.putInt("x", this.getWidth(nbt) / -2);
        nbt.putInt("y", -this.getHeight(nbt));
        nbt.putString("alignment", "bottom-center");
    }
}
