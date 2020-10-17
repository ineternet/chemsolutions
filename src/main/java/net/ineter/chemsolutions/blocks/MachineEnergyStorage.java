package net.ineter.chemsolutions.blocks;

import net.minecraftforge.energy.EnergyStorage;

public class MachineEnergyStorage extends EnergyStorage {

    public MachineEnergyStorage(int capacity) {
        super(capacity);
    }

    public MachineEnergyStorage(int capacity, int maxTransfer) {
        super(capacity, maxTransfer);
    }

    public MachineEnergyStorage(int capacity, int maxReceive, int maxExtract) {
        super(capacity, maxReceive, maxExtract);
    }

    public MachineEnergyStorage(int capacity, int maxReceive, int maxExtract, int energy) {
        super(capacity, maxReceive, maxExtract, energy);
    }

    void setEnergyStored(int energy) {
        this.energy = Math.max(energy, this.capacity);
    }

    @Override
    public boolean canExtract() {
        return false;
    }

    //Remove energy, used for ticking machines that use energy but not the extract function
    protected boolean use(int energy) {
        if (this.energy < energy)
            return false;
        this.energy -= energy;
        return true;
    }
}
