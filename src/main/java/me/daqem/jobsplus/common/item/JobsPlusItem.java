package me.daqem.jobsplus.common.item;

import me.daqem.jobsplus.utils.JobGetters;
import me.daqem.jobsplus.utils.enums.Jobs;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.*;

public class JobsPlusItem extends Item {

    public final Jobs job;
    public int requiredLevel = -1;

    public JobsPlusItem(Properties properties, Jobs job) {
        super(properties);
        this.job = job;
    }

    public int getRequiredLevel() {
        if (requiredLevel == -1) this.requiredLevel = JobGetters.getRequiredJobLevelForItem(this.getDefaultInstance());
        return requiredLevel;
    }

    public Jobs getJob() {
        return job;
    }

    public static class PickAxe extends PickaxeItem {

        public final Jobs job;
        public int requiredLevel = -1;

        public PickAxe(Tier p_42961_, int attackDamage, float attackSpeed, Properties properties, Jobs job) {
            super(p_42961_, attackDamage, attackSpeed, properties);
            this.job = job;
        }

        public int getRequiredLevel() {
            if (requiredLevel == -1)
                this.requiredLevel = JobGetters.getRequiredJobLevelForItem(this.getDefaultInstance());
            return requiredLevel;
        }

        public Jobs getJob() {
            return job;
        }
    }

    public static class Axe extends AxeItem {

        public final Jobs job;
        public int requiredLevel = -1;

        public Axe(Tier tier, float attackDamage, float attackSpeed, Properties properties, Jobs job) {
            super(tier, attackDamage, attackSpeed, properties);
            this.job = job;
        }

        public int getRequiredLevel() {
            if (requiredLevel == -1)
                this.requiredLevel = JobGetters.getRequiredJobLevelForItem(this.getDefaultInstance());
            return requiredLevel;
        }

        public Jobs getJob() {
            return job;
        }
    }

    public static class Shovel extends ShovelItem {

        public final Jobs job;
        public int requiredLevel = -1;

        public Shovel(Tier tier, float attackDamage, float attackSpeed, Properties properties, Jobs job) {
            super(tier, attackDamage, attackSpeed, properties);
            this.job = job;
        }

        public int getRequiredLevel() {
            if (requiredLevel == -1)
                this.requiredLevel = JobGetters.getRequiredJobLevelForItem(this.getDefaultInstance());
            return requiredLevel;
        }

        public Jobs getJob() {
            return job;
        }
    }

    public static class Hoe extends HoeItem {

        public final Jobs job;
        public int requiredLevel = -1;

        public Hoe(Tier tier, int attackDamage, float attackSpeed, Properties properties, Jobs job) {
            super(tier, attackDamage, attackSpeed, properties);
            this.job = job;
        }

        public int getRequiredLevel() {
            if (requiredLevel == -1)
                this.requiredLevel = JobGetters.getRequiredJobLevelForItem(this.getDefaultInstance());
            return requiredLevel;
        }

        public Jobs getJob() {
            return job;
        }
    }

    public static class Sword extends SwordItem {

        public final Jobs job;
        public int requiredLevel = -1;

        public Sword(Tier tier, int attackDamage, float attackSpeed, Properties properties, Jobs job) {
            super(tier, attackDamage, attackSpeed, properties);
            this.job = job;
        }

        public int getRequiredLevel() {
            if (requiredLevel == -1)
                this.requiredLevel = JobGetters.getRequiredJobLevelForItem(this.getDefaultInstance());
            return requiredLevel;
        }

        public Jobs getJob() {
            return job;
        }
    }

    public static class Bow extends BowItem {

        public final Jobs job;
        public int requiredLevel = -1;

        public Bow(Properties properties, Jobs job) {
            super(properties);
            this.job = job;
        }


        public int getRequiredLevel() {
            if (requiredLevel == -1)
                this.requiredLevel = JobGetters.getRequiredJobLevelForItem(this.getDefaultInstance());
            return requiredLevel;
        }

        public Jobs getJob() {
            return job;
        }
    }

    public static class Rod extends FishingRodItem {

        public final Jobs job;
        public int requiredLevel = -1;

        public Rod(Properties properties, Jobs job) {
            super(properties);
            this.job = job;
        }


        public int getRequiredLevel() {
            if (requiredLevel == -1)
                this.requiredLevel = JobGetters.getRequiredJobLevelForItem(this.getDefaultInstance());
            return requiredLevel;
        }

        public Jobs getJob() {
            return job;
        }
    }

    public static class Armor extends ArmorItem {

        public final Jobs job;
        public int requiredLevel = -1;

        public Armor(ArmorMaterial armorMaterial, EquipmentSlot equipmentSlot, Properties properties, Jobs job) {
            super(armorMaterial, equipmentSlot, properties);
            this.job = job;
        }

        public int getRequiredLevel() {
            if (requiredLevel == -1)
                this.requiredLevel = JobGetters.getRequiredJobLevelForItem(this.getDefaultInstance());
            return requiredLevel;
        }

        public Jobs getJob() {
            return job;
        }
    }

    public static class ExperienceBottle extends ExperienceBottleItem {

        public final Jobs job;
        public int requiredLevel = -1;

        public ExperienceBottle(Properties properties, Jobs job) {
            super(properties);
            this.job = job;
        }

        public int getRequiredLevel() {
            if (requiredLevel == -1)
                this.requiredLevel = JobGetters.getRequiredJobLevelForItem(this.getDefaultInstance());
            return requiredLevel;
        }

        public void setRequiredLevel(int requiredLevel) {
            this.requiredLevel = requiredLevel;
        }

        public Jobs getJob() {
            return job;
        }
    }
}
