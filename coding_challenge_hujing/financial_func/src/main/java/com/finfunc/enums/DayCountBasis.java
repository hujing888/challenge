package com.finfunc.enums;

/**
 * The type of Day Count Basis
 */
public enum DayCountBasis {
    UsPsa30_360(0),
    ActualActual(1),
    Actual360(2),
    Actual365(3),
    Europ30_360(4);

    private int value;

    DayCountBasis(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
