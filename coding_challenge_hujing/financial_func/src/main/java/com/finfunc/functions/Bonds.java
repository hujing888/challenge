package com.finfunc.functions;

import com.finfunc.enums.DayCountBasis;
import com.finfunc.enums.Frequency;
import com.finfunc.exception.FinancialFuncException;

import java.util.Date;

public class Bonds {

    public static double duration(Date settlement, Date maturity, double coupon, double yld, Frequency frequency, DayCountBasis basis, boolean isMDuration) throws FinancialFuncException {
        // TODO
        return Double.NaN;
    }

    public static double yieldMat(Date settlement, Date maturity, Date issue, double rate, double pr, DayCountBasis basis) throws FinancialFuncException {
        // TODO
        return Double.NaN;
    }

    public static double yieldFunc(Date settlement, Date maturity, double rate, double pr, double redemption, int freq, DayCountBasis basis) throws FinancialFuncException {
        // TODO
        return Double.NaN;
    }
}
