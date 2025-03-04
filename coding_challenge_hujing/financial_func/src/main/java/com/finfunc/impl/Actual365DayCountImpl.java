package com.finfunc.impl;

import java.util.Date;

/***
 *@title Actual360DayCountImpl
 *@description
 *@author HUJING
 *@version 1.0.0
 *@date 2025/3/3 17:47
 **/
public class Actual365DayCountImpl extends AbstractDayCount{

    @Override
    public double coupDays(Date settl, Date mat, int freq) {
        return 365D/freq;
    }

    @Override
    public double daysInYear(Date issue, Date settl) {
        return 365D;
    }
}
