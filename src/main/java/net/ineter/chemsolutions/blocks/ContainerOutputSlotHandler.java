package net.ineter.chemsolutions.blocks;

import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;

public class ContainerOutputSlotHandler implements IItemHandlerModifiable {
    private final ItemStackHandler inventory;
    private final int outputSlot;

    public ContainerOutputSlotHandler(ItemStackHandler inventory, int outputSlot) {
        this.inventory = inventory;
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
            return inventory.getStackInSlot(outputSlot);
        return ItemStack.EMPTY;
    }

    @Nonnull
    @Override
    public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
        if (slot == 0)
            if (isItemValid(0, stack))
                return inventory.insertItem(outputSlot, stack, simulate);
        return stack;
    }

    @Nonnull
    @Override
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        if (slot == 0)
            return inventory.extractItem(outputSlot, amount, simulate);
        return ItemStack.EMPTY;
    }

    @Override
    public int getSlotLimit(int slot) {
        if (slot == 0)
            return inventory.getSlotLimit(outputSlot);
        return 0;
    }

    @Override
    public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
        return false;
    }

    @Override
    public void setStackInSlot(int slot, @Nonnull ItemStack stack) {
        if (slot == 0)
            inventory.setStackInSlot(outputSlot, stack);
    }
}
