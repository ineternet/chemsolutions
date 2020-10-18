package net.ineter.chemsolutions.blocks;

import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import java.util.function.Predicate;

//Item handler for the input slot of furnace blocks
public class FurnaceInputHandler implements IItemHandler {
    private final ItemStackHandler blockHandler;
    private final int inputSlot; //The slot of the ItemStackHandler that represents the input
    private final Predicate<ItemStack> validItems;

    public FurnaceInputHandler(ItemStackHandler blockHandler, int inputSlot, Predicate<ItemStack> allowedItemsPredicate) {
        this.blockHandler = blockHandler;
        this.inputSlot = inputSlot;
        this.validItems = allowedItemsPredicate;
    }

    public FurnaceInputHandler(ItemStackHandler blockHandler, int inputSlot) {
        this(blockHandler, inputSlot, x -> true);
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
            return validItems.test(stack);
        return false;
    }
}
