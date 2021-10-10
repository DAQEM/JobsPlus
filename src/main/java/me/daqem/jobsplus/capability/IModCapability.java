package me.daqem.jobsplus.capability;

public interface IModCapability {

    int[] getFarmer();
    void setFarmer(int[] value);

    int[] getButcher();
    void setButcher(int[] value);

    int[] getMiner();
    void setMiner(int[] value);

    int[] getLumberjack();
    void setLumberjack(int[] value);

    int[] getBuilder();
    void setBuilder(int[] value);

    int[] getDigger();
    void setDigger(int[] value);

    int[] getHunter();
    void setHunter(int[] value);

    int[] getAlchemist();
    void setAlchemist(int[] value);

    int[] getEnchanter();
    void setEnchanter(int[] value);

    int[] getCraftsman();
    void setCraftsman(int[] value);

    int[] getFisherman();
    void setFisherman(int[] value);

    int[] getSmith();
    void setSmith(int[] value);

    int getCoins();
    void setCoins(int value);

    int[] getVerification();
    void setVerification(int[] value);

    int[] getSelector();
    void setSelector(int[] value);

    void copyForRespawn(ModCapabilityImpl oldStore);
}