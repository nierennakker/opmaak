package dev.nierennakker.opmaak.impl;

import dev.nierennakker.opmaak.Opmaak;
import dev.nierennakker.opmaak.api.IComponent;
import dev.nierennakker.opmaak.api.IOpmaakAPI;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public enum OpmaakAPI implements IOpmaakAPI {
    INSTANCE;

    private final List<IComponent> components = new ArrayList<>();

    @Override
    public void registerComponent(IComponent component) {
        if (this.components.stream().anyMatch((c) -> c.getID().equals(component.getID()))) {
            Opmaak.LOGGER.warn("Duplicate id found for component " + component.getID());

            return;
        }

        this.components.add(component);
    }

    @Override
    public List<IComponent> getComponentsForType(ElementType type) {
        return this.components.stream().filter((c) -> c.getType() == type).collect(Collectors.toList());
    }
}
