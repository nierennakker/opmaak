package dev.nierennakker.opmaak.impl;

import com.google.common.collect.ImmutableList;
import dev.nierennakker.opmaak.Opmaak;
import dev.nierennakker.opmaak.api.IComponent;
import dev.nierennakker.opmaak.api.IComponentScreen;
import dev.nierennakker.opmaak.api.IOpmaakAPI;
import dev.nierennakker.opmaak.util.Alignment;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtIo;
import net.minecraftforge.client.gui.OverlayRegistry;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public enum OpmaakAPI implements IOpmaakAPI {
    INSTANCE;

    private final CompoundTag tag;
    private final List<IComponent> components = new ArrayList<>();

    OpmaakAPI() {
        var nbt = new CompoundTag();

        if (Opmaak.CONFIG.exists()) {
            try {
                nbt = NbtIo.readCompressed(Opmaak.CONFIG);
            } catch (IOException e) {
                Opmaak.LOGGER.warn(e);
            }
        }

        this.tag = nbt;
    }

    @Override
    public void registerComponent(IComponent component) {
        if (this.components.stream().anyMatch((c) -> c.getID().equals(component.getID()))) {
            Opmaak.LOGGER.warn("Duplicate id found for component " + component.getID());

            return;
        }

        OverlayRegistry.enableOverlay(component.getOverlay(), false);
        OverlayRegistry.registerOverlayAbove(component.getOverlay(), component.getID().toString(), (gui, stack, delta, width, height) -> {
            var mc = Minecraft.getInstance();
            var player = mc.gui.getCameraPlayer();

            if (mc.screen instanceof IComponentScreen && !((IComponentScreen) mc.screen).displayComponent(component)) {
                return;
            }

            var nbt = OpmaakAPI.INSTANCE.getComponentStorage(component);
            var position = Alignment.toAbsolute(nbt.getString("alignment"), nbt.getInt("x"), nbt.getInt("y"));

            if (position == null) {
                return;
            }

            component.render(stack, nbt, player, position.getA(), position.getB(), delta);
        });

        this.components.add(component);
    }

    @Override
    public List<IComponent> getComponents() {
        return ImmutableList.copyOf(this.components);
    }

    @Override
    public CompoundTag getComponentStorage(IComponent component) {
        var compound = this.tag.getCompound("components");
        var key = component.getID().toString();

        if (!compound.contains(key)) {
            CompoundTag tag = new CompoundTag();

            tag.putInt("x", 0);
            tag.putInt("y", 0);
            tag.putString("alignment", Alignment.ALIGNMENTS.get(4));
            compound.put(key, tag);

            this.tag.put("components", compound);
        }

        return compound.getCompound(key);
    }

    @Override
    public void writeStorage() {
        try {
            Opmaak.CONFIG.createNewFile();
            NbtIo.writeCompressed(this.tag, Opmaak.CONFIG);
        } catch (IOException e) {
            Opmaak.LOGGER.warn(e);
        }
    }
}
