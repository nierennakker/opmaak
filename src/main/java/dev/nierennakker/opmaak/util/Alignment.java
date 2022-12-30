package dev.nierennakker.opmaak.util;

import com.google.common.collect.ImmutableList;
import dev.nierennakker.opmaak.api.setting.WidgetSetting;
import dev.nierennakker.opmaak.api.setting.WidgetStorage;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Tuple;

import javax.annotation.Nullable;
import java.util.List;

public class Alignment {
    public static final List<String> ALIGNMENTS = ImmutableList.of(
            "top-left", "top-center", "top-right",
            "middle-left", "middle-center", "middle-right",
            "bottom-left", "bottom-center", "bottom-right"
    );

    @Nullable
    public static Tuple<Integer, Integer> toAbsolute(WidgetStorage storage) {
        var alignment = storage.get(WidgetSetting.ALIGNMENT);
        var x = storage.get(WidgetSetting.X);
        var y = storage.get(WidgetSetting.Y);

        return Alignment.toAbsolute(alignment, x, y);
    }

    @Nullable
    public static Tuple<Integer, Integer> toAbsolute(String alignment, int x, int y) {
        return Alignment.convert(alignment, x, y, false);
    }

    @Nullable
    public static Tuple<Integer, Integer> toRelative(WidgetStorage storage) {
        var alignment = storage.get(WidgetSetting.ALIGNMENT);
        var x = storage.get(WidgetSetting.X);
        var y = storage.get(WidgetSetting.Y);

        return Alignment.toRelative(alignment, x, y);
    }

    @Nullable
    public static Tuple<Integer, Integer> toRelative(String alignment, int x, int y) {
        return Alignment.convert(alignment, x, y, true);
    }

    @Nullable
    private static Tuple<Integer, Integer> convert(String alignment, int x, int y, boolean relative) {
        if (!Alignment.ALIGNMENTS.contains(alignment)) {
            return null;
        }

        var window = Minecraft.getInstance().getWindow();
        var width = window.getGuiScaledWidth();
        var height = window.getGuiScaledHeight();
        var parts = alignment.split("-");

        var a = switch (parts[1]) {
            case "left" -> x;
            case "right" -> relative ? x - width : x + width;
            default -> relative ? x - width / 2 : x + width / 2;
        };

        var b = switch (parts[0]) {
            case "top" -> y;
            case "bottom" -> relative ? y - height : y + height;
            default -> relative ? y - height / 2 : y + height / 2;
        };

        return new Tuple<>(a, b);
    }
}
