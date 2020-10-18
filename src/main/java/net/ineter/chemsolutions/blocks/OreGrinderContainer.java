package net.ineter.chemsolutions.blocks;

import net.ineter.chemsolutions.recipes.GrinderRecipe;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIntArray;
import net.minecraft.util.IntArray;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nonnull;

public class OreGrinderContainer extends Container {

    private final ItemStackHandler inventory;
    private final IIntArray machineData;

    /*public OreGrinderContainer(int id, PlayerInventory playerInventoryIn) {
        this(id, playerInventoryIn, new IntArray(3), new ItemStackHandler(2));
    }*/

    public OreGrinderContainer(int id, World world, BlockPos tepos, PlayerInventory playerInventoryIn, PlayerEntity player) {
        super(ContainerRegistrar.oreGrinderContainerRegistryObject.get(), id);

        OreGrinderTileEntity te = (OreGrinderTileEntity) world.getTileEntity(tepos);

        if (te == null) {
            this.machineData = new IntArray(3);
            this.inventory = new ItemStackHandler(2);
        } else {
            this.machineData = te.machineData;
            this.inventory = te.handler;
        }

        SlotItemHandler inputSlot = new SlotItemHandler(new ContainerInputSlotHandler(inventory, 0, s -> GrinderRecipe.findRecipe(s) != null), 0, 56, 35);
        this.addSlot(inputSlot);
        SlotItemHandler outputSlot = new SlotItemHandler(new ContainerOutputSlotHandler(inventory, 1), 0, 116, 35);
        this.addSlot(outputSlot);

        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                this.addSlot(new Slot(playerInventoryIn, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }

        for (int k = 0; k < 9; ++k) {
            this.addSlot(new Slot(playerInventoryIn, k, 8 + k * 18, 142));
        }

        trackIntArray(this.machineData);
    }

    @Nonnull
    public ItemStack transferStackInSlot(PlayerEntity playerIn, int slot) {
        return playerIn.inventory.getStackInSlot(slot);
    }

    @Override
    public boolean canInteractWith(@Nonnull PlayerEntity playerIn) {
        return true;
    }

    @OnlyIn(Dist.CLIENT)
    private float progress() {
        int cookDuration = machineData.get(1);
        int cookTicks = machineData.get(0);
        if (cookTicks == 0 || cookDuration == 0)
            return 0f;
        return (float) cookTicks / cookDuration;
    }

    @OnlyIn(Dist.CLIENT)
    public int getCookProgressionScaled(int scale) {
        return (int) Math.ceil(progress() * scale);
    }

    @OnlyIn(Dist.CLIENT)
    public int getEnergy() {
        return machineData.get(2);
    }
}
