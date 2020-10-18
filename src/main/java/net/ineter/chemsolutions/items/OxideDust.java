package net.ineter.chemsolutions.items;

import net.minecraft.item.Item;

public class OxideDust extends Item {

    private static final Properties ITEM_PROPERTIES = new Properties() {
    };

    public final Ore baseOre;

    public OxideDust(Ore fromOre) {
        super(ITEM_PROPERTIES);
        baseOre = fromOre;
    }
}
