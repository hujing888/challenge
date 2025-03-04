package com.finfunc.enums;

/**
 * Indicates whether accrued interest is computed from issue date (by default) or first interest to settlement
 */
public enum AccrIntCalcMethod {

    FromFirstToSettlement(0),
    FromIssueToSettlement(1);

    private int value;

    AccrIntCalcMethod(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
