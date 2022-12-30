package dev.nierennakker.opmaak.impl;

import com.google.common.collect.ImmutableList;
import dev.nierennakker.opmaak.Opmaak;
import dev.nierennakker.opmaak.api.OpmaakAPI;
import dev.nierennakker.opmaak.api.Widget;
import dev.nierennakker.opmaak.api.setting.WidgetStorage;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtIo;
import net.minecraftforge.client.gui.overlay.NamedGuiOverlay;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public enum OpmaakAPIImpl implements OpmaakAPI {
    INSTANCE;

    private final CompoundTag compoundTag;
    private final List<Widget> widgets = new ArrayList<>();

    OpmaakAPIImpl() {
        var compoundTag = new CompoundTag();

        if (Opmaak.CONFIG.exists()) {
            try {
                compoundTag = NbtIo.readCompressed(Opmaak.CONFIG);
            } catch (IOException e) {
                Opmaak.LOGGER.warn(e);
            }
        }

        this.compoundTag = compoundTag;
    }

    @Override
    public void registerWidget(Widget widget) {
        if (this.getWidgets().stream().anyMatch((c) -> c.getID().equals(widget.getID()))) {
            Opmaak.LOGGER.warn("Duplicate id found for widget " + widget.getID());

            return;
        }

        this.widgets.add(widget);
    }

    @Override
    public List<Widget> getWidgets() {
        return ImmutableList.copyOf(this.widgets);
    }

    @Override
    public List<Widget> getWidgetsForOverlay(NamedGuiOverlay overlay) {
        return this.widgets.stream()
                .filter((w) -> w.getOverlay().id() == overlay.id())
                .collect(Collectors.toList());
    }

    @Override
    public WidgetStorage getWidgetStorage(Widget widget) {
        var compound = this.compoundTag.getCompound("widgets");
        var key = widget.getID().toString();

        if (!compound.contains(key)) {
            var compoundTag = new CompoundTag();
            var storage = new WidgetStorageImpl(compoundTag);

            widget.writeDefaultStorage(storage);
            compound.put(key, compoundTag);

            this.compoundTag.put("widgets", compound);
        }

        return new WidgetStorageImpl(compound.getCompound(key));
    }

    @Override
    public void writeStorage() {
        try {
            Opmaak.CONFIG.createNewFile();
            NbtIo.writeCompressed(this.compoundTag, Opmaak.CONFIG);
        } catch (IOException e) {
            Opmaak.LOGGER.warn(e);
        }
    }
}
