package net.ineter.chemsolutions.blocks;

import net.ineter.chemsolutions.recipes.GrinderRecipe;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;

public class ContainerInputSlotHandler implements IItemHandlerModifiable {
    private final ItemStackHandler inventory;
    private final int inputSlot;

    public ContainerInputSlotHandler(ItemStackHandler inventory, int inputSlot) {
        this.inventory = inventory;
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
            return inventory.getStackInSlot(inputSlot);
        return ItemStack.EMPTY;
    }

    @Nonnull
    @Override
    public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
        if (slot == inputSlot)
            if (isItemValid(inputSlot, stack))
                return inventory.insertItem(inputSlot, stack, simulate);
        return stack;
    }

    @Nonnull
    @Override
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        if (slot == inputSlot)
            return inventory.extractItem(inputSlot, amount, simulate);
        return ItemStack.EMPTY;
    }

    @Override
    public int getSlotLimit(int slot) {
        if (slot == inputSlot)
            return inventory.getSlotLimit(inputSlot);
        return 0;
    }

    @Override
    public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
        if (slot == inputSlot)
            return GrinderRecipe.findRecipe(stack) != null;
        return false;
    }

    @Override
    public void setStackInSlot(int slot, @Nonnull ItemStack stack) {
        if (slot == inputSlot)
            inventory.setStackInSlot(inputSlot, stack);
    }
}
