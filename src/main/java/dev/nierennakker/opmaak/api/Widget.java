package dev.nierennakker.opmaak.api;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.client.gui.IIngameOverlay;

import javax.annotation.Nullable;

public interface Widget {
    ResourceLocation getID();

    Component getName();

    @Nullable
    default IIngameOverlay getOverlay() {
        return null;
    }

    void render(PoseStack stack, CompoundTag nbt, Player player, int x, int y, float delta);

    int getWidth(CompoundTag nbt);

    int getHeight(CompoundTag nbt);

    default void writeDefaultStorage(CompoundTag nbt) {
    }
}
