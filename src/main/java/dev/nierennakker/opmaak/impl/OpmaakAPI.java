package dev.nierennakker.opmaak.impl;

import com.google.common.collect.ImmutableList;
import dev.nierennakker.opmaak.Opmaak;
import dev.nierennakker.opmaak.api.IComponent;
import dev.nierennakker.opmaak.api.IOpmaakAPI;
import dev.nierennakker.opmaak.util.Alignment;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtIo;
import net.minecraftforge.client.gui.IIngameOverlay;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

        this.components.add(component);
    }

    @Override
    public List<IComponent> getComponents() {
        return ImmutableList.copyOf(this.components);
    }

    @Override
    public List<IComponent> getComponentsForOverlay(IIngameOverlay overlay) {
        return this.components.stream().filter((c) -> c.getOverlay() == overlay).collect(Collectors.toList());
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
