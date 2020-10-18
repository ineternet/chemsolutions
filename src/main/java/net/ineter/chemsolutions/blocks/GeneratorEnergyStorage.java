package net.ineter.chemsolutions.blocks;

import net.minecraftforge.energy.EnergyStorage;

public class GeneratorEnergyStorage extends EnergyStorage {

    public GeneratorEnergyStorage(int capacity) {
        super(capacity);
    }

    public GeneratorEnergyStorage(int capacity, int maxTransfer) {
        super(capacity, maxTransfer);
    }

    public GeneratorEnergyStorage(int capacity, int maxReceive, int maxExtract) {
        super(capacity, maxReceive, maxExtract);
    }

    public GeneratorEnergyStorage(int capacity, int maxReceive, int maxExtract, int energy) {
        super(capacity, maxReceive, maxExtract, energy);
    }

    void setEnergyStored(int energy) {
        this.energy = Math.max(energy, this.capacity);
    }

    @Override
    public boolean canExtract() {
        return true;
    }

    @Override
    public boolean canReceive() {
        return false;
    }

    //Add energy, used for ticking machines that create energy "out of thin air" (from e.g. burning items)
    protected boolean add(int energy) {
        if (this.energy + energy > this.capacity)
            return false;
        this.energy += energy;
        return true;
    }
}
