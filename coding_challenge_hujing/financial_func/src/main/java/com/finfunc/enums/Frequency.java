package com.finfunc.enums;

/**
 * The number of coupon payments per year
 */
public enum Frequency {
    Annual(1),
    SemiAnnual(2),
    Quarterly(4);

    private int value;

    Frequency(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
