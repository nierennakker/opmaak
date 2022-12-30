package dev.nierennakker.opmaak.widget;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import dev.nierennakker.opmaak.api.OpmaakAPI;
import dev.nierennakker.opmaak.api.Widget;
import dev.nierennakker.opmaak.api.setting.SettingWriter;
import dev.nierennakker.opmaak.api.setting.WidgetSetting;
import dev.nierennakker.opmaak.api.setting.WidgetStorage;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;

import java.util.Map;

public class OffhandWidget extends GuiComponent implements Widget {
    public static final WidgetSetting<Visibility> VISIBILITY = new WidgetSetting<>() {
        @Override
        public String getID() {
            return "visibility";
        }

        @Override
        public String getName() {
            return "setting.visibility";
        }

        @Override
        public SettingWriter<Visibility> getWriter() {
            return new SettingWriter<>() {
                @Override
                public Visibility fromTag(Tag tag) {
                    return Visibility.valueOf(tag.getAsString());
                }

                @Override
                public Tag toTag(Visibility value) {
                    return StringTag.valueOf(value.name());
                }
            };
        }

        @Override
        public Visibility getDefaultValue() {
            return Visibility.AUTOMATIC;
        }

        @Override
        public Map<? extends Visibility, Component> getOptions() {
            return Map.of(
                    Visibility.ALWAYS, Component.translatable("setting.visibility.always"),
                    Visibility.AUTOMATIC, Component.translatable("setting.visibility.automatic")
            );
        }
    };

    public enum Visibility {
        ALWAYS,
        AUTOMATIC
    }

    @Override
    public ResourceLocation getID() {
        return new ResourceLocation(OpmaakAPI.MOD_ID, "offhand");
    }

    @Override
    public Component getName() {
        return Component.translatable("widget.offhand");
    }

    @Override
    public VanillaGuiOverlay getOverlay() {
        return VanillaGuiOverlay.HOTBAR;
    }

    @Override
    public WidgetSetting<?>[] getSettings() {
        return new WidgetSetting[]{
                OffhandWidget.VISIBILITY
        };
    }

    @Override
    public void render(PoseStack stack, WidgetStorage storage, Player player, int x, int y, float delta) {
        var mc = Minecraft.getInstance();
        var item = player.getOffhandItem();

        if (item.isEmpty()) {
            return;
        }

        RenderSystem.setShaderTexture(0, Gui.WIDGETS_LOCATION);

        this.blit(stack, x, y - 1, 24, 22, 29, 24);
        mc.gui.renderSlot(x + 3, y + 3, delta, player, item, 10);
    }

    @Override
    public int getWidth(WidgetStorage storage) {
        return 22;
    }

    @Override
    public int getHeight(WidgetStorage storage) {
        return 22;
    }
}
