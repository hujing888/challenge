package com.finfunc.functions;

import com.finfunc.IDayCount;
import com.finfunc.enums.DayCountBasis;
import com.finfunc.enums.Frequency;
import com.finfunc.enums.NumDenumPosition;
import com.finfunc.exception.FinancialFuncException;
import com.finfunc.impl.AbstractDayCount;

import java.util.Date;

import static com.finfunc.functions.Common.throwException;

public class Bonds {

    public static double duration(Date settlement, Date maturity, double coupon, double yld, Frequency frequency, DayCountBasis basis, boolean isMDuration) throws FinancialFuncException {
        IDayCount iDayCount = AbstractDayCount.getDayCountInstance(basis);
        int freq = frequency.getValue();
        //付息次数
        double n = iDayCount.coupNum(settlement,maturity,freq);
        //pcd到settl的天数
        double a = iDayCount.coupDaysBS(settlement,maturity,freq);
        //pcd到ncd的天数
        double e = iDayCount.coupDays(settlement,maturity,freq);
        //settl到NCD的天数
        double dsr = e-a;

        double result1 = Math.pow((yld / freq + 1),(dsr / e + n -1));
        if(result1 == 0){
            throwException("result1 = (yld / frequency + 1)^((dsc / e) + n -1)  can not equals 0)");
        }

        double[] acc = new double[]{0,0};
        for(int num = 1; num<= n;num++){
            double tmpResult1 = num - 1. + dsr / e;
            double tmpResult2 = Math.pow(yld / freq + 1,tmpResult1);
            if(tmpResult2 == 0){
                throwException("tmpResult2 = Math.pow(yld / freq + 1,tmpResult1) can not equals 0");
            }
            double tmpResult3 = (100. * coupon / freq) / tmpResult2;
            acc[0] = acc[0] + tmpResult3 * tmpResult1;
            acc[1] = acc[1] + tmpResult3;
        }

        double result2 = ((dsr / e + n -1) * 100. / result1) + acc[0];
        double result3 = 100. / result1 + acc[1];
        if(result3 == 0){
            throwException("result3= 100. / result1 + acc[1] can not equals 0");
        }
        if(!isMDuration){
            return result2 / result3 / freq;
        }else{
            return result2 / result3 / freq / (yld / freq + 1);
        }
    }

    public static double yieldMat(Date settlement, Date maturity, Date issue, double rate, double pr, DayCountBasis basis) throws FinancialFuncException {
        IDayCount iDayCount = AbstractDayCount.getDayCountInstance(basis);
        double b = iDayCount.daysInYear(issue,settlement);
        double dim = iDayCount.daysBetweeen(issue,maturity,NumDenumPosition.Numerator);
        double a = iDayCount.daysBetweeen(issue,settlement,NumDenumPosition.Numerator);
        return (dim / b * rate + 1. - pr / 100. - a / b * rate) / (pr / 100. + a / b * rate) * (b /(dim - a));
    }

    public static double yieldFunc(Date settlement, Date maturity, double rate, double pr, double redemption, int freq, DayCountBasis basis) throws FinancialFuncException {
        IDayCount iDayCount = AbstractDayCount.getDayCountInstance(basis);
        //付息次数
        double n = iDayCount.coupNum(settlement,maturity,freq);
        //pcd到settl的天数
        double a = iDayCount.coupDaysBS(settlement,maturity,freq);
        //pcd到ncd的天数
        double e = iDayCount.coupDays(settlement,maturity,freq);
        //settl到NCD的天数
        //double dsr = iDayCount.coupDayNC(settlement,maturity,freq);
        double dsr = e-a;
        return Yield.yield(n,a,e,dsr,rate,redemption,pr,freq);
    }

}
