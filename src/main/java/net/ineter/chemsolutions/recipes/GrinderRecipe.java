package net.ineter.chemsolutions.recipes;

import net.ineter.chemsolutions.items.ItemRegistrar;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;

public class GrinderRecipe {

    //new GrinderRecipe()
    public static final List<GrinderRecipe> RECIPES = Arrays.asList(
            new GrinderRecipe(Items.IRON_ORE, 1, ItemRegistrar.ORE_DUSTS.get("dust_iron"), 2, 6000),
            new GrinderRecipe(Items.GOLD_ORE, 1, ItemRegistrar.ORE_DUSTS.get("dust_gold"), 2, 4000)
    );

    public final Item input;
    public final int inputAmount;
    public final Item output;
    public final int outputAmount;
    public final int energyRequired;

    private GrinderRecipe(Item input, Item output) {
        this.input = input;
        this.output = output;
        this.inputAmount = 1;
        this.outputAmount = 1;
        this.energyRequired = 4000;
    }

    private GrinderRecipe(Item input, int inputAmount, Item output, int outputAmount, int energyRequired) {
        this.input = input;
        this.output = output;
        this.inputAmount = inputAmount;
        this.outputAmount = outputAmount;
        this.energyRequired = energyRequired;
    }

    @Nullable
    public static GrinderRecipe findRecipe(ItemStack inputStack) {
        return RECIPES.stream().filter(x -> x.input == inputStack.getItem() && x.inputAmount <= inputStack.getCount()).findFirst().orElse(null);
    }

    public ItemStack createStack() {
        return new ItemStack(this.output, this.outputAmount);
    }
}
