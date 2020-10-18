package net.ineter.chemsolutions.blocks;

import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import java.util.function.Predicate;

public class ContainerInputSlotHandler implements IItemHandlerModifiable {
    private final ItemStackHandler inventory;
    private final int inputSlot;
    private final Predicate<ItemStack> validItems;

    public ContainerInputSlotHandler(ItemStackHandler inventory, int inputSlot, Predicate<ItemStack> allowedItemsPredicate) {
        this.inventory = inventory;
        this.inputSlot = inputSlot;
        this.validItems = allowedItemsPredicate;
    }

    public ContainerInputSlotHandler(ItemStackHandler inventory, int inputSlot) {
        this(inventory, inputSlot, s -> true);
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
            return validItems.test(stack);
        return false;
    }

    @Override
    public void setStackInSlot(int slot, @Nonnull ItemStack stack) {
        if (slot == inputSlot)
            inventory.setStackInSlot(inputSlot, stack);
    }
}
