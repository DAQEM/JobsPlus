package me.daqem.jobsplus.utils.enums;

public enum CapType {
    LEVEL(0),
    EXP(1),
    POWERUP1(2),
    POWERUP2(3),
    POWERUP3(4);

    private final int value;

    CapType(final int newValue) {
        value = newValue;
    }

    public int get() {return value;}
}
