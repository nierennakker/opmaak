package dev.nierennakker.opmaak.impl;

import dev.nierennakker.opmaak.api.setting.WidgetStorage;
import dev.nierennakker.opmaak.api.setting.WidgetSetting;
import net.minecraft.nbt.CompoundTag;

public class WidgetStorageImpl implements WidgetStorage {
    private final CompoundTag compoundTag;

    public WidgetStorageImpl(CompoundTag compoundTag) {
        this.compoundTag = compoundTag;
    }

    @Override
    public <T> T get(WidgetSetting<T> setting) {
        if (!this.compoundTag.contains(setting.getID())) {
            this.reset(setting);
        }

        var tag = this.compoundTag.get(setting.getID());

        return setting.getWriter().fromTag(tag);
    }

    @Override
    public <T> void set(WidgetSetting<T> setting, T value) {
        this.compoundTag.put(setting.getID(), setting.getWriter().toTag(value));
    }

    @Override
    public <T> void reset(WidgetSetting<T> setting) {
        this.set(setting, setting.getDefaultValue());
    }
}
