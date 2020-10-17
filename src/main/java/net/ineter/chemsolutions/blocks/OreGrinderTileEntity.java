package net.ineter.chemsolutions.blocks;

import net.ineter.chemsolutions.recipes.GrinderRecipe;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.IIntArray;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class OreGrinderTileEntity extends TileEntity implements ITickableTileEntity, INamedContainerProvider {
    public final MachineEnergyStorage energyStorage = new MachineEnergyStorage(40000) {{
        energy = capacity;
    }};
    private int perTickRf = 20;
    private LazyOptional<? extends IItemHandler> inputHandler = LazyOptional.of(() -> new FurnaceInputHandler(this.handler, 0));
    private LazyOptional<? extends IItemHandler> outputHandler = LazyOptional.of(() -> new FurnaceOutputHandler(this.handler, 1));
    private int cookTicks = 0;
    public ItemStackHandler handler = new ItemStackHandler(2) {
        @Override
        public void setStackInSlot(int slot, @Nonnull ItemStack stack) {
            if (slot == 0)
                cookTicks = 0;
            super.setStackInSlot(slot, stack);
        }
    };
    public final IIntArray machineData = new IIntArray() {
        @Override
        public int get(int i) {
            if (i == 0) return OreGrinderTileEntity.this.cookTicks;
            if (i == 1) return OreGrinderTileEntity.this.getCookDuration();
            if (i == 2) return OreGrinderTileEntity.this.energyStorage.getEnergyStored();
            return 0;
        }

        @Override
        public void set(int i, int i1) {
            if (i == 0) OreGrinderTileEntity.this.cookTicks = i1;
        }

        @Override
        public int size() {
            return 3;
        }
    };

    public OreGrinderTileEntity() {
        super(BlockRegistrar.TETYPE_ORE_GRINDER);
    }

    public int getCookDuration() {
        GrinderRecipe recipe = getRecipe();
        if (recipe != null)
            return recipe.energyRequired / perTickRf;
        return 0;
    }

    @Override
    public void read(BlockState state, CompoundNBT tag) {
        handler.deserializeNBT(tag.getCompound("inventory"));
        energyStorage.setEnergyStored(tag.getInt("energy"));
        super.read(state, tag);
    }

    @Override
    @Nonnull
    public CompoundNBT write(CompoundNBT tag) {
        CompoundNBT inventory = handler.serializeNBT();
        tag.put("inventory", inventory);
        tag.putInt("energy", energyStorage.getEnergyStored());
        return super.write(tag);
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if (!this.removed && side != null)
            if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
                switch (side) {
                    case UP:
                        return inputHandler.cast();
                    case DOWN:
                        return outputHandler.cast();
                }
            else if (cap == CapabilityEnergy.ENERGY)
                return LazyOptional.of(() -> energyStorage).cast();
        return super.getCapability(cap, side);
    }

    public void dropInventory(World worldIn, BlockPos pos) {
        NonNullList<ItemStack> items = NonNullList.withSize(handler.getSlots(), ItemStack.EMPTY);
        for (int i = 0; i < items.size(); i++)
            items.set(i, handler.getStackInSlot(i));
        InventoryHelper.dropItems(worldIn, pos, items);
    }

    @Nullable
    private GrinderRecipe getRecipe() {
        ItemStack inputStack = handler.getStackInSlot(0);
        if (inputStack.getCount() > 0) {
            return GrinderRecipe.findRecipe(inputStack);
        }
        return null;
    }

    private boolean canSmelt() {
        ItemStack outputStack = handler.getStackInSlot(1);
        GrinderRecipe recipe = getRecipe();
        return recipe != null && (outputStack.getItem() == recipe.output || outputStack.isEmpty());
    }

    private void smelt() {
        if (canSmelt()) {
            ItemStack inputStack = handler.getStackInSlot(0);
            ItemStack outputStack = handler.getStackInSlot(1);
            GrinderRecipe recipe = getRecipe();
            assert recipe != null; //canSmelt verifies that the recipe is not null so if we are here, the recipe exists
            if (outputStack.getItem() == recipe.output)
                outputStack.grow(recipe.outputAmount);
            else
                handler.setStackInSlot(1, recipe.createStack());
            inputStack.shrink(recipe.inputAmount);
        }
    }

    @Override
    public void tick() {
        if (canSmelt()) {
            //energyStorage.receiveEnergy(4000, false);
            if (energyStorage.use(perTickRf)) {
                if (++cookTicks == getCookDuration() - 1) {
                    smelt();
                    cookTicks = 0;
                }
            }
        } else
            cookTicks = 0;
    }

    @Override
    @Nonnull
    public ITextComponent getDisplayName() {
        return new TranslationTextComponent("container.ore_grinder");
    }

    @Nullable
    @Override
    public Container createMenu(int p_createMenu_1_, @Nonnull PlayerInventory p_createMenu_2_, @Nonnull PlayerEntity p_createMenu_3_) {
        return new OreGrinderContainer(p_createMenu_1_, p_createMenu_3_.getEntityWorld(), this.getPos(), p_createMenu_2_, p_createMenu_3_);
    }
}
