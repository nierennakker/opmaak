package dev.nierennakker.opmaak;

import dev.nierennakker.opmaak.api.IComponentScreen;
import dev.nierennakker.opmaak.api.IOpmaakAPI;
import dev.nierennakker.opmaak.impl.OpmaakAPI;
import dev.nierennakker.opmaak.screen.OpmaakScreen;
import dev.nierennakker.opmaak.util.Alignment;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = IOpmaakAPI.MOD_ID, value = Dist.CLIENT)
public class Listener {
    @SubscribeEvent
    public static void renderOverlay(RenderGameOverlayEvent.PreLayer event) {
        var mc = Minecraft.getInstance();
        var player = mc.gui.getCameraPlayer();
        var components = OpmaakAPI.INSTANCE.getComponentsForOverlay(event.getOverlay());

        if (player == null || components.isEmpty()) {
            return;
        }

        event.setCanceled(true);

        for (var component : components) {
            if (mc.screen instanceof IComponentScreen) {
                if (!((IComponentScreen) mc.screen).displayComponent(component)) {
                    continue;
                }
            }

            var nbt = OpmaakAPI.INSTANCE.getComponentStorage(component);
            var position = Alignment.toAbsolute(nbt.getString("alignment"), nbt.getInt("x"), nbt.getInt("y"));

            if (position == null) {
                continue;
            }

            component.render(event.getMatrixStack(), nbt, player, position.getA(), position.getB(), event.getPartialTicks());
        }
    }

    @SubscribeEvent
    public static void keyPress(InputEvent.KeyInputEvent event) {
        if (Opmaak.KEY.consumeClick()) {
            Minecraft.getInstance().setScreen(new OpmaakScreen());
        }
    }
}
