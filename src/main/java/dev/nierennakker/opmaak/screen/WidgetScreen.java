package dev.nierennakker.opmaak.screen;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.serialization.Codec;
import dev.nierennakker.opmaak.api.Widget;
import dev.nierennakker.opmaak.api.setting.WidgetSetting;
import dev.nierennakker.opmaak.api.setting.WidgetStorage;
import dev.nierennakker.opmaak.impl.OpmaakAPIImpl;
import net.minecraft.client.OptionInstance;
import net.minecraft.client.Options;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.OptionsList;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraftforge.client.gui.overlay.ForgeGui;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public class WidgetScreen extends Screen {
    private final Widget widget;

    @Nullable
    private OptionsList list;

    public WidgetScreen(Widget widget) {
        super(widget.getName().plainCopy().append(" ").append(Component.translatable("screen.opmaak.settings")));

        this.widget = widget;
    }

    @Override
    public void init() {
        var storage = OpmaakAPIImpl.INSTANCE.getWidgetStorage(this.widget);

        this.list = new OptionsList(this.minecraft, this.width, this.height, 32, this.height - 32, 25);

        this.list.addSmall(Arrays.stream(this.widget.getSettings())
                .filter((w) -> w.getOptions() != null)
                .map((w) -> this.getOption(storage, w))
                .toArray(OptionInstance[]::new));

        this.addWidget(this.list);
    }

    private <T> OptionInstance<T> getOption(WidgetStorage storage, WidgetSetting<T> setting) {
        var options = setting.getOptions();
        var value = storage.get(setting);

        return new OptionInstance<T>(setting.getName(), OptionInstance.noTooltip(), (component, selected) -> Component.literal(selected.toString()), new OptionInstance.Enum<>(new ArrayList<>(options.keySet()), Codec.STRING.xmap((input) -> {
            return (T) options.keySet().stream().findAny();
        }, Object::toString)), value, (v) -> {});
    }

    @Override
    public void render(PoseStack stack, int mouseX, int mouseY, float delta) {
        if (this.minecraft.gui instanceof ForgeGui gui) {
            gui.setupOverlayRenderState(true, false);
        }

        this.renderBackground(stack);
        this.list.render(stack, mouseX, mouseY, delta);

        GuiComponent.drawCenteredString(stack, this.font, this.title, this.width / 2, 5, 0xffffffff);

        super.render(stack, mouseX, mouseY, delta);
    }
}
