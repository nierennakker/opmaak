package dev.nierennakker.opmaak;

import dev.nierennakker.opmaak.api.IComponent;
import dev.nierennakker.opmaak.api.IOpmaakAPI;
import dev.nierennakker.opmaak.impl.OpmaakAPI;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;

@Mod.EventBusSubscriber(modid = IOpmaakAPI.MOD_ID, value = Dist.CLIENT)
public class Listener {
    @SubscribeEvent
    public static void renderOverlay(RenderGameOverlayEvent.Pre event) {
        List<IComponent> components = OpmaakAPI.INSTANCE.getComponentsForType(event.getType());

        if (components.isEmpty()) {
            return;
        }

        event.setCanceled(true);

        for (IComponent component : components) {
            component.render(event.getMatrixStack(), 0, 0, event.getPartialTicks());
        }
    }
}
