package net.ineter.chemsolutions.blocks;

import net.ineter.chemsolutions.recipes.GrinderRecipe;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;

//Item handler for the input slot of furnace blocks
public class FurnaceInputHandler implements IItemHandler {
    private final ItemStackHandler blockHandler;
    private final int inputSlot; //The slot of the ItemStackHandler that represents the input

    public FurnaceInputHandler(ItemStackHandler blockHandler, int inputSlot) {
        this.blockHandler = blockHandler;
        this.inputSlot = inputSlot;
    }

    @Override
    public int getSlots() {
        return 1;
    }

    @Nonnull
    @Override
    public ItemStack getStackInSlot(int slot) {
        if (slot == inputSlot)
            return blockHandler.getStackInSlot(inputSlot);
        return ItemStack.EMPTY;
    }

    @Nonnull
    @Override
    public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
        if (slot == inputSlot)
            if (isItemValid(inputSlot, stack))
                return blockHandler.insertItem(inputSlot, stack, simulate);
        return stack;
    }

    @Nonnull
    @Override
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        //Can't extract from input slots
        return ItemStack.EMPTY;
    }

    @Override
    public int getSlotLimit(int slot) {
        if (slot == inputSlot)
            return blockHandler.getSlotLimit(inputSlot);
        return 0;
    }

    @Override
    public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
        if (slot == inputSlot)
            return GrinderRecipe.findRecipe(stack) != null;
        return false;
    }
}
