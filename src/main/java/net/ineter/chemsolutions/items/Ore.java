package net.ineter.chemsolutions.items;

public enum Ore {
    IRON("iron", OreType.ORE, 0xFFABABAB, 1, 1),
    GOLD("gold", OreType.ORE, 0xFFEFEF09, 4, 1),
    DIAMOND("diamond", OreType.CRYSTAL, 0xFF0cFFFF, 2, 4);


    public final int colorMultiplier;
    public final String registrySuffix;
    public final int minSolventLevel;
    public final int minGrinderLevel;
    public final OreType oreType;

    Ore(String registrySuffix, OreType oreType, int colorMultiplier, int minSolventLevel, int minGrinderLevel) {
        this.colorMultiplier = colorMultiplier;
        this.registrySuffix = registrySuffix;
        this.oreType = oreType;
        this.minSolventLevel = minSolventLevel;
        this.minGrinderLevel = minGrinderLevel;
    }

    Ore(String registrySuffix) {
        this.colorMultiplier = 0xFFFFFFFF;
        this.registrySuffix = registrySuffix;
        this.oreType = OreType.ORE;
        this.minSolventLevel = 0;
        this.minGrinderLevel = 0;
    }

    public enum OreType {
        ORE,
        CRYSTAL,
        DUST,
    }
}
