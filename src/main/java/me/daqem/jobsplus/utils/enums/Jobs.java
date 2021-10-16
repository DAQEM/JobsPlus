package me.daqem.jobsplus.utils.enums;

public enum Jobs {
    ALCHEMIST(0),
    BUILDER(1),
    BUTCHER(2),
    CRAFTSMAN(3),
    DIGGER(4),
    ENCHANTER(5),
    FARMER(6),
    FISHERMAN(7),
    HUNTER(8),
    LUMBERJACK(9),
    MINER(10),
    SMITH(11);

    private final int value;

    Jobs(final int newValue) {
        value = newValue;
    }

    public int get() {return value;}
}
