package com.finfunc;

import com.finfunc.enums.NumDenumPosition;

import java.util.Date;

public interface IDayCount {
    /**
     * @description E=结算日所在付息期的天数
     * @param settl 结算日
     * @param mat 到期日
     * @param freq 结算频率
     **/
    double coupDays(Date settl, Date mat, int freq);

    /**
     * @description 结算日之前的上一个付息日
     * @param settl 结算日
     * @param mat 到期日
     * @param freq 结算频率
     **/
    Date coupPCD(Date settl, Date mat, int freq);

    /**
     * @description 结算日之后下一个付息日
     * @param settl 结算日
     * @param mat 到期日
     * @param freq 结算频率
     **/
    Date coupNCD(Date settl, Date mat, int freq);

    /**
     * @description 结算日和到期日之间的付息次数，向上取整
     * @param settl 结算日
     * @param mat 到期日
     * @param freq 结算频率
     **/
    double coupNum(Date settl, Date mat, int freq);

    /**
     * @description 从付息期开始到结算日的天数(pcd到settl的天数) A
     * @param settl 结算日
     * @param mat 到期日
     * @param freq 结算频率
     **/
    double coupDaysBS(Date settl, Date mat, int freq);

    /**
     * @description 结算日到下一次付息日之间的天数(settl到NCD的天数) DSR=E-A
     * @param settl 结算日
     * @param mat 到期日
     * @param freq 结算频率
     **/
    double coupDayNC(Date settl, Date mat, int freq);

    /**
     * @description
     * @param settl 结算日
     * @param issue 到期日
     * @param position 结算频率
     **/
    double daysBetweeen(Date issue, Date settl, NumDenumPosition position);

    /**
     * @description 一年平均天数
     * @param issue 结算日
     * @param settl 到期日
     **/
    double daysInYear(Date issue, Date settl);

    /**
     * @description 对date增加months个月，returnLastDay为true时，设置日期为当月最后一天
     * @param date 结算日
     * @param months 到期日
     * @param returnLastDay 结算评率
     **/
    Date changeMonth(Date date, int months, boolean returnLastDay);
}
