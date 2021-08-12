package dev.nierennakker.opmaak.api;

import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.client.gui.IIngameOverlay;

import java.util.List;

public interface IOpmaakAPI {
    String MOD_ID = "opmaak";
    String API_METHOD = "api";

    void registerComponent(IComponent component);

    List<IComponent> getComponents();

    List<IComponent> getComponentsForOverlay(IIngameOverlay overlay);

    CompoundTag getComponentStorage(IComponent component);

    void writeStorage();
}
