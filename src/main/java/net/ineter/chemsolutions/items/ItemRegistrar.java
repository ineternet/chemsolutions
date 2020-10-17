package net.ineter.chemsolutions.items;

import net.minecraft.client.Minecraft;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.*;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.text.Color;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static net.ineter.chemsolutions.ChemistrySolutions.MODID;

public class ItemRegistrar {
    private static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MODID);
    private static final Style SUBTLE_TOOLTIP = Style.EMPTY.setItalic(true).setColor(Color.fromInt(0xFF404040));
    private static final Style WARN_TOOLTIP = Style.EMPTY.setItalic(true).setColor(Color.fromInt(0xFF8300));
    public static Map<String, Rocks> ORE_ROCKS = new HashMap<>();
    public static Map<String, Dust> ORE_DUSTS = new HashMap<>();
    public static Item CARBONFIBER_PLATING = new Item(new Item.Properties()) {
        @Override
        public void addInformation(ItemStack p_77624_1_, @Nullable World p_77624_2_, List<ITextComponent> p_77624_3_, ITooltipFlag p_77624_4_) {
            p_77624_3_.add(new TranslationTextComponent("tooltip.chemsolutions.carbonfiber_plating").setStyle(SUBTLE_TOOLTIP));
            super.addInformation(p_77624_1_, p_77624_2_, p_77624_3_, p_77624_4_);
        }
    };
    public static final IArmorMaterial CARBONFIBER = new IArmorMaterial() {
        @Override
        public int getDurability(@Nonnull EquipmentSlotType equipmentSlotType) {
            return ArmorMaterial.DIAMOND.getDurability(equipmentSlotType) * 2;
        }

        @Override
        public int getDamageReductionAmount(@Nonnull EquipmentSlotType equipmentSlotType) {
            return ArmorMaterial.DIAMOND.getDamageReductionAmount(equipmentSlotType) + 2;
        }

        @Override
        public int getEnchantability() {
            return ArmorMaterial.DIAMOND.getEnchantability() - 2;
        }

        @Override
        public SoundEvent getSoundEvent() {
            return ArmorMaterial.IRON.getSoundEvent();
        }

        @Override
        public Ingredient getRepairMaterial() {
            return Ingredient.fromItems(CARBONFIBER_PLATING);
        }

        @Override
        public String getName() {
            return "chemsolutions:carbonfiber";
        }

        @Override
        public float getToughness() {
            return ArmorMaterial.DIAMOND.getToughness() + 2;
        }

        @Override
        public float getKnockbackResistance() {
            return ArmorMaterial.DIAMOND.getKnockbackResistance() * 2;
        }
    };
    public static Item CARBONFIBER_SHEET = new Item(new Item.Properties()) {
        @Override
        public void addInformation(ItemStack p_77624_1_, @Nullable World p_77624_2_, List<ITextComponent> p_77624_3_, ITooltipFlag p_77624_4_) {
            p_77624_3_.add(new TranslationTextComponent("tooltip.chemsolutions.carbonfiber_sheet.warning").setStyle(WARN_TOOLTIP));
            super.addInformation(p_77624_1_, p_77624_2_, p_77624_3_, p_77624_4_);
        }
    };
    public static Item CARBONFIBER_SPOOL = new Item(new Item.Properties());
    public static Item CARBONFIBER_CHESTPLATE = new ArmorItem(CARBONFIBER, EquipmentSlotType.CHEST, new Item.Properties());

    static {
        for (Ore ore : Ore.values()) {
            ORE_ROCKS.put("rocks_" + ore.registrySuffix, new Rocks(ore));
            ORE_DUSTS.put("dust_" + ore.registrySuffix, new Dust(ore));
        }
    }

    public static void registerAll() {
        //Rocks
        for (Rocks oreRocks : ORE_ROCKS.values()) {
            RegistryObject<Item> oreRocksRegistryObject = ITEMS.register("ore_rocks_" + oreRocks.baseOre.registrySuffix, () -> oreRocks);
        }
        //Dusts
        for (Dust oreDust : ORE_DUSTS.values()) {
            RegistryObject<Item> oreDustRegistryObject = ITEMS.register("ore_dust_" + oreDust.baseOre.registrySuffix, () -> oreDust);
        }

        ITEMS.register("carbonfiber_plating", () -> CARBONFIBER_PLATING);
        ITEMS.register("carbonfiber_chestplate", () -> CARBONFIBER_CHESTPLATE);
        ITEMS.register("carbonfiber_sheet", () -> CARBONFIBER_SHEET);
        ITEMS.register("carbonfiber_spool", () -> CARBONFIBER_SPOOL);

        ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
    }

    public static void registerItemColors() {
        //This code needs to be executed on the client
        for (Dust oreDust : ORE_DUSTS.values()) {
            Minecraft.getInstance().getItemColors().register(((itemStack, i) -> oreDust.baseOre.colorMultiplier), oreDust);
        }

        for (Rocks oreRocks : ORE_ROCKS.values()) {
            Minecraft.getInstance().getItemColors().register(((itemStack, i) -> i > 0 ? oreRocks.baseOre.colorMultiplier : -1), oreRocks);
        }
    }
}