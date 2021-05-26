package dev.nierennakker.opmaak.api;

import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;

import java.util.List;

public interface IOpmaakAPI {
    String MOD_ID = "opmaak";
    String API_METHOD = "api";

    void registerComponent(IComponent component);

    List<IComponent> getComponentsForType(ElementType type);
}
