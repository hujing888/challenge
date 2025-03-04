package com.finfunc.enums;

/**
 * Specifies whether to switch to straight-line depreciation when depreciation is greater than the declining balance calculation
 */
public enum VdbSwitch {

    DontSwitchToStraightLine(1),
    SwitchToStraightLine(0);

    private int value;

    VdbSwitch(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
