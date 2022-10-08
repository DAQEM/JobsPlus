package me.daqem.jobsplus.common.item;

import me.daqem.jobsplus.utils.JobGetters;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.*;

public class JobsPlusItem extends Item {

    public int requiredLevel = -1;

    public JobsPlusItem(Properties properties) {
        super(properties);
    }

    public int getRequiredLevel() {
        if (requiredLevel == -1) this.requiredLevel = JobGetters.getRequiredJobLevelForItem(this.getDefaultInstance());
        return requiredLevel;
    }

    public static class PickAxe extends PickaxeItem {

        public int requiredLevel = -1;

        public PickAxe(Tier p_42961_, int attackDamage, float attackSpeed, Properties p_42964_) {
            super(p_42961_, attackDamage, attackSpeed, p_42964_);
        }

        public int getRequiredLevel() {
            if (requiredLevel == -1)
                this.requiredLevel = JobGetters.getRequiredJobLevelForItem(this.getDefaultInstance());
            return requiredLevel;
        }
    }

    public static class Axe extends AxeItem {

        public int requiredLevel = -1;

        public Axe(Tier tier, float attackDamage, float attackSpeed, Properties properties) {
            super(tier, attackDamage, attackSpeed, properties);
        }

        public int getRequiredLevel() {
            if (requiredLevel == -1)
                this.requiredLevel = JobGetters.getRequiredJobLevelForItem(this.getDefaultInstance());
            return requiredLevel;
        }
    }

    public static class Shovel extends ShovelItem {

        public int requiredLevel = -1;

        public Shovel(Tier tier, float attackDamage, float attackSpeed, Properties properties) {
            super(tier, attackDamage, attackSpeed, properties);
        }

        public int getRequiredLevel() {
            if (requiredLevel == -1)
                this.requiredLevel = JobGetters.getRequiredJobLevelForItem(this.getDefaultInstance());
            return requiredLevel;
        }
    }

    public static class Hoe extends HoeItem {

        public int requiredLevel = -1;

        public Hoe(Tier tier, int attackDamage, float attackSpeed, Properties properties) {
            super(tier, attackDamage, attackSpeed, properties);
        }

        public int getRequiredLevel() {
            if (requiredLevel == -1)
                this.requiredLevel = JobGetters.getRequiredJobLevelForItem(this.getDefaultInstance());
            return requiredLevel;
        }
    }

    public static class Sword extends SwordItem {

        public int requiredLevel = -1;

        public Sword(Tier tier, int attackDamage, float attackSpeed, Properties properties) {
            super(tier, attackDamage, attackSpeed, properties);
        }

        public int getRequiredLevel() {
            if (requiredLevel == -1)
                this.requiredLevel = JobGetters.getRequiredJobLevelForItem(this.getDefaultInstance());
            return requiredLevel;
        }
    }

    public static class Bow extends BowItem {

        public int requiredLevel = -1;

        public Bow(Properties properties) {
            super(properties);
        }


        public int getRequiredLevel() {
            if (requiredLevel == -1)
                this.requiredLevel = JobGetters.getRequiredJobLevelForItem(this.getDefaultInstance());
            return requiredLevel;
        }
    }

    public static class Rod extends FishingRodItem {

        public int requiredLevel = -1;

        public Rod(Properties properties) {
            super(properties);
        }


        public int getRequiredLevel() {
            if (requiredLevel == -1)
                this.requiredLevel = JobGetters.getRequiredJobLevelForItem(this.getDefaultInstance());
            return requiredLevel;
        }
    }

    public static class Armor extends ArmorItem {

        public int requiredLevel = -1;

        public Armor(ArmorMaterial armorMaterial, EquipmentSlot equipmentSlot, Properties properties) {
            super(armorMaterial, equipmentSlot, properties);
        }

        public int getRequiredLevel() {
            if (requiredLevel == -1)
                this.requiredLevel = JobGetters.getRequiredJobLevelForItem(this.getDefaultInstance());
            return requiredLevel;
        }
    }

    public static class ExperienceBottle extends ExperienceBottleItem {

        public int requiredLevel = -1;

        public ExperienceBottle(Properties properties) {
            super(properties);
        }

        public int getRequiredLevel() {
            if (requiredLevel == -1)
                this.requiredLevel = JobGetters.getRequiredJobLevelForItem(this.getDefaultInstance());
            return requiredLevel;
        }

        public void setRequiredLevel(int requiredLevel) {
            this.requiredLevel = requiredLevel;
        }
    }
}
