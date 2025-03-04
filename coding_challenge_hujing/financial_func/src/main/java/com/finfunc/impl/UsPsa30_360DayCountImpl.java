package com.finfunc.impl;

import com.finfunc.enums.NumDenumPosition;

import java.util.Calendar;
import java.util.Date;

/***
 *@title Actual360DayCountImpl
 *@description
 *@author HUJING
 *@version 1.0.0
 *@date 2025/3/3 17:47
 **/
public class UsPsa30_360DayCountImpl extends AbstractDayCount{
   /* @Override
    public double coupDays(Date settl, Date mat, int freq) {
        Date pcd = super.coupPCD(settl,mat,freq);
        Date ncd = super.coupNCD(settl,mat,freq);
        return getUsPsaDays(pcd,ncd);
    }*/

    @Override
    public double coupDays(Date settl, Date mat, int freq) {
        return 360D/freq;
    }

    @Override
    public double coupDaysBS(Date settl, Date mat, int freq) {
        Date pcd = super.coupPCD(settl,mat,freq);
        return getUsPsaDays(pcd,settl);
    }

    @Override
    public double coupDayNC(Date settl, Date mat, int freq) {
        Date ncd = super.coupNCD(settl,mat,freq);
        return getUsPsaDays(settl,ncd);
    }

    @Override
    public double daysBetweeen(Date issue, Date settl, NumDenumPosition position) {
        return getUsPsaDays(issue,settl);
    }
    @Override
    public double daysInYear(Date issue, Date settl) {
        return 360D;
    }

    private double getUsPsaDays(Date startDate,Date endDate){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(startDate);
        int startYear = calendar.get(Calendar.YEAR);
        int startMonth = calendar.get(Calendar.MONTH)+1;
        int startDay = calendar.get(Calendar.DAY_OF_MONTH);
        boolean isStartDateLastDayOfFeb = startMonth == 2 && startDay == calendar.getActualMaximum(Calendar.DAY_OF_MONTH);;

        calendar.setTime(endDate);
        int endYear = calendar.get(Calendar.YEAR);
        int endMonth = calendar.get(Calendar.MONTH)+1;
        int endDay = calendar.get(Calendar.DAY_OF_MONTH);
        boolean isEndDateLastDayOfFeb = endMonth == 2 && endDay == calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

        if(isEndDateLastDayOfFeb && isStartDateLastDayOfFeb ){
            endDay = 30;
        }

        if(endDay == 31 && startDay >= 30){
            endDay = 30;
        }

        if(startDay == 31 || isStartDateLastDayOfFeb){
            startDay = 30;
        }

        return ((endYear - startYear) * 12 + endMonth - startMonth) * 30 + endDay - startDay;
    }
}
