package dev.nierennakker.opmaak.api.setting;

import net.minecraft.nbt.*;

public interface SettingWriter<T> {
    SettingWriter<String> STRING = new SettingWriter<>() {
        @Override
        public String fromTag(Tag tag) {
            return tag.getAsString();
        }

        @Override
        public Tag toTag(String value) {
            return StringTag.valueOf(value);
        }
    };
    SettingWriter<Boolean> BOOLEAN = new SettingWriter<>() {
        @Override
        public Boolean fromTag(Tag tag) {
            return ((NumericTag) tag).getAsByte() != 0;
        }

        @Override
        public Tag toTag(Boolean value) {
            return ByteTag.valueOf(value);
        }
    };
    SettingWriter<Integer> INTEGER = new SettingWriter<>() {
        @Override
        public Integer fromTag(Tag tag) {
            return ((IntTag) tag).getAsInt();
        }

        @Override
        public Tag toTag(Integer value) {
            return IntTag.valueOf(value);
        }
    };

    T fromTag(Tag tag);

    Tag toTag(T value);
}
