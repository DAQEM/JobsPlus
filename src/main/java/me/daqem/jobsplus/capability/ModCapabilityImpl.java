package me.daqem.jobsplus.capability;

import me.daqem.jobsplus.JobsPlus;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;

public class ModCapabilityImpl implements IModCapability {

    public static final ResourceLocation ID = JobsPlus.getId("jobs");

    @CapabilityInject(IModCapability.class)
    public static Capability<IModCapability> MOD_CAPABILITY = null;

    private int[] farmerMap = new int[]{0, 0, 0, 0, 0};
    private int[] butcherMap = new int[]{0, 0, 0, 0, 0};
    private int[] minerMap = new int[]{0, 0, 0, 0, 0};
    private int[] lumberjackMap = new int[]{0, 0, 0, 0, 0};
    private int[] builderMap = new int[]{0, 0, 0, 0, 0};
    private int[] diggerMap = new int[]{0, 0, 0, 0, 0};
    private int[] hunterMap = new int[]{0, 0, 0, 0, 0};
    private int[] alchemistMap = new int[]{0, 0, 0, 0, 0};
    private int[] enchanterMap = new int[]{0, 0, 0, 0, 0};
    private int[] craftsmanMap = new int[]{0, 0, 0, 0, 0};
    private int[] fishermanMap = new int[]{0, 0, 0, 0, 0};
    private int[] smithMap = new int[]{0, 0, 0, 0, 0};
    private int coins = 0;
    private int[] verification = new int[]{0, 0, 0, 0, 0};
    private int[] selector = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
    private int display = 0;

    @Override
    public int[] getFarmer() {
        return this.farmerMap;
    }

    @Override
    public void setFarmer(int[] value) {
        this.farmerMap = value;
    }

    @Override
    public int[] getButcher() {
        return this.butcherMap;
    }

    @Override
    public void setButcher(int[] value) {
        this.butcherMap = value;
    }

    @Override
    public int[] getMiner() {
        return this.minerMap;
    }

    @Override
    public void setMiner(int[] value) {
        this.minerMap = value;
    }

    @Override
    public int[] getLumberjack() {
        return this.lumberjackMap;
    }

    @Override
    public void setLumberjack(int[] value) {
        this.lumberjackMap = value;
    }

    @Override
    public int[] getBuilder() {
        return this.builderMap;
    }

    @Override
    public void setBuilder(int[] value) {
        this.builderMap = value;
    }

    @Override
    public int[] getDigger() {
        return this.diggerMap;
    }

    @Override
    public void setDigger(int[] value) {
        this.diggerMap = value;
    }

    @Override
    public int[] getHunter() {
        return this.hunterMap;
    }

    @Override
    public void setHunter(int[] value) {
        this.hunterMap = value;
    }

    @Override
    public int[] getAlchemist() {
        return this.alchemistMap;
    }

    @Override
    public void setAlchemist(int[] value) {
        this.alchemistMap = value;
    }

    @Override
    public int[] getEnchanter() {
        return this.enchanterMap;
    }

    @Override
    public void setEnchanter(int[] value) {
        this.enchanterMap = value;
    }

    @Override
    public int[] getCraftsman() {
        return this.craftsmanMap;
    }

    @Override
    public void setCraftsman(int[] value) {
        this.craftsmanMap = value;
    }

    @Override
    public int[] getFisherman() {
        return this.fishermanMap;
    }

    @Override
    public void setFisherman(int[] value) {
        this.fishermanMap = value;
    }

    @Override
    public int[] getSmith() {
        return this.smithMap;
    }

    @Override
    public void setSmith(int[] value) {
        this.smithMap = value;
    }

    @Override
    public int getCoins() { return this.coins; }

    @Override
    public void setCoins(int value) { this.coins = value; }

    @Override
    public int[] getVerification() { return this.verification; }

    @Override
    public void setVerification(int[] value) { this.verification = value; }

    @Override
    public int[] getSelector() { return this.selector; }

    @Override
    public void setSelector(int[] value) { this.selector = value; }

    @Override
    public int getDisplay() { return display; }

    @Override
    public void setDisplay(int value) { this.display = value; }

    @Override
    public void copyForRespawn(ModCapabilityImpl oldStore) {
        this.farmerMap = oldStore.farmerMap;
        this.butcherMap = oldStore.butcherMap;
        this.minerMap = oldStore.minerMap;
        this.lumberjackMap = oldStore.lumberjackMap;
        this.builderMap = oldStore.builderMap;
        this.diggerMap = oldStore.diggerMap;
        this.hunterMap = oldStore.hunterMap;
        this.alchemistMap = oldStore.alchemistMap;
        this.enchanterMap = oldStore.enchanterMap;
        this.craftsmanMap = oldStore.craftsmanMap;
        this.fishermanMap = oldStore.fishermanMap;
        this.smithMap = oldStore.smithMap;
        this.coins = oldStore.coins;
        this.verification = oldStore.verification;
        this.selector = oldStore.selector;
        this.display = oldStore.display;
    }

    public CompoundTag serializeNBT() {
        final CompoundTag nbt = new CompoundTag();
        nbt.putIntArray("farmer", this.getFarmer());
        nbt.putIntArray("butcher", this.getButcher());
        nbt.putIntArray("miner", this.getMiner());
        nbt.putIntArray("lumberjack", this.getLumberjack());
        nbt.putIntArray("builder", this.getBuilder());
        nbt.putIntArray("digger", this.getDigger());
        nbt.putIntArray("hunter", this.getHunter());
        nbt.putIntArray("alchemist", this.getAlchemist());
        nbt.putIntArray("enchanter", this.getEnchanter());
        nbt.putIntArray("craftsman", this.getCraftsman());
        nbt.putIntArray("fisherman", this.getFisherman());
        nbt.putIntArray("smith", this.getSmith());
        nbt.putInt("coins", this.getCoins());
        nbt.putIntArray("verification", this.getVerification());
        nbt.putIntArray("selector", this.getSelector());
        nbt.putInt("display", this.getDisplay());
        return nbt;
    }

    public void deserializeNBT(CompoundTag nbt) {
        this.setFarmer(nbt.getIntArray("farmer"));
        this.setButcher(nbt.getIntArray("butcher"));
        this.setMiner(nbt.getIntArray("miner"));
        this.setLumberjack(nbt.getIntArray("lumberjack"));
        this.setBuilder(nbt.getIntArray("builder"));
        this.setDigger(nbt.getIntArray("digger"));
        this.setHunter(nbt.getIntArray("hunter"));
        this.setAlchemist(nbt.getIntArray("alchemist"));
        this.setEnchanter(nbt.getIntArray("enchanter"));
        this.setCraftsman(nbt.getIntArray("craftsman"));
        this.setFisherman(nbt.getIntArray("fisherman"));
        this.setSmith(nbt.getIntArray("smith"));
        this.setCoins(nbt.getInt("coins"));
        this.setVerification(nbt.getIntArray("verification"));
        this.setSelector(nbt.getIntArray("selector"));
        this.setDisplay(nbt.getInt("display"));
    }
}