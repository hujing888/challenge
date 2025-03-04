package com.finfunc.functions;

import com.finfunc.exception.FinancialFuncException;

import java.math.BigDecimal;

public class Common {
    private final static double PRECISION = 1e-6;

    public boolean areEqual(double x, double y) {
        if (Math.abs(x - y) < PRECISION) {
            return true;
        } else {
            return false;
        }
    }

    public double ln(double x) {
        return Math.log(x);
    }

    public double sign(double x) {
        return Math.signum(x);
    }

    public double idem(double x) {
        return x;
    }

    public double min(double x, double y) {
        return Math.min(x, y);
    }

    public double max(double x, double y) {
        return Math.max(x, y);
    }

    public double rest(double x) {
        return BigDecimal.valueOf(x).divideAndRemainder(BigDecimal.ONE)[1].doubleValue();
    }

    public double ceiling(double x) {
        return Math.ceil(x);
    }

    public double floor(double x) {
        return Math.floor(x);
    }

    public double pow(double x, double y) {
        return Math.pow(x, y);
    }

    public double sqr(double x) {
        return Math.sqrt(x);
    }

    public double log10(double x) {
        return Math.log10(x);
    }

    public static void throwException(String s) throws FinancialFuncException {
        throw new FinancialFuncException(s);
    }

    public static void elesThrow(String s, boolean expectTrue) throws FinancialFuncException {
        if(!expectTrue) {
            throwException(s);
        }
    }
}
