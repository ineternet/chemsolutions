package net.ineter.chemsolutions.items;

import net.minecraft.item.Item;

public class Dust extends Item {

    private static final Properties ITEM_PROPERTIES = new Properties() {
    };

    public final Ore baseOre;

    public Dust(Ore fromOre) {
        super(ITEM_PROPERTIES);
        baseOre = fromOre;
    }
}
