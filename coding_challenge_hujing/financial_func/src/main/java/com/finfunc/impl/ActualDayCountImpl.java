package com.finfunc.impl;

import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;

import java.util.Calendar;
import java.util.Date;

/***
 *@title Actual360DayCountImpl
 *@description
 *@author HUJING
 *@version 1.0.0
 *@date 2025/3/3 17:47
 **/
public class ActualDayCountImpl extends AbstractDayCount{

    @Override
    public double daysInYear(Date issue, Date settl) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(issue);
        int startYear = calendar.get(Calendar.YEAR);
        calendar.setTime(settl);
        int endYear = calendar.get(Calendar.YEAR);
        double totYears = endYear-startYear;
        if(totYears == 0 && DateUtil.isLeapYear(startYear)){
            return 366D;
        }else if(totYears == 0 && !DateUtil.isLeapYear(startYear)){
            return 365D;
        }
        calendar.setTime(settl);
        calendar.add(Calendar.YEAR,1);
        calendar.set(Calendar.MONTH,0);
        calendar.set(Calendar.DATE,1);
        Date end = calendar.getTime();

        calendar.setTime(issue);
        calendar.set(Calendar.MONTH,0);
        calendar.set(Calendar.DATE,1);
        Date start = calendar.getTime();

        double totDays = DateUtil.between(start,end,DateUnit.DAY);
        return totDays / (totYears+1);
    }
}
