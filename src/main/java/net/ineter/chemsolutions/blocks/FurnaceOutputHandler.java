package net.ineter.chemsolutions.blocks;

import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;

//Item handler for the output slot of furnace blocks
public class FurnaceOutputHandler implements IItemHandler {
    private final ItemStackHandler blockHandler;
    private final int outputSlot; //The slot of the ItemStackHandler that represents the output

    public FurnaceOutputHandler(ItemStackHandler blockHandler, int outputSlot) {
        this.blockHandler = blockHandler;
        this.outputSlot = outputSlot;
    }

    @Override
    public int getSlots() {
        return 1;
    }

    @Nonnull
    @Override
    public ItemStack getStackInSlot(int slot) {
        if (slot == 0)
            return blockHandler.getStackInSlot(outputSlot);
        return ItemStack.EMPTY;
    }

    @Nonnull
    @Override
    public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
        if (slot == 0)
            if (isItemValid(0, stack))
                return blockHandler.insertItem(outputSlot, stack, simulate);
        return stack;
    }

    @Nonnull
    @Override
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        if (slot == 0)
            return blockHandler.extractItem(outputSlot, amount, simulate);
        return ItemStack.EMPTY;
    }

    @Override
    public int getSlotLimit(int slot) {
        if (slot == 0)
            return blockHandler.getSlotLimit(outputSlot);
        return 0;
    }

    @Override
    public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
        return false;
    }
}
