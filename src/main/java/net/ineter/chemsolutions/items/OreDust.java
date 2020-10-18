package net.ineter.chemsolutions.items;

import net.minecraft.block.BlockState;
import net.minecraft.block.IBucketPickupHandler;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

public class OreDust extends Item {

    private static final Properties ITEM_PROPERTIES = new Properties() {
    };

    public final Ore baseOre;

    public OreDust(Ore fromOre) {
        super(ITEM_PROPERTIES);
        baseOre = fromOre;
    }

    @Override
    @Nonnull
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, @Nonnull Hand handIn) {
        ItemStack otherhandItem = playerIn.getHeldItem(handIn == Hand.OFF_HAND ? Hand.MAIN_HAND : Hand.OFF_HAND);
        ItemStack itemstack = playerIn.getHeldItem(handIn);
        if (otherhandItem.getItem() != Items.BOWL)
            return ActionResult.resultPass(itemstack);
        BlockRayTraceResult raytraceresult = rayTrace(worldIn, playerIn, RayTraceContext.FluidMode.SOURCE_ONLY);
        if (raytraceresult.getType() == RayTraceResult.Type.MISS) {
            return ActionResult.resultPass(itemstack);
        } else if (raytraceresult.getType() != RayTraceResult.Type.BLOCK) {
            return ActionResult.resultPass(itemstack);
        } else {
            BlockPos blockpos = raytraceresult.getPos();
            Direction direction = raytraceresult.getFace();
            BlockPos blockpos1 = blockpos.offset(direction);
            BlockState blockstate1 = worldIn.getBlockState(blockpos);
            if (blockstate1.getBlock() instanceof IBucketPickupHandler) {
                Fluid fluid = blockstate1.getFluidState().getFluid();
                if (fluid == Fluids.WATER) {
                    SoundEvent soundevent = SoundEvents.ENTITY_PLAYER_SPLASH;
                    playerIn.playSound(soundevent, 1.0F, 1.0F);
                    ItemStack dust;

                    if (!baseOre.pure) {
                        dust = new ItemStack(ItemRegistrar.ORE_OXIDES.get("oxide_" + baseOre.registrySuffix));
                    } else {
                        dust = new ItemStack(ItemRegistrar.ORE_PULVS.get("pulv_" + baseOre.registrySuffix));
                    }

                    if (dust == ItemStack.EMPTY)
                        return ActionResult.resultFail(itemstack);

                    if (!playerIn.inventory.addItemStackToInventory(dust)) {
                        InventoryHelper.spawnItemStack(worldIn, playerIn.getPosX(), playerIn.getPosY(), playerIn.getPosZ(), dust);
                    }

                    itemstack.shrink(1);
                    return ActionResult.resultSuccess(itemstack);
                }
            }

            return ActionResult.resultFail(itemstack);
        }
    }
}
