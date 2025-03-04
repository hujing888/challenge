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
    public void testYield1() throws FinancialFuncException {
        Date settlement = DateUtil.parse("2024-12-01", "yyyy-MM-dd");
        Date maturity = DateUtil.parse("2025-05-01", "yyyy-MM-dd");
        double rate = 0.05;
        double pr = 75;
        double redemption = 100;

        double actualValue = Financial.yield(settlement, maturity, rate, pr, redemption, Frequency.SemiAnnual, DayCountBasis.ActualActual);
        Assert.assertEquals(0.861026611, actualValue, 0.0001);
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
    public void testYieldMat2() throws FinancialFuncException {
        Date settlement = DateUtil.parse("1993-12-31", "yyyy-MM-dd");
        Date maturity = DateUtil.parse("1995-11-30", "yyyy-MM-dd");
        Date issue = DateUtil.parse("1993-02-28", "yyyy-MM-dd");

        double rate = 0.07;
        double pr = 75;

        double actualValue = Financial.yieldMat(settlement, maturity, issue, rate, pr, DayCountBasis.UsPsa30_360);
        Assert.assertEquals(0.2481350652412, actualValue, 0.0001);
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

        //跟EXCEL中入参有出入，重新调整下
        //double actualValue = Financial.mDuration(DateUtil.beginOfDay(settlement), DateUtil.beginOfDay(maturity), coupon, yld, Frequency.Annual, DayCountBasis.Europ30_360);
        double actualValue = Financial.mDuration(DateUtil.beginOfDay(settlement), DateUtil.beginOfDay(maturity), coupon, yld, Frequency.SemiAnnual, DayCountBasis.ActualActual);
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


    //异常测试用例
    @Test
    public void testYieldException() throws Exception{

        Date settlement = DateUtil.parse("1993-12-31", "yyyy-MM-dd");
        Date maturity = DateUtil.parse("2025-05-01", "yyyy-MM-dd");
        double rate = 0.05;
        double pr = 75;
        double redemption = 100;
        try{
            Financial.yield(null, maturity, rate, pr, redemption, Frequency.SemiAnnual, DayCountBasis.Actual365);
        }catch (FinancialFuncException e){
            Assert.assertEquals("settlement must not be null",e.getMessage());
        }

        try{
            Financial.yield(settlement, null, rate, pr, redemption, Frequency.SemiAnnual, DayCountBasis.Actual365);
        }catch (FinancialFuncException e){
            Assert.assertEquals("maturity must not be null",e.getMessage());
        }

        try{
            Date settlement2 = DateUtil.parse("2026-12-31", "yyyy-MM-dd");
            Financial.yield(settlement2, maturity, 2, pr, redemption, Frequency.SemiAnnual, DayCountBasis.Actual365);
        }catch (FinancialFuncException e){
            Assert.assertEquals("maturity must be after settlement",e.getMessage());
        }

        try{
            Financial.yield(settlement, maturity, -1, pr, redemption, Frequency.SemiAnnual, DayCountBasis.Actual365);
        }catch (FinancialFuncException e){
            Assert.assertEquals("rate must not be negative",e.getMessage());
        }

        try{
            Financial.yield(settlement, maturity, rate, -1, redemption, Frequency.SemiAnnual, DayCountBasis.Actual365);
        }catch (FinancialFuncException e){
            Assert.assertEquals("pr must be more than 0",e.getMessage());
        }

        try{
            Financial.yield(settlement, maturity, rate, pr, -1, Frequency.SemiAnnual, DayCountBasis.Actual365);
        }catch (FinancialFuncException e){
            Assert.assertEquals("redemption must be more than 0",e.getMessage());
        }

    }
}
