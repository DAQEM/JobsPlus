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
    SELCTOR_BUTCHER(2),
    SELCTOR_CRAFTSMAN(3),
    SELCTOR_DIGGER(4),
    SELCTOR_FARMER(5),
    SELCTOR_FISHERMAN(6),
    SELCTOR_ENCHANTER(7),
    SELCTOR_HUNTER(8),
    SELCTOR_LUMBERJACK(9),
    SELCTOR_MINER(10),
    SELCTOR_SMITH(11);

    private final int value;

    CapType(final int newValue) {
        value = newValue;
    }

    public int get() {return value;}
}
