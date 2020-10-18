package net.ineter.chemsolutions.items;

public enum Ore {
    IRON("iron", OreType.ORE, 0xFFABABAB, 1, 1, false),
    GOLD("gold", OreType.ORE, 0xFFEFEF09, 4, 1, true),
    DIAMOND("diamond", OreType.CRYSTAL, 0xFF0cFFFF, 2, 4, true),

    COAL("coal", OreType.CRYSTAL, 0xFF070707, 1, 1, true),
    LAPIS_LAZULI("lapis_lazuli", OreType.CRYSTAL, 0xFF0710EF, 2, 1, false),
    REDSTONE("redstone", OreType.DUST, 0xFFFF0501, 3, 2, true),
    EMERALD("emerald", OreType.CRYSTAL, 0xFF06BF20, 2, 3, true),
    NETHER_QUARTZ("nether_quartz", OreType.CRYSTAL, 0xFFD1CEC2, 2, 2, false),
    ;


    public final int colorMultiplier;
    public final String registrySuffix;
    public final int minSolventLevel;
    public final int minGrinderLevel;
    public final OreType oreType;
    public final boolean pure; //Determines whether there's an intermediate step between elemental and oxide dust

    Ore(String registrySuffix, OreType oreType, int colorMultiplier, int minSolventLevel, int minGrinderLevel, boolean pure) {
        this.colorMultiplier = colorMultiplier;
        this.registrySuffix = registrySuffix;
        this.oreType = oreType;
        this.minSolventLevel = minSolventLevel;
        this.minGrinderLevel = minGrinderLevel;
        this.pure = pure;
    }

    Ore(String registrySuffix, OreType oreType, int colorMultiplier, int minSolventLevel, int minGrinderLevel) {
        this(registrySuffix, oreType, colorMultiplier, minSolventLevel, minGrinderLevel, false);
    }

    Ore(String registrySuffix) {
        this(registrySuffix, OreType.ORE, 0xFFFFFFFF, 0, 0, false);
    }

    public enum OreType {
        ORE,
        CRYSTAL,
        DUST,
    }
}
