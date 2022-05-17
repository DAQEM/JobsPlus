package me.daqem.jobsplus.common.potion.brewing;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraftforge.common.brewing.IBrewingRecipe;
import org.jetbrains.annotations.NotNull;

public class ModBrewingRecipe implements IBrewingRecipe {

    private final Potion bottleInput;
    private final Item itemInput;
    private final ItemStack output;

    public ModBrewingRecipe(Potion bottleInput, Item itemInput, Potion output) {
        this.bottleInput = bottleInput;
        this.itemInput = itemInput;
        this.output = PotionUtils.setPotion(new ItemStack(Items.POTION), output);
    }

    @Override
    public boolean isInput(@NotNull ItemStack input) {
        return PotionUtils.getPotion(input).equals(this.bottleInput);
    }

    @Override
    public boolean isIngredient(ItemStack ingredient) {
        return ingredient.getItem().equals(this.itemInput);
    }

    @Override
    public @NotNull ItemStack getOutput(@NotNull ItemStack input, @NotNull ItemStack ingredient) {
        if (isInput(input) && isIngredient(ingredient)) return this.output.copy();
        else return ItemStack.EMPTY;
    }
}
