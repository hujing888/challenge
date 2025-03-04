package com.finfunc.enums;

/**
 * Indicates when payments are due (end/beginning of period)
 */
public enum PaymentDue {
    EndOfPeriod(0),
    BeginningOfPeriod(1);

    private int value;

    PaymentDue(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}