package dev.nierennakker.opmaak;

import dev.nierennakker.opmaak.api.IOpmaakAPI;
import dev.nierennakker.opmaak.component.AttackIndicatorComponent;
import dev.nierennakker.opmaak.component.HotbarComponent;
import dev.nierennakker.opmaak.component.OffhandComponent;
import dev.nierennakker.opmaak.impl.OpmaakAPI;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ExtensionPoint;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.network.FMLNetworkConstants;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.glfw.GLFW;

import java.io.File;
import java.util.function.Consumer;
import java.util.function.Supplier;

@Mod(IOpmaakAPI.MOD_ID)
public class Opmaak {
    public static final Logger LOGGER = LogManager.getLogger();
    public static final KeyBinding KEY = new KeyBinding("key.opmaak", GLFW.GLFW_KEY_O, "key.categories.misc");
    public static final File CONFIG = new File(".", "opmaak.dat");

    public Opmaak() {
        ModLoadingContext.get().registerExtensionPoint(ExtensionPoint.DISPLAYTEST, () -> Pair.of(() -> FMLNetworkConstants.IGNORESERVERONLY, (a, b) -> true));
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
            Supplier<Consumer<IOpmaakAPI>> value = message.getMessageSupplier();

            value.get().accept(OpmaakAPI.INSTANCE);
        });
    }
}
