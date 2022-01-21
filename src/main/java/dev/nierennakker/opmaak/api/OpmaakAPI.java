package dev.nierennakker.opmaak.api;

import net.minecraft.nbt.CompoundTag;

import java.util.List;

public interface OpmaakAPI {
    String MOD_ID = "opmaak";
    String API_METHOD = "api";

    void registerWidget(Widget widget);

    List<Widget> getWidgets();

    CompoundTag getWidgetStorage(Widget widget);

    void writeStorage();
}
