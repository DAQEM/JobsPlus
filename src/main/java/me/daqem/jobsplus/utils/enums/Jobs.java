package me.daqem.jobsplus.utils.enums;

public enum Jobs {
    ALCHEMIST(0),
    BUILDER(1),
    BUTCHER(2),
    CRAFTSMAN(3),
    DIGGER(4),
    FARMER(5),
    FISHERMAN(6),
    ENCHANTER(7),
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
