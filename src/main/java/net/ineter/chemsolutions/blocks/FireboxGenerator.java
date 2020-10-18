package net.ineter.chemsolutions.blocks;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.ineter.chemsolutions.ChemistrySolutions;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fml.network.NetworkHooks;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.function.Predicate;

public class FireboxGenerator extends EnergyGenerator {

    public static final Properties BLOCK_PROPERTIES = Properties.from(Blocks.IRON_BLOCK).harvestLevel(0);
    public static final DirectionProperty FACING = HorizontalBlock.HORIZONTAL_FACING;

    private static final Predicate<ItemStack> IS_FLAMMABLE = x -> ForgeHooks.getBurnTime(x) > 0;

    protected FireboxGenerator() {
        super(BLOCK_PROPERTIES);
        this.setDefaultState(this.stateContainer.getBaseState().with(FACING, Direction.NORTH));
    }

    public BlockState getStateForPlacement(BlockItemUseContext p_196258_1_) {
        return this.getDefaultState().with(FACING, p_196258_1_.getPlacementHorizontalFacing().getOpposite());
    }

    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> p_206840_1_) {
        p_206840_1_.add(FACING);
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onReplaced(BlockState state, @Nonnull World worldIn, @Nonnull BlockPos pos, BlockState newState, boolean isMoving) {
        if (!state.isIn(newState.getBlock())) {
            TileEntity tileentity = worldIn.getTileEntity(pos);
            if (tileentity instanceof FireboxGeneratorTileEntity) {
                ((FireboxGeneratorTileEntity) tileentity).dropInventory(worldIn, pos);
            }

            super.onReplaced(state, worldIn, pos, newState, isMoving);
        }
    }

    @SuppressWarnings("deprecation")
    @Override
    @Nonnull
    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
        if (worldIn.isRemote) {
            return ActionResultType.SUCCESS;
        } else {
            TileEntity tileentity = worldIn.getTileEntity(pos);
            if (tileentity instanceof FireboxGeneratorTileEntity) {
                NetworkHooks.openGui((ServerPlayerEntity) player, (INamedContainerProvider) tileentity, pos);
            }
            return ActionResultType.CONSUME;
        }
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(@Nonnull IBlockReader worldIn) {
        return new FireboxGeneratorTileEntity();
    }

    public static class FireboxGeneratorTileEntity extends TileEntity implements ITickableTileEntity, INamedContainerProvider {
        public final GeneratorEnergyStorage energyStorage = new GeneratorEnergyStorage(40000);
        public ItemStackHandler handler = new ItemStackHandler(1);
        private int perTickRf = 20;
        private LazyOptional<? extends IItemHandler> inputHandler = LazyOptional.of(() -> new FurnaceInputHandler(this.handler, 0, IS_FLAMMABLE));
        private LazyOptional<? extends IEnergyStorage> energyHandler = LazyOptional.of(() -> energyStorage);
        private int burntime = 0;
        private int lastItemBurntime;
        public final IIntArray machineData = new IIntArray() {
            @Override
            public int get(int i) {
                if (i == 0) return FireboxGeneratorTileEntity.this.burntime;
                if (i == 1) return FireboxGeneratorTileEntity.this.energyStorage.getEnergyStored();
                if (i == 2) return FireboxGeneratorTileEntity.this.lastItemBurntime;
                return 0;
            }

            @Override
            public void set(int i, int i1) {
                if (i == 0) FireboxGeneratorTileEntity.this.burntime = i1;
                if (i == 2) FireboxGeneratorTileEntity.this.lastItemBurntime = i1;
            }

            @Override
            public int size() {
                return 3;
            }
        };

        public FireboxGeneratorTileEntity() {
            super(BlockRegistrar.TETYPE_FIREBOX_GENERATOR);
        }

        public void dropInventory(World worldIn, BlockPos pos) {
            NonNullList<ItemStack> items = NonNullList.withSize(handler.getSlots(), ItemStack.EMPTY);
            for (int i = 0; i < items.size(); i++)
                items.set(i, handler.getStackInSlot(i));
            InventoryHelper.dropItems(worldIn, pos, items);
        }

        @Override
        @Nonnull
        public ITextComponent getDisplayName() {
            return new TranslationTextComponent("container.generator_firebox");
        }

        @Nullable
        @Override
        public Container createMenu(int i, @Nonnull PlayerInventory playerInventory, @Nonnull PlayerEntity playerEntity) {
            return new FireboxGeneratorContainer(i, playerEntity.getEntityWorld(), this.getPos(), playerInventory, playerEntity);
        }

        @Override
        public void read(BlockState state, CompoundNBT tag) {
            handler.deserializeNBT(tag.getCompound("inventory"));
            energyStorage.setEnergyStored(tag.getInt("energy"));
            burntime = tag.getInt("burntime");
            lastItemBurntime = tag.getInt("lastItemBurntime");
            super.read(state, tag);
        }

        @Override
        @Nonnull
        public CompoundNBT write(CompoundNBT tag) {
            CompoundNBT inventory = handler.serializeNBT();
            tag.put("inventory", inventory);
            tag.putInt("energy", energyStorage.getEnergyStored());
            tag.putInt("burntime", burntime);
            tag.putInt("lastItemBurntime", lastItemBurntime);
            return super.write(tag);
        }

        @Nonnull
        @Override
        public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
            if (!this.removed && side != null)
                if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
                    return inputHandler.cast();
                else if (cap == CapabilityEnergy.ENERGY)
                    return energyHandler.cast();
            return super.getCapability(cap, side);
        }

        @Override
        public void tick() {
            if (burntime <= 0) {
                ItemStack input = handler.getStackInSlot(0);
                int fueltime = ForgeHooks.getBurnTime(input) / 4;
                if (fueltime > 0) {
                    lastItemBurntime = fueltime;
                    burntime = fueltime;
                    input.shrink(1);
                }
            } else {
                if (energyStorage.add(80))
                    burntime--;
            }

            int maxSend = 80;
            int sentThisTick = 0;
            for (Direction dir : Direction.values()) {
                if (sentThisTick >= maxSend)
                    break;
                if (getWorld() != null) {
                    TileEntity te = getWorld().getTileEntity(this.getPos().offset(dir));
                    if (te != null) {
                        LazyOptional<IEnergyStorage> capability = te.getCapability(CapabilityEnergy.ENERGY, dir.getOpposite());
                        if (capability.resolve().isPresent()) {
                            IEnergyStorage storage = capability.resolve().get();
                            if (storage.canReceive()) {
                                sentThisTick += storage.receiveEnergy(this.energyStorage.extractEnergy(maxSend - sentThisTick, false), false);
                            }
                        }
                    }
                }
            }
        }
    }

    public static class FireboxGeneratorContainer extends Container {
        private final ItemStackHandler inventory;
        private final IIntArray machineData;

        public FireboxGeneratorContainer(int id, World world, BlockPos tepos, PlayerInventory playerInventoryIn, PlayerEntity player) {
            super(ContainerRegistrar.fireboxGeneratorContainerRegistryObject.get(), id);

            FireboxGeneratorTileEntity te = (FireboxGeneratorTileEntity) world.getTileEntity(tepos);

            if (te == null) {
                this.machineData = new IntArray(3);
                this.inventory = new ItemStackHandler(1);
            } else {
                this.machineData = te.machineData;
                this.inventory = te.handler;
            }

            SlotItemHandler inputSlot = new SlotItemHandler(new ContainerInputSlotHandler(inventory, 0, IS_FLAMMABLE), 0, 56, 35);
            this.addSlot(inputSlot);

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
            int burntime = machineData.get(0);
            int totalburntime = machineData.get(2);
            if (burntime == 0 || totalburntime == 0)
                return 0f;
            return 1f - (float) burntime / totalburntime;
        }

        @OnlyIn(Dist.CLIENT)
        public int getCookProgressionScaled(int scale) {
            return (int) Math.ceil(progress() * scale);
        }

        @OnlyIn(Dist.CLIENT)
        public int getEnergy() {
            return machineData.get(1);
        }

        @OnlyIn(Dist.CLIENT)
        public int getBurntime() {
            return machineData.get(0);
        }
    }

    @SuppressWarnings("deprecation")
    @OnlyIn(Dist.CLIENT)
    public static class FireboxContainerScreen extends ContainerScreen<FireboxGeneratorContainer> {
        private final ResourceLocation guiTexture = new ResourceLocation(ChemistrySolutions.MODID, "textures/gui/container/generator_itemburner.png");

        public FireboxContainerScreen(FireboxGeneratorContainer screenContainer, PlayerInventory inv, ITextComponent titleIn) {
            super(screenContainer, inv, titleIn);
        }

        @Override
        public void render(@Nonnull MatrixStack p_230430_1_, int p_230430_2_, int p_230430_3_, float p_230430_4_) {
            this.renderBackground(p_230430_1_);
            super.render(p_230430_1_, p_230430_2_, p_230430_3_, p_230430_4_);
            this.renderHoveredTooltip(p_230430_1_, p_230430_2_, p_230430_3_);
        }

        @Override
        protected void drawGuiContainerBackgroundLayer(@Nonnull MatrixStack matrixStack, float partialTicks, int x, int y) {
            RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
            if (this.minecraft != null) {
                this.minecraft.getTextureManager().bindTexture(this.guiTexture);
            }
            int lvt_5_1_ = this.guiLeft;
            int lvt_6_1_ = this.guiTop;
            this.blit(matrixStack, lvt_5_1_, lvt_6_1_, 0, 0, this.xSize, this.ySize);
            int lvt_7_2_ = this.getContainer().getCookProgressionScaled(24);
            this.blit(matrixStack, lvt_5_1_ + 79, lvt_6_1_ + 34, 176, 14, lvt_7_2_, 16);
        }

        @Override
        protected void drawGuiContainerForegroundLayer(@Nonnull MatrixStack p_230451_1_, int p_230451_2_, int p_230451_3_) {
            super.drawGuiContainerForegroundLayer(p_230451_1_, p_230451_2_, p_230451_3_);
            this.font.drawString(p_230451_1_, I18n.format("container.ore_grinder.energy", this.getContainer().getEnergy()), 8, 16, 0xFF404040);
            this.font.drawString(p_230451_1_, I18n.format("container.generator.burntime", this.getContainer().getBurntime()), 8, 25, 0xFF404040);
        }
    }

}
