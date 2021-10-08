package me.daqem.jobsplus.utils.enums;

public enum CapType {
    ENABLED(0),
    LEVEL(1),
    EXP(2),
    POWERUP1(3),
    POWERUP2(4),
    POWERUP3(5);

    private final int value;

    CapType(final int newValue) {
        value = newValue;
    }

    public int get() {return value;}
}
