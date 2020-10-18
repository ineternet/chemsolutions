package net.ineter.chemsolutions.blocks;

import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.ContainerBlock;

import javax.annotation.Nonnull;

public abstract class EnergyGenerator extends ContainerBlock {

    protected EnergyGenerator(Properties p_i48446_1_) {
        super(p_i48446_1_);
    }

    @Override
    @Nonnull
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }
}
