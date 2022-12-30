package dev.nierennakker.opmaak;

import dev.nierennakker.opmaak.api.OpmaakAPI;
import dev.nierennakker.opmaak.impl.OpmaakAPIImpl;
import dev.nierennakker.opmaak.widget.AttackIndicatorWidget;
import dev.nierennakker.opmaak.widget.HotbarWidget;
import dev.nierennakker.opmaak.widget.OffhandWidget;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.client.event.RegisterGuiOverlaysEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.glfw.GLFW;

import java.io.File;
import java.util.function.Consumer;
import java.util.function.Supplier;

@Mod(OpmaakAPI.MOD_ID)
public class Opmaak {
    public static final Logger LOGGER = LogManager.getLogger(OpmaakAPI.MOD_ID);
    public static final KeyMapping KEY = new KeyMapping("key.opmaak", GLFW.GLFW_KEY_O, "key.categories.misc");
    public static final File CONFIG = new File(".", "opmaak.dat");

    public Opmaak() {
        FMLJavaModLoadingContext.get().getModEventBus().register(this);
    }

    @SubscribeEvent
    public void registerGuiOverlay(RegisterGuiOverlaysEvent event) {
        Opmaak.LOGGER.info("Registering gui overlays");
    }

    @SubscribeEvent
    public void registerKeyMapping(RegisterKeyMappingsEvent event) {
        event.register(Opmaak.KEY);
    }

    @SubscribeEvent
    public void enqueueIMC(InterModEnqueueEvent event) {
        InterModComms.sendTo(OpmaakAPI.MOD_ID, OpmaakAPI.API_METHOD, () -> (Consumer<OpmaakAPI>) (api) -> {
            api.registerWidget(new AttackIndicatorWidget());
            api.registerWidget(new HotbarWidget());
            api.registerWidget(new OffhandWidget());
        });
    }

    @SubscribeEvent
    public void processIMC(InterModProcessEvent event) {
        event.getIMCStream(OpmaakAPI.API_METHOD::equals).forEach((message) -> {
            var value = (Supplier<Consumer<OpmaakAPI>>) message.messageSupplier();

            value.get().accept(OpmaakAPIImpl.INSTANCE);
        });
    }
}
