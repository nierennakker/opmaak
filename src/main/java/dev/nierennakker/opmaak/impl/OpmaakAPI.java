package dev.nierennakker.opmaak.impl;

import com.google.common.collect.ImmutableList;
import dev.nierennakker.opmaak.Opmaak;
import dev.nierennakker.opmaak.api.IComponent;
import dev.nierennakker.opmaak.api.IOpmaakAPI;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public enum OpmaakAPI implements IOpmaakAPI {
    INSTANCE;

    private final CompoundNBT nbt;
    private final List<IComponent> components = new ArrayList<>();

    OpmaakAPI() {
        CompoundNBT nbt = new CompoundNBT();

        if (Opmaak.CONFIG.exists()) {
            try {
                nbt = CompressedStreamTools.readCompressed(Opmaak.CONFIG);
            } catch (IOException e) {
                Opmaak.LOGGER.warn(e);
            }
        }

        this.nbt = nbt;
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
    public List<IComponent> getComponentsForType(ElementType type) {
        return this.components.stream().filter((c) -> c.getType() == type).collect(Collectors.toList());
    }

    @Override
    public CompoundNBT getComponentStorage(IComponent component) {
        CompoundNBT compound = this.nbt.getCompound("components");
        String key = component.getID().toString();

        if (!compound.contains(key)) {
            CompoundNBT nbt = new CompoundNBT();

            nbt.putInt("x", 0);
            nbt.putInt("y", 0);
            compound.put(key, nbt);

            this.nbt.put("components", compound);
        }

        return compound.getCompound(key);
    }

    @Override
    public void writeStorage() {
        try {
            Opmaak.CONFIG.createNewFile();
            CompressedStreamTools.writeCompressed(this.nbt, Opmaak.CONFIG);
        } catch (IOException e) {
            Opmaak.LOGGER.warn(e);
        }
    }
}
