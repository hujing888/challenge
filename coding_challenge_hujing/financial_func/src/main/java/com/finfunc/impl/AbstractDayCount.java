package com.finfunc.impl;

import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import com.finfunc.IDayCount;
import com.finfunc.enums.DayCountBasis;
import com.finfunc.enums.NumDenumPosition;
import com.finfunc.exception.FinancialFuncException;

import java.util.Calendar;
import java.util.Date;

/***
 *@title AbstractDayCount 天数计算抽象类，默认按实际天数/实际天数 DayCountBasis = ActualActual处理
 *@description
 *@author HUJING
 *@version 1.0.0
 *@date 2025/3/3 17:45
 **/
public abstract class AbstractDayCount implements IDayCount {

    public static void main(String[] args) {
        AbstractDayCount abstractDayCount = new Actual360DayCountImpl();
        Date settl = DateUtil.parse("1993-12-30", "yyyy-MM-dd");
        Date mat = DateUtil.parse("2025-01-01", "yyyy-MM-dd");
        System.out.println(abstractDayCount.coupNum(settl,mat,2));
        System.out.println(abstractDayCount.coupPCD(settl,mat,2));
        System.out.println(abstractDayCount.coupNCD(settl,mat,2));
        System.out.println(abstractDayCount.changeMonth(settl,2,true));
    }

    public double coupDays(Date settl, Date mat, int freq) {
        Date pcd = this.coupPCD(settl,mat,freq);
        Date ncd = this.coupNCD(settl,mat,freq);
        return DateUtil.between(pcd, ncd, DateUnit.DAY, true);
    }

    @Override
    public Date coupPCD(Date settl, Date mat, int freq) {
        double num = coupNum(settl,mat,freq);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(mat);
        return changeMonth(mat,-(int)num * freq2Month(freq),DateUtil.isLastDayOfMonth(mat));
        //calendar.add(Calendar.MONTH, -((int)num * freq2Month(freq)));
        //return calendar.getTime();
    }

    @Override
    public Date coupNCD(Date settl, Date mat, int freq) {
        double num = coupNum(settl,mat,freq);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(mat);
        return changeMonth(mat,-(int)(num-1) * freq2Month(freq),DateUtil.isLastDayOfMonth(mat));
        /*calendar.add(Calendar.MONTH, -((int)(num-1) * freq2Month(freq)));
        return calendar.getTime();*/
    }

    @Override
    public double coupNum(Date settl, Date mat, int freq) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(settl);
        int startYear = calendar.get(Calendar.YEAR);
        int startMonth = calendar.get(Calendar.MONTH) + 1;
        int startDay = calendar.get(Calendar.DAY_OF_MONTH);
        boolean isStartDateLastDayOfMonth = DateUtil.isLastDayOfMonth(settl);

        calendar.setTime(mat);
        int endYear = calendar.get(Calendar.YEAR);
        int endMonth = calendar.get(Calendar.MONTH) + 1;
        int endDay = calendar.get(Calendar.DAY_OF_MONTH);

        //都为最后一天，则认为startDay=endDay 处理2月问题
        if(DateUtil.isLastDayOfMonth(settl) && DateUtil.isLastDayOfMonth(mat) ){
            startDay=endDay;
        }

        int totalMonth = (endYear - startYear) * 12 + endMonth - startMonth;
        if (startDay < endDay ) totalMonth += 1;

        return Math.ceil((double)totalMonth / (double)freq2Month(freq));
    }

    @Override
    public double coupDaysBS(Date settl, Date mat, int freq) {
        Date pcd = this.coupPCD(settl,mat,freq);
        return DateUtil.between(pcd, settl, DateUnit.DAY, true);
    }

    @Override
    public double coupDayNC(Date settl, Date mat, int freq) {
        Date ncd = this.coupNCD(settl,mat,freq);
        return DateUtil.between(settl, ncd, DateUnit.DAY, true);
    }

    @Override
    public double daysBetweeen(Date issue, Date settl, NumDenumPosition position) {
        //计算实际天数
        if(NumDenumPosition.Numerator == position){
            return DateUtil.between(issue, settl, DateUnit.DAY, true);
        }else{
            return 0;
        }
    }

    public abstract double daysInYear(Date issue, Date settl);

    @Override
    public Date changeMonth(Date date, int months, boolean returnLastDay) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH,months);
        if(returnLastDay){
            int maxDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
            calendar.set(Calendar.DAY_OF_MONTH,maxDay);
        }
        return calendar.getTime();
    }

    /**
     * @description 支付频率（按月份），如：年付息一次 freq=1，为12个月付息一次
     * @param freq
     **/
    private static int freq2Month(int freq){
        return 12 / freq;
    }



    public static IDayCount getDayCountInstance(DayCountBasis basis) throws FinancialFuncException {
        IDayCount iDayCount;
        switch (basis){
            case Actual360:
                iDayCount = new Actual360DayCountImpl();
                break;
            case Actual365:
                iDayCount = new Actual365DayCountImpl();
                break;
            case ActualActual:
                iDayCount = new ActualDayCountImpl();
                break;
            case Europ30_360:
                iDayCount = new Europ30_360DayCountImpl();
                break;
            case UsPsa30_360:
                iDayCount = new UsPsa30_360DayCountImpl();
                break;
            default:
                throw new FinancialFuncException(basis.name() + "the day count basis type is not exists");

        }
        return iDayCount;
    }
}
