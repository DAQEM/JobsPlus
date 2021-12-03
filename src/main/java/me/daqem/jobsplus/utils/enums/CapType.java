package me.daqem.jobsplus.utils.enums;

public enum CapType {
    LEVEL(0),
    EXP(1),
    POWERUP1(2),
    POWERUP2(3),
    POWERUP3(4),

    START_VERIFICATION_FREE(0),
    START_VERIFICATION_PAID(1),
    STOP_VERIFICATION_FREE(2),
    STOP_VERIFICATION_PAID(3),
    POWER_UP_VERIFICATION(4),

    SELCTOR_ALCHEMIST(0),
    SELCTOR_BUILDER(1),
    SELCTOR_DIGGER(2),
    SELCTOR_FARMER(3),
    SELCTOR_FISHERMAN(4),
    SELCTOR_ENCHANTER(5),
    SELCTOR_HUNTER(6),
    SELCTOR_LUMBERJACK(7),
    SELCTOR_MINER(8),
    SELCTOR_SMITH(9);

    private final int value;

    CapType(final int newValue) {
        value = newValue;
    }

    public int get() {return value;}
}
