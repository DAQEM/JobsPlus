package me.daqem.jobsplus.common.capability;

public interface ISuperPowerCapability {

    int[] getSuperpower();

    void setSuperpower(int[] value);

    void copyForRespawn(SuperPowerCapabilityImpl oldStore);
}
