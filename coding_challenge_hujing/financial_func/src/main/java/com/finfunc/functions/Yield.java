package com.finfunc.functions;

import com.finfunc.exception.FinancialFuncException;

import static com.finfunc.functions.Common.PRECISION;
import static com.finfunc.functions.Common.throwException;

/***
 *@title Yield计算专用类  参考网上代码
 *@description
 *@author HUJING
 *@version 1.0.0
 *@date 2025/3/4 12:44
 **/
public class Yield {

    public static Double yield(double n,double a,double e,double dsr,double rate,double redemption,double pr,int freq) throws FinancialFuncException{
        if(n <= 1){
            //付息期间<=1，无需迭代
            return ((redemption / 100 + rate / freq) / (pr / 100 + (a * rate / e /freq)) - 1) * freq * e / dsr;
        }
        double guess = 0.06;
        //牛顿拉斐尔迭代
        Double result = Yield.newton(n,a,e,dsr,guess,rate,redemption,pr,freq);
        if(!Double.isNaN(result)){
            return result;
        }
        //牛顿迭代失败，先确定收益范围，再使用二分查找
        double[] bounds = findBounds(n,a,e,dsr,guess,rate,redemption,pr,freq,-1,Double.MAX_VALUE);
        return bisection(n,a,e,dsr,rate,redemption,pr,freq,bounds[0],bounds[1]);
    }

    //牛顿迭代
    private static Double newton(double n,double a,double e,double dsr,double guess,double rate,double redemption,double pr,int freq){
        int count = 0;
        while (count < Common.DEFAULT_NEWTON_COUNT){
            double fx = price(n,a,e,dsr,guess,rate,redemption,freq) - pr;
            double rguess = guess + PRECISION;
            double lguess = guess - PRECISION;
            double rfx = price(n,a,e,dsr,rguess,rate,redemption,freq) - pr;
            double lfx = price(n,a,e,dsr,lguess,rate,redemption,freq) - pr;
            double Fx = (rfx - lfx)/(2*PRECISION);
            double newGuess = guess - (fx / Fx);
            if(Common.areEqual(newGuess,guess)){
                return newGuess;
            }
            guess = newGuess;
            count++;
        }
        //迭代超过最大次数，精度仍然不满足要求，返回NAN
        return Double.NaN;
    }

    private static double price(double n,double a,double e,double dsr,double guess,double rate,double redemption,int freq){
        double coupon = 100 * rate / freq;
        double accrInt = 100 * rate / freq * a / e;
        double pvOfRedemption = redemption / Math.pow(1 + guess / freq, (n - 1 + dsr / e));
        double sumPvOfCoupons = 0D;
        for (int i = 1; i<= n; i++){
            sumPvOfCoupons  = sumPvOfCoupons + coupon / Math.pow(1 + guess / freq, (i - 1 + dsr / e));
        }
        if(n == 1){
            return (redemption + coupon) / (1. + dsr / e * guess / freq) - accrInt;
        }
        return pvOfRedemption + sumPvOfCoupons - accrInt;
    }

    private static double[] findBounds(double n,double a,double e,double dsr,double guess,double rate,
                                      double redemption,double pr,int freq,double minBond,double maxBond)throws FinancialFuncException {
        if (guess <= minBond || guess >= maxBond) {
            throwException(String.format("guess needs to be between %f and %f", minBond, maxBond));
        }
        //最多遍历60次
        int maxTry = 60;
        double factor = 1.6;
        double shift = 0.01;
        double lguess = guess - shift;
        double rguess = guess + shift;

        while (true) {
            double lower = lguess <= minBond ? minBond : lguess;
            double upper = rguess >= maxBond ? maxBond : rguess;
            if (maxTry <= 0) {
                throwException(String.format("Not found an interval comprising the root after 60 tries, last tried was (%f, %f)", lower, upper));
            }
            double fx = price(n, a, e, dsr, lower, rate, redemption, freq) - pr;
            double fy = price(n, a, e, dsr, upper, rate, redemption, freq) - pr;
            if (fx * fy <= 0) {
                return new double[]{lower, upper};
            } else {
                lguess = lower + factor * (lower - upper);
                rguess = upper + factor * (upper - lower);
            }
            maxTry = maxTry - 1;
        }
    }

    //二分查找
    private static double bisection(double n,double a,double e,double dsr,double rate,
                                   double redemption,double pr,int freq,double lower,double upper) throws FinancialFuncException{
        if(lower == upper){
            throwException(String.format("(lower=upper=%f) impossible to start bisection",lower));
        }
        //最多遍历200次
        int maxCount = 200;
        while (true){
            if(maxCount <= 0){
                throwException("No root found in 200 iterations");
            }
            double fx = price(n, a, e, dsr, lower, rate, redemption, freq) - pr;
            if(Common.areEqual(fx,PRECISION)){
                return lower;
            }else{
                double fy = price(n, a, e, dsr, upper, rate, redemption, freq) - pr;
                if(Common.areEqual(fy,PRECISION)){
                    return upper;
                }else{
                    if(fx * fy > 0){
                        throwException(String.format("(%f,%f) don't bracket the root",lower,upper));
                    }
                    double midvalue = lower + 0.5 * (upper - lower);
                    double fmid = price(n, a, e, dsr, midvalue, rate, redemption, freq) - pr;
                    if(Common.areEqual(fmid,PRECISION)){
                        return midvalue;
                    }
                    if(fx * fmid < 0){
                        upper = midvalue;
                    }else if(fx * fmid > 0 ){
                        lower = midvalue;
                    }else {
                        throwException("Bisection: It should never get here");
                    }
                }
            }
            maxCount--;
        }
    }
}
