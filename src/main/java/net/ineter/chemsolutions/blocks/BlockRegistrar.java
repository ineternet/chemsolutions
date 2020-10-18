package net.ineter.chemsolutions.blocks;

import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import static net.ineter.chemsolutions.ChemistrySolutions.MODID;

//Also tile entities
@SuppressWarnings("ConstantConditions")
public class BlockRegistrar {

    private static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, MODID);
    private static final DeferredRegister<Item> BLOCK_ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MODID);
    private static final DeferredRegister<TileEntityType<?>> TILES = DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, MODID);

    private static final Block ORE_GRINDER = new OreGrinder();
    public static final TileEntityType<?> TETYPE_ORE_GRINDER = TileEntityType.Builder.create(OreGrinderTileEntity::new, ORE_GRINDER).build(null);

    private static final Block FIREBOX_GENERATOR = new FireboxGenerator();
    public static final TileEntityType<?> TETYPE_FIREBOX_GENERATOR = TileEntityType.Builder.create(FireboxGenerator.FireboxGeneratorTileEntity::new, FIREBOX_GENERATOR).build(null);

    private static final Item.Properties GENERIC_BLOCK_ITEM_PROPERTIES = new Item.Properties();

    public static void registerAll() {
        //Ore grinder
        RegistryObject<Block> oreGrinderRegistryObject = BLOCKS.register("machine_grinder", () -> ORE_GRINDER);
        RegistryObject<TileEntityType<?>> oreGrinderTERegistryObject = TILES.register("machine_grinder", () -> TETYPE_ORE_GRINDER);

        //Firebox generator
        RegistryObject<Block> fireboxGeneratorRegistryObject = BLOCKS.register("generator_firebox", () -> FIREBOX_GENERATOR);
        RegistryObject<TileEntityType<?>> fireboxGeneratorTERegistryObject = TILES.register("generator_firebox", () -> TETYPE_FIREBOX_GENERATOR);

        //Block items
        BLOCK_ITEMS.register("machine_grinder", () -> new BlockItem(ORE_GRINDER, GENERIC_BLOCK_ITEM_PROPERTIES));
        BLOCK_ITEMS.register("generator_firebox", () -> new BlockItem(FIREBOX_GENERATOR, GENERIC_BLOCK_ITEM_PROPERTIES));

        BLOCKS.register(FMLJavaModLoadingContext.get().getModEventBus());
        BLOCK_ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
        TILES.register(FMLJavaModLoadingContext.get().getModEventBus());
    }
}
