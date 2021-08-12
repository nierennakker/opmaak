package dev.nierennakker.opmaak;

import dev.nierennakker.opmaak.api.IOpmaakAPI;
import dev.nierennakker.opmaak.component.AttackIndicatorComponent;
import dev.nierennakker.opmaak.component.HotbarComponent;
import dev.nierennakker.opmaak.component.OffhandComponent;
import dev.nierennakker.opmaak.impl.OpmaakAPI;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.IExtensionPoint;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fmlclient.registry.ClientRegistry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.glfw.GLFW;

import java.io.File;
import java.util.function.Consumer;
import java.util.function.Supplier;

@Mod(IOpmaakAPI.MOD_ID)
public class Opmaak {
    public static final Logger LOGGER = LogManager.getLogger();
    public static final KeyMapping KEY = new KeyMapping("key.opmaak", GLFW.GLFW_KEY_O, "key.categories.misc");
    public static final File CONFIG = new File(".", "opmaak.dat");

    public Opmaak() {
        ModLoadingContext.get().registerExtensionPoint(IExtensionPoint.DisplayTest.class, () -> new IExtensionPoint.DisplayTest(() -> "", (a, b) -> true));
        FMLJavaModLoadingContext.get().getModEventBus().register(this);
    }

    @SubscribeEvent
    public void clientSetup(FMLClientSetupEvent event) {
        ClientRegistry.registerKeyBinding(Opmaak.KEY);
    }

    @SubscribeEvent
    public void enqueueIMC(InterModEnqueueEvent event) {
        InterModComms.sendTo(IOpmaakAPI.MOD_ID, IOpmaakAPI.API_METHOD, () -> (Consumer<IOpmaakAPI>) (api) -> {
            api.registerComponent(new AttackIndicatorComponent());
            api.registerComponent(new HotbarComponent());
            api.registerComponent(new OffhandComponent());
        });
    }

    @SubscribeEvent
    public void processIMC(InterModProcessEvent event) {
        event.getIMCStream(IOpmaakAPI.API_METHOD::equals).forEach((message) -> {
            var value = (Supplier<Consumer<IOpmaakAPI>>) message.messageSupplier();

            value.get().accept(OpmaakAPI.INSTANCE);
        });
    }
}
