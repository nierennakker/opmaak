package dev.nierennakker.opmaak;

import dev.nierennakker.opmaak.api.OpmaakAPI;
import dev.nierennakker.opmaak.screen.OpmaakScreen;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = OpmaakAPI.MOD_ID, value = Dist.CLIENT)
public class Listener {
    @SubscribeEvent
    public static void keyPress(InputEvent.KeyInputEvent event) {
        if (Opmaak.KEY.consumeClick()) {
            Minecraft.getInstance().setScreen(new OpmaakScreen());
        }
    }
}
