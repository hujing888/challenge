package com.finfunc;

import com.finfunc.enums.NumDenumPosition;

import java.util.Date;

public interface IDayCount {
    double coupDays(Date settl, Date mat, int freq);
    Date coupPCD(Date settl, Date mat, int freq);
    Date coupNCD(Date settl, Date mat, int freq);
    double coupNum(Date settl, Date mat, int freq);
    double coupDaysBS(Date settl, Date mat, int freq);
    double coupDayNC(Date settl, Date mat, int freq);
    double daysBetweeen(Date issue, Date settl, NumDenumPosition position);
    double daysInYear(Date issue, Date settl);
    Date changeMonth(Date date, int months, boolean returnLastDay);
}
