package net.ineter.chemsolutions;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IAdvancedRegistration;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;

@JeiPlugin
public class ChemSolJeiPlugin implements IModPlugin {
    private static ChemSolJeiPlugin INSTANCE;

    public static ChemSolJeiPlugin getInstance() {
        return INSTANCE;
    }

    @Override
    @Nonnull
    public ResourceLocation getPluginUid() {
        return new ResourceLocation("chemsolutions");
    }

    @Override
    public void registerAdvanced(IAdvancedRegistration registration) {

    }
}
