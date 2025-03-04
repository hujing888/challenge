package com.fml;


import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;

/**
 * @program: fml-parser
 * @description
 * test1-test3 普通表达式+ -运算，支持小数
 * test4-test6 普通表达式*运算，乘加，乘减，支持小数
 * test7-test9 普通表达式/运算，除加，除减，支持小数
 * test10 普通表达式+ - * /计算
 * test11-test15 复杂表达式+ - * /计算
 * test16-test18 异常捕获
 * @author: HUJING
 * @create: 2025/03/03 12:26
 * @version: 1.0
 **/
public class FmlParserTest {

    @Test
    public void test1() {
        String expression = "1 + 23";
        FmlParser fmlParser = new FmlParser(expression);
        BigDecimal result = fmlParser.calculate(null);
        Assert.assertEquals(24, result.doubleValue(), 0);
        System.out.println(expression + " = " + result);
    }

    @Test
    public void test2() {
        String expression = "400 - 3 ";
        FmlParser fmlParser = new FmlParser(expression);
        BigDecimal result = fmlParser.calculate(null);
        Assert.assertEquals(397, result.doubleValue(), 0);
        System.out.println(expression + " = " + result);
    }

    @Test
    public void test3() {
        String expression = "1.5 + 2.86 - 3";
        FmlParser fmlParser = new FmlParser(expression);
        BigDecimal result = fmlParser.calculate(null);
        Assert.assertEquals(1.36, result.doubleValue(), 0);
        System.out.println(expression + " = " + result);
    }

    @Test
    public void test4() {
        String expression = "2 * 3";
        FmlParser fmlParser = new FmlParser(expression);
        BigDecimal result = fmlParser.calculate(null);
        Assert.assertEquals(6, result.doubleValue(), 0);
        System.out.println(expression + " = " + result);
    }

    @Test
    public void test5() {
        String expression = "5 *(1+2+3+(10-5+(1+2)))";
        FmlParser fmlParser = new FmlParser(expression);
        BigDecimal result = fmlParser.calculate(null);
        Assert.assertEquals(70, result.doubleValue(), 0);
        System.out.println(expression + " = " + result);
    }

    @Test
    public void test6() {
        String expression = "3 * ( 5.6 + 4.5 )";
        FmlParser fmlParser = new FmlParser(expression);
        BigDecimal result = fmlParser.calculate(null);
        Assert.assertEquals(30.3, result.doubleValue(), 0);
        System.out.println(expression + " = " + result);
    }

    @Test
    public void test7() {
        String expression = "4/2";
        FmlParser fmlParser = new FmlParser(expression);
        BigDecimal result = fmlParser.calculate(null);
        Assert.assertEquals(2, result.doubleValue(), 0);
        System.out.println(expression + " = " + result);
    }

    @Test
    public void test8() {
        String expression = " 3 / 4 + 5.2";
        FmlParser fmlParser = new FmlParser(expression);
        BigDecimal result = fmlParser.calculate(null);
        Assert.assertEquals(5.95, result.doubleValue(), 0);
        System.out.println(expression + " = " + result);
    }

    @Test
    public void test9() {
        String expression = " 12 / ( 2 + 5 -1)";
        FmlParser fmlParser = new FmlParser(expression);
        BigDecimal result = fmlParser.calculate(null);
        Assert.assertEquals(2, result.doubleValue(), 0);
        System.out.println(expression + " = " + result);
    }

    @Test
    public void test10() {
        String expression = " 3 / 4 + ( 5.2 -1.5 ) * 2";
        FmlParser fmlParser = new FmlParser(expression);
        BigDecimal result = fmlParser.calculate(null);
        Assert.assertEquals(8.15, result.doubleValue(), 0);
        System.out.println(expression + " = " + result);
    }

    @Test
    public void test11() {
        String expression = "A1 = 2; B2 = 11; A1+B2 ";
        FmlParser fmlParser = new FmlParser(expression);
        BigDecimal result = fmlParser.calculate(null);
        Assert.assertEquals(13, result.doubleValue(), 0);
        System.out.println(expression + " = " + result);
    }

    @Test
    public void test12() {
        String expression = "A = 3; B = 5; C = A + B; A * B + ( 1 + C / 4 )";
        FmlParser fmlParser = new FmlParser(expression);
        BigDecimal result = fmlParser.calculate(null);
        Assert.assertEquals(18, result.doubleValue(), 0);
        System.out.println(expression + " = " + result);
    }

    @Test
    public void test13() {
        String expression = "A = 3; B = 5; C = A * B; A * B + ( 1 + C / 4 )";
        FmlParser fmlParser = new FmlParser(expression);
        BigDecimal result = fmlParser.calculate(null);
        Assert.assertEquals(19.75, result.doubleValue(), 0);
        System.out.println(expression + " = " + result);
    }

    @Test
    public void test14() {
        String expression = "A * B + ( 1 + C / 4 ) - A / B; A = 3; B = 5; C = A * B";
        FmlParser fmlParser = new FmlParser(expression);
        BigDecimal result = fmlParser.calculate(null);
        Assert.assertEquals(19.15, result.doubleValue(), 0);
        System.out.println(expression + " = " + result);
    }

    @Test
    public void test15() {
        String expression = "A=20;B=30.5;C=22;D=500;E=D*(C/(A+B));D-A+10/5+5-2*6-3+((10+10/2)*20 -300)";
        FmlParser fmlParser = new FmlParser(expression);
        BigDecimal result = fmlParser.calculate(null);
        Assert.assertEquals(472, result.doubleValue(), 0);
        System.out.println(expression + " = " + result);
    }

    @Test
    public void test16() {
        //传入空字符串
        try {
            FmlParser fmlParser = new FmlParser();
            fmlParser.calculate(null);
        } catch (RuntimeException e) {
            Assert.assertEquals("the fml is blank,please check it!", e.getMessage());
        }
    }

    @Test
    public void test17() {
        //左右括号不相等
        try {
            String expression = "( ( 1 + 3 / 2 ) -1";
            FmlParser fmlParser = new FmlParser(expression);
            fmlParser.calculate(null);
        } catch (RuntimeException e) {
            Assert.assertEquals("the right parenthesis count is not equals left parenthesis count,please check it!", e.getMessage());
        }
    }

    @Test
    public void test18() {
        //左右括号不相等
        try {
            String expression = "1 + A - 0.5 ";
            FmlParser fmlParser = new FmlParser(expression);
            fmlParser.calculate(null);
        } catch (RuntimeException e) {
            Assert.assertEquals("the fml contains illegal characters,please check it!", e.getMessage());
        }
    }

}
