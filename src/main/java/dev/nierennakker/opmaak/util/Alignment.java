package dev.nierennakker.opmaak.util;

import com.google.common.collect.ImmutableList;
import net.minecraft.client.MainWindow;
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
    public static Tuple<Integer, Integer> toAbsolute(String align, int x, int y) {
        return Alignment.convert(align, x, y, false);
    }

    @Nullable
    public static Tuple<Integer, Integer> toRelative(int x, int y, String align) {
        return Alignment.convert(align, x, y, true);
    }

    @Nullable
    private static Tuple<Integer, Integer> convert(String align, int x, int y, boolean relative) {
        if (!Alignment.ALIGNMENTS.contains(align)) {
            return null;
        }

        MainWindow window = Minecraft.getInstance().getWindow();
        int width = window.getGuiScaledWidth();
        int height = window.getGuiScaledHeight();
        String[] parts = align.split("-");

        int a;

        switch (parts[1]) {
            case "left":
                a = x;
                break;
            case "right":
                a = relative ? x - width : x + width;
                break;
            default:
                int half = width / 2;
                a = relative ? x - half : x + half;
                break;
        }

        int b;

        switch (parts[0]) {
            case "top":
                b = y;
                break;
            case "bottom":
                b = relative ? y - height : y + height;
                break;
            default:
                int half = height / 2;
                b = relative ? y - half : y + half;
                break;
        }

        return new Tuple<>(a, b);
    }
}
