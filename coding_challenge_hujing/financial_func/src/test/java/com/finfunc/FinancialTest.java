package com.finfunc;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.IoUtil;
import com.finfunc.enums.DayCountBasis;
import com.finfunc.enums.Frequency;
import com.finfunc.exception.FinancialFuncException;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class FinancialTest {

    @Test
    public void testYield() throws FinancialFuncException {
        Date settlement = DateUtil.parse("1993-12-31", "yyyy-MM-dd");
        Date maturity = DateUtil.parse("2025-05-01", "yyyy-MM-dd");
        double rate = 0.05;
        double pr = 75;
        double redemption = 100;

        double actualValue = Financial.yield(settlement, maturity, rate, pr, redemption, Frequency.SemiAnnual, DayCountBasis.Actual365);
        Assert.assertEquals(0.069728868D, actualValue, 0.0001);
    }

    @Test
    public void testYieldMat() throws FinancialFuncException {
        Date settlement = DateUtil.parse("1993-12-31", "yyyy-MM-dd");
        Date maturity = DateUtil.parse("2000-02-28", "yyyy-MM-dd");
        Date issue = DateUtil.parse("1993-02-28", "yyyy-MM-dd");

        double rate = 0.07;
        double pr = 75;

        double actualValue = Financial.yieldMat(settlement, maturity, issue, rate, pr, DayCountBasis.ActualActual);
        Assert.assertEquals(0.136710295, actualValue, 0.0001);
    }

    @Test
    public void testDuration() throws FinancialFuncException {
        Date settlement = DateUtil.parse("1980-02-15", "yyyy-MM-dd");
        Date maturity = DateUtil.parse("2000-02-28", "yyyy-MM-dd");
        double coupon = 100;
        double yld = 0.03;

        double actualValue = Financial.duration(DateUtil.beginOfDay(settlement), DateUtil.beginOfDay(maturity), coupon, yld, Frequency.Annual, DayCountBasis.Europ30_360);
        Assert.assertEquals(8.963062287, actualValue, 0.0001);
    }

    @Test
    public void testMduration() throws FinancialFuncException {
        Date settlement = DateUtil.parse("1993-12-31", "yyyy-MM-dd");
        Date maturity = DateUtil.parse("2010-06-30", "yyyy-MM-dd");
        double coupon = 100;
        double yld = 0.03;

        double actualValue = Financial.mDuration(DateUtil.beginOfDay(settlement), DateUtil.beginOfDay(maturity), coupon, yld, Frequency.Annual, DayCountBasis.Europ30_360);
        Assert.assertEquals(7.716108992, actualValue, 0.0001);
    }

    @Test
    public void batchTestYieldMat() throws Exception {
        List<String> contentLines = new ArrayList<>();
        IoUtil.readLines(this.getClass().getResourceAsStream("/testdata/yieldmat.test"), "UTF-8", contentLines);
        for(String line : contentLines) {
            String[] parts = line.split("\\,");
            Date settlement = DateUtil.parse(parts[0], "MM/dd/yyyy HH:mm:ss");
            Date maturity = DateUtil.parse(parts[1], "MM/dd/yyyy HH:mm:ss");
            Date issue = DateUtil.parse(parts[2], "MM/dd/yyyy HH:mm:ss");

            double rate = Double.parseDouble(parts[3]);
            double pr = Double.parseDouble(parts[4]);
            DayCountBasis basis = DayCountBasis.valueOf(parts[5]);

            double expectValue = Double.parseDouble(parts[6]);
            double actualValue = Financial.yieldMat(DateUtil.beginOfDay(settlement), DateUtil.beginOfDay(maturity), DateUtil.beginOfDay(issue), rate, pr, basis);
            Assert.assertEquals(line, expectValue, actualValue, 0.0001);
            System.out.println(line + "    actual:" + String.valueOf(actualValue) + "    pass!");
        }
    }

    @Test
    public void batchTestMDuration() throws Exception {
        List<String> contentLines = new ArrayList<>();
        IoUtil.readLines(this.getClass().getResourceAsStream("/testdata/mduration.test"), "UTF-8", contentLines);
        for(String line : contentLines) {
            String[] parts = line.split("\\,");
            Date settlement = DateUtil.parse(parts[0], "MM/dd/yyyy HH:mm:ss");
            Date maturity = DateUtil.parse(parts[1], "MM/dd/yyyy HH:mm:ss");
            double coupon = Double.parseDouble(parts[2]);
            double yld = Double.parseDouble(parts[3]);

            Frequency frequency = Frequency.valueOf(parts[4]);
            DayCountBasis basis = DayCountBasis.valueOf(parts[5]);

            double expectValue = Double.parseDouble(parts[6]);
            double actualValue = Financial.mDuration(DateUtil.beginOfDay(settlement), DateUtil.beginOfDay(maturity), coupon, yld, frequency, basis);
            Assert.assertEquals(line, expectValue, actualValue, 0.0001);
            System.out.println(line + "    actual:" + String.valueOf(actualValue) + "    pass!");
        }
    }

    @Test
    public void batchTestDuration() throws Exception {
        List<String> contentLines = new ArrayList<>();
        IoUtil.readLines(this.getClass().getResourceAsStream("/testdata/duration.test"), "UTF-8", contentLines);
        for(String line : contentLines) {
            String[] parts = line.split("\\,");
            Date settlement = DateUtil.parse(parts[0], "MM/dd/yyyy HH:mm:ss");
            Date maturity = DateUtil.parse(parts[1], "MM/dd/yyyy HH:mm:ss");
            double coupon = Double.parseDouble(parts[2]);
            double yld = Double.parseDouble(parts[3]);

            Frequency frequency = Frequency.valueOf(parts[4]);
            DayCountBasis basis = DayCountBasis.valueOf(parts[5]);

            double expectValue = Double.parseDouble(parts[6]);
            double actualValue = Financial.duration(DateUtil.beginOfDay(settlement), DateUtil.beginOfDay(maturity), coupon, yld, frequency, basis);
            Assert.assertEquals(line, expectValue, actualValue, 0.0001);
            System.out.println(line + "    actual:" + String.valueOf(actualValue) + "    pass!");
        }
    }
}
