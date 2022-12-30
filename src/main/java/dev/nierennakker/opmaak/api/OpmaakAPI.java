package dev.nierennakker.opmaak.api;

import dev.nierennakker.opmaak.api.setting.WidgetStorage;
import net.minecraftforge.client.gui.overlay.NamedGuiOverlay;

import java.util.List;

public interface OpmaakAPI {
    String MOD_ID = "opmaak";
    String API_METHOD = "api";

    void registerWidget(Widget widget);

    List<Widget> getWidgets();

    List<Widget> getWidgetsForOverlay(NamedGuiOverlay overlay);

    WidgetStorage getWidgetStorage(Widget widget);

    void writeStorage();
}
