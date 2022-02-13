package me.daqem.jobsplus.capability;

public interface ISuperPowerCapability {

    int[] getSuperpower();

    void setSuperpower(int[] value);

    void copyForRespawn(SuperPowerCapabilityImpl oldStore);
}
