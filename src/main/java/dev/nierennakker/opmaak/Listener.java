package dev.nierennakker.opmaak;

import dev.nierennakker.opmaak.api.OpmaakAPI;
import dev.nierennakker.opmaak.api.WidgetScreen;
import dev.nierennakker.opmaak.api.setting.WidgetSetting;
import dev.nierennakker.opmaak.impl.OpmaakAPIImpl;
import dev.nierennakker.opmaak.screen.OpmaakScreen;
import dev.nierennakker.opmaak.util.Alignment;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = OpmaakAPI.MOD_ID, value = Dist.CLIENT)
public class Listener {
    @SubscribeEvent
    public static void renderGuiOverlay(RenderGuiOverlayEvent.Pre event) {
        var mc = Minecraft.getInstance();
        var player = mc.gui.getCameraPlayer();
        var widgets = OpmaakAPIImpl.INSTANCE.getWidgetsForOverlay(event.getOverlay());

        if (player == null || widgets.isEmpty()) {
            return;
        }

        event.setCanceled(true);

        for (var widget : widgets) {
            if (mc.screen instanceof WidgetScreen screen && !screen.displayWidget(widget)) {
                continue;
            }

            var storage = OpmaakAPIImpl.INSTANCE.getWidgetStorage(widget);

            if (!storage.get(WidgetSetting.ENABLED)) {
                continue;
            }

            var position = Alignment.toAbsolute(storage);

            if (position == null) {
                continue;
            }

            if (mc.gui instanceof ForgeGui gui) {
                gui.setupOverlayRenderState(true, false);
            }

            widget.render(event.getPoseStack(), storage, player, position.getA(), position.getB(), event.getPartialTick());
        }
    }

    @SubscribeEvent
    public static void keyInput(InputEvent.Key event) {
        if (Opmaak.KEY.consumeClick()) {
            Minecraft.getInstance().setScreen(new OpmaakScreen());
        }
    }
}
