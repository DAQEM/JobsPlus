package me.daqem.jobsplus.utils.enums;

public enum CapType {
    LEVEL(0),
    EXP(1),
    POWER_UP1(2),
    POWER_UP2(3),
    POWER_UP3(4),

    START_VERIFICATION_FREE(0),
    START_VERIFICATION_PAID(1),
    STOP_VERIFICATION_FREE(2),
    STOP_VERIFICATION_PAID(3),
    POWER_UP_VERIFICATION(4),

    SELECTOR_ALCHEMIST(0),
    SELECTOR_BUILDER(1),
    SELECTOR_DIGGER(2),
    SELECTOR_ENCHANTER(3),
    SELECTOR_FARMER(4),
    SELECTOR_FISHERMAN(5),
    SELECTOR_HUNTER(6),
    SELECTOR_LUMBERJACK(7),
    SELECTOR_MINER(8),
    SELECTOR_SMITH(9);

    private final int value;

    CapType(final int newValue) {
        value = newValue;
    }

    public int get() {
        return value;
    }
}
