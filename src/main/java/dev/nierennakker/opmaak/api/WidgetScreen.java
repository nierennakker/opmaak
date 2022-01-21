package dev.nierennakker.opmaak.api;

public interface WidgetScreen {
    default boolean displayWidget(Widget widget) {
        return false;
    }
}
