package net.ineter.chemsolutions.blocks;

import net.minecraft.client.gui.ScreenManager;
import net.minecraft.inventory.container.ContainerType;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import static net.ineter.chemsolutions.ChemistrySolutions.MODID;

public class ContainerRegistrar {
    private static final DeferredRegister<ContainerType<?>> CONTAINERS = DeferredRegister.create(ForgeRegistries.CONTAINERS, MODID);

    public static final RegistryObject<ContainerType<OreGrinderContainer>> oreGrinderContainerRegistryObject = CONTAINERS.register("machine_generic",
            () -> IForgeContainerType.create((wid, inv, data) -> new OreGrinderContainer(wid, inv.player.getEntityWorld(), data.readBlockPos(), inv, inv.player)));

    public static final RegistryObject<ContainerType<FireboxGenerator.FireboxGeneratorContainer>> fireboxGeneratorContainerRegistryObject = CONTAINERS.register("generator_firebox",
            () -> IForgeContainerType.create((wid, inv, data) -> new FireboxGenerator.FireboxGeneratorContainer(wid, inv.player.getEntityWorld(), data.readBlockPos(), inv, inv.player)));

    public static void registerAll() {
        CONTAINERS.register(FMLJavaModLoadingContext.get().getModEventBus());
    }

    public static void registerGuis() {
        ScreenManager.registerFactory(oreGrinderContainerRegistryObject.get(), GrinderContainerScreen::new);
        ScreenManager.registerFactory(fireboxGeneratorContainerRegistryObject.get(), FireboxGenerator.FireboxContainerScreen::new);
    }
}
