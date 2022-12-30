package dev.nierennakker.opmaak.api.setting;

import dev.nierennakker.opmaak.util.Alignment;
import net.minecraft.network.chat.Component;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.stream.Collectors;

public interface WidgetSetting<T> {
    WidgetSetting<Boolean> ENABLED = new WidgetSetting<>() {
        @Override
        public String getID() {
            return "enabled";
        }

        @Override
        public String getName() {
            return "setting.enabled";
        }

        @Override
        public SettingWriter<Boolean> getWriter() {
            return SettingWriter.BOOLEAN;
        }

        @Override
        public Boolean getDefaultValue() {
            return true;
        }

        @Override
        public Map<? extends Boolean, Component> getOptions() {
            return Map.of(
                    true, Component.translatable("setting.enabled.true"),
                    false, Component.translatable("setting.enabled.false")
            );
        }
    };
    WidgetSetting<Integer> X = new WidgetSetting<>() {
        @Override
        public String getID() {
            return "x";
        }

        @Override
        public String getName() {
            return "setting.x";
        }

        @Override
        public SettingWriter<Integer> getWriter() {
            return SettingWriter.INTEGER;
        }

        @Override
        public Integer getDefaultValue() {
            return 0;
        }
    };
    WidgetSetting<Integer> Y = new WidgetSetting<>() {
        @Override
        public String getID() {
            return "y";
        }

        @Override
        public String getName() {
            return "setting.y";
        }

        @Override
        public SettingWriter<Integer> getWriter() {
            return SettingWriter.INTEGER;
        }

        @Override
        public Integer getDefaultValue() {
            return 0;
        }
    };
    WidgetSetting<String> ALIGNMENT = new WidgetSetting<>() {
        @Override
        public String getID() {
            return "alignment";
        }

        @Override
        public String getName() {
            return "setting.alignment";
        }

        @Override
        public SettingWriter<String> getWriter() {
            return SettingWriter.STRING;
        }

        @Override
        public String getDefaultValue() {
            return Alignment.ALIGNMENTS.get(4);
        }

        @Override
        public Map<? extends String, Component> getOptions() {
            return Alignment.ALIGNMENTS.stream()
                    .collect(Collectors.toMap((a) -> a, (a) -> Component.translatable("setting.alignment." + a)));
        }
    };

    String getID();

    String getName();

    SettingWriter<T> getWriter();

    T getDefaultValue();

    @Nullable
    default Map<? extends T, Component> getOptions() {
        return null;
    }
}
