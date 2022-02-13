package me.daqem.jobsplus.utils.enums;

import net.minecraft.world.item.Items;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public enum ItemTiers implements Tier {

    LEVEL_1(3, 3543, 3.0F, 0.5F, 10, () -> Ingredient.of(Items.IRON_BLOCK)),
    LEVEL_2(3, 5678, 6.0F, 1.0F, 12, () -> Ingredient.of(Items.GOLD_BLOCK)),
    LEVEL_3(4, 12835, 9.0F, 1.5F, 15, () -> Ingredient.of(Items.DIAMOND_BLOCK)),
    LEVEL_4(4, 17754, 12.0F, 2.0F, 20, () -> Ingredient.of(Items.EMERALD_BLOCK));

    private final int harvestLevel;
    private final int maxUses;
    private final float efficiency;
    private final float attackDamage;
    private final int enchantability;
    private final Ingredient repairMaterial;


    ItemTiers(int harvestLevel, int maxUses, float efficiency, float attackDamage, int enchantability, Supplier<Ingredient> repairMaterial) {
        this.harvestLevel = harvestLevel;
        this.maxUses = maxUses;
        this.efficiency = efficiency;
        this.attackDamage = attackDamage;
        this.enchantability = enchantability;
        this.repairMaterial = repairMaterial.get();
    }

    @Override
    public int getUses() {
        return maxUses;
    }

    @Override
    public float getSpeed() {
        return efficiency;
    }

    @Override
    public float getAttackDamageBonus() {
        return attackDamage;
    }

    @Override
    public int getLevel() {
        return harvestLevel;
    }

    @Override
    public int getEnchantmentValue() {
        return enchantability;
    }

    @Override
    public @NotNull Ingredient getRepairIngredient() {
        return repairMaterial;
    }
}
