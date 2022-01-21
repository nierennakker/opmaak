package dev.nierennakker.opmaak.impl;

import com.google.common.collect.ImmutableList;
import dev.nierennakker.opmaak.Opmaak;
import dev.nierennakker.opmaak.api.Widget;
import dev.nierennakker.opmaak.api.WidgetScreen;
import dev.nierennakker.opmaak.api.OpmaakAPI;
import dev.nierennakker.opmaak.util.Alignment;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtIo;
import net.minecraftforge.client.gui.OverlayRegistry;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public enum OpmaakAPIImpl implements OpmaakAPI {
    INSTANCE;

    private final CompoundTag nbt;
    private final List<Widget> widgets = new ArrayList<>();

    OpmaakAPIImpl() {
        var nbt = new CompoundTag();

        if (Opmaak.CONFIG.exists()) {
            try {
                nbt = NbtIo.readCompressed(Opmaak.CONFIG);
            } catch (IOException e) {
                Opmaak.LOGGER.warn(e);
            }
        }

        this.nbt = nbt;
    }

    @Override
    public void registerWidget(Widget widget) {
        if (this.widgets.stream().anyMatch((c) -> c.getID().equals(widget.getID()))) {
            Opmaak.LOGGER.warn("Duplicate id found for widget " + widget.getID());

            return;
        }

        OverlayRegistry.enableOverlay(widget.getOverlay(), false);
        OverlayRegistry.registerOverlayAbove(widget.getOverlay(), widget.getID().toString(), (gui, stack, delta, width, height) -> {
            var mc = Minecraft.getInstance();
            var player = mc.gui.getCameraPlayer();

            if (mc.screen instanceof WidgetScreen && !((WidgetScreen) mc.screen).displayWidget(widget)) {
                return;
            }

            var nbt = OpmaakAPIImpl.INSTANCE.getWidgetStorage(widget);
            var position = Alignment.toAbsolute(nbt.getString("alignment"), nbt.getInt("x"), nbt.getInt("y"));

            if (position == null) {
                return;
            }

            widget.render(stack, nbt, player, position.getA(), position.getB(), delta);
        });

        this.widgets.add(widget);
    }

    @Override
    public List<Widget> getWidgets() {
        return ImmutableList.copyOf(this.widgets);
    }

    @Override
    public CompoundTag getWidgetStorage(Widget widget) {
        var compound = this.nbt.getCompound("widgets");
        var key = widget.getID().toString();

        if (!compound.contains(key)) {
            CompoundTag nbt = new CompoundTag();

            nbt.putInt("x", 0);
            nbt.putInt("y", 0);
            nbt.putString("alignment", Alignment.ALIGNMENTS.get(4));
            widget.writeDefaultStorage(nbt);
            compound.put(key, nbt);

            this.nbt.put("widgets", compound);
        }

        return compound.getCompound(key);
    }

    @Override
    public void writeStorage() {
        try {
            Opmaak.CONFIG.createNewFile();
            NbtIo.writeCompressed(this.nbt, Opmaak.CONFIG);
        } catch (IOException e) {
            Opmaak.LOGGER.warn(e);
        }
    }
}
