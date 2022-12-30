package dev.nierennakker.opmaak.api.setting;

public interface WidgetStorage {
    <T> T get(WidgetSetting<T> setting);

    <T> void set(WidgetSetting<T> setting, T value);

    <T> void reset(WidgetSetting<T> setting);
}
