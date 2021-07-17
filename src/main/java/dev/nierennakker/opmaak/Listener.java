package dev.nierennakker.opmaak;

import dev.nierennakker.opmaak.api.IComponent;
import dev.nierennakker.opmaak.api.IComponentScreen;
import dev.nierennakker.opmaak.api.IOpmaakAPI;
import dev.nierennakker.opmaak.impl.OpmaakAPI;
import dev.nierennakker.opmaak.screen.OpmaakScreen;
import dev.nierennakker.opmaak.util.Alignment;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Tuple;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;

@Mod.EventBusSubscriber(modid = IOpmaakAPI.MOD_ID, value = Dist.CLIENT)
public class Listener {
    @SubscribeEvent
    public static void renderOverlay(RenderGameOverlayEvent.Pre event) {
        Minecraft mc = Minecraft.getInstance();
        PlayerEntity player = mc.gui.getCameraPlayer();
        List<IComponent> components = OpmaakAPI.INSTANCE.getComponentsForType(event.getType());

        if (player == null || components.isEmpty()) {
            return;
        }

        event.setCanceled(true);

        for (IComponent component : components) {
            if (mc.screen instanceof IComponentScreen) {
                if (!((IComponentScreen) mc.screen).displayComponent(component)) {
                    continue;
                }
            }

            CompoundNBT nbt = OpmaakAPI.INSTANCE.getComponentStorage(component);
            Tuple<Integer, Integer> position = Alignment.toAbsolute(nbt.getString("alignment"), nbt.getInt("x"), nbt.getInt("y"));

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
