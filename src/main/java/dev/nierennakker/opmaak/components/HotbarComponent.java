package dev.nierennakker.opmaak.components;

import com.mojang.blaze3d.matrix.MatrixStack;
import dev.nierennakker.opmaak.Opmaak;
import dev.nierennakker.opmaak.api.IComponent;
import dev.nierennakker.opmaak.api.IOpmaakAPI;
import net.minecraft.util.ResourceLocation;

public class HotbarComponent implements IComponent {
    @Override
    public ResourceLocation getID() {
        return new ResourceLocation(IOpmaakAPI.MOD_ID, "hotbar");
    }

    @Override
    public void render(MatrixStack stack, int x, int y, float delta) {
        Opmaak.LOGGER.info("hotbar?");
    }
}
