package com.fml;


import cn.hutool.core.util.StrUtil;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;
import java.util.StringJoiner;


public class FmlParser {

    private final static char CHARACTER_ADD = '+';
    private final static char CHARACTER_SUBTRACT = '-';
    private final static char CHARACTER_MULTIPLY = '*';
    private final static char CHARACTER_DIVIDE = '/';
    private final static char CHARACTER_LEFT_PARENTHESIS = '(';
    private final static char CHARACTER_RIGHT_PARENTHESIS = ')';
    private final static char CHARACTER_COMMA = ',';
    private final static char CHARACTER_SEMICOLON = ';';
    private final static char CHARACTER_EQUAL = '=';
    private final static char CHARACTER_POINT = '.';
    private final static String STR_SPACE = " ";
    private final static String STR_EMPTY = "";

    public FmlParser(String fml) {
        this.fml = fml;
    }

    public FmlParser() {
    }

    private String fml;

    public static void main(String[] args) throws Exception{
        FmlParser fmlParser = new FmlParser("A * B + ( 1 + (C+A+B) / 4 );A = 3; B = 5; C = A + B");
        System.out.println(fmlParser.calculate(null));
    }

    /**
     * @param str
     * @return java.lang.Double
     * @throws
     * @description 四则混合运算
     * @author HUJING
     * @date 2025/3/3 10:26
     **/
    public BigDecimal calculate(String str){
        //1.若有入参，则以入参作为四则运算字符
        fml = StrUtil.isNotBlank(str) ? str : fml;
        //2.处理四则运算字符，进行分割以及变量替换
        delExpression();
        //3.校验四则运算表达式
        checkExpression();
        //4.返回计算结果
        return getResult();
    }

    /**
     * @description 处理字符串，得到可执行的表达式
     * @param
     * @return
     * @throws
     * @author HUJING
     * @date 2025/3/3 11:16
     **/
    private void delExpression() {
        //不包含分号，则是普通算式，快速返回 如：1 + 2 * 3
        if(StrUtil.isBlank(fml)) return;
        if (!fml.contains(String.valueOf(CHARACTER_SEMICOLON))) {
            fml = fml.replaceAll(STR_SPACE, STR_EMPTY);
            return;
        }
        //包含分号，则是需要进行变量替换的算式(以分号分割字符串) 如：A = 3; B = 5; C = A + B; A * B + ( 1 + C / 4 )
        String[] strArr = fml.replaceAll(STR_SPACE, STR_EMPTY).split(String.valueOf(CHARACTER_SEMICOLON));
        //Map存储变量数据 key=变量名，value=变量值，如key=A1,value=2
        Map<String, String> map = new HashMap<>();
        for (String str : strArr) {
            //不包含等号，则是最终计算表达式 如：A * B + ( 1 + C / 4 )
            if (!str.contains(String.valueOf(CHARACTER_EQUAL))) {
                fml = str.replaceAll(STR_SPACE, STR_EMPTY);
                continue;
            }
            //等号分割字符串
            String[] tmp = str.split(String.valueOf(CHARACTER_EQUAL));
            //等式左右不成立，直接丢弃 如 A= , =B , = 等
            if (null == tmp || tmp.length < 2) {
                continue;
            }
            String key = tmp[0].replaceAll(STR_SPACE, STR_EMPTY);
            String value = tmp[1].replaceAll(STR_SPACE, STR_EMPTY);
            //若是变量表达式，则替换表达式中所有变量为具体数字，并在前后拼接（），如：(3+5)
            if (isExpression(value)) {
                for (String k : map.keySet()) {
                    value = value.replaceAll(k, map.get(k));
                }
                map.put(key, CHARACTER_LEFT_PARENTHESIS + value + CHARACTER_RIGHT_PARENTHESIS);
            } else {
                map.put(key, value);
            }
        }

        //遍历变量数据map，将变量全量替换成数字
        for (String key : map.keySet()) {
            fml = fml.replaceAll(key, map.get(key));
        }
    }

    //校验表达式是否有误
    private void checkExpression(){
        if(StrUtil.isBlank(fml)){
            throw new RuntimeException("the fml is blank,please check it!");
        }
        char[] charArr = fml.toCharArray();
        int rightCount=0,leftCount=0;
        for(char cc :charArr){
            if(CHARACTER_RIGHT_PARENTHESIS == cc) {
                rightCount++;
                continue;
            }
            if(CHARACTER_LEFT_PARENTHESIS == cc){
                leftCount++;
                continue;
            }
            if(!Character.isDigit(cc) && !isOperator(cc) && !(CHARACTER_POINT == cc)){
                throw new RuntimeException("the fml contains illegal characters,please check it!");
            }
        }
        if (rightCount != leftCount) {
            throw new RuntimeException("the right parenthesis count is not equals left parenthesis count,please check it!");
        }
    }

    //获取计算结果
    private BigDecimal getResult() {
        String operateStack = getOperateStackInfo();
        Stack stk = new Stack();
        String parts[] = operateStack.split(String.valueOf(CHARACTER_COMMA));
        BigDecimal result = new BigDecimal(0);
        for (String part : parts) {
            char tmp = part.charAt(0);
            //数字类型直接出栈
            if (!isOperator(tmp)) {
                stk.push(part);
            } else {
                BigDecimal a = new BigDecimal(String.valueOf(stk.pop()));
                BigDecimal b = new BigDecimal(String.valueOf(stk.pop()));
                result = calculate(b, a, tmp);
                //将计算后的结果重新压入栈
                stk.push(String.valueOf(result));
            }
        }
        return result;
    }

    /**
     * @description 获取操作数栈信息
     * @param
     * @return
     * @throws
     * @author HUJING
     * @date 2025/3/3 11:07
     **/
    private String getOperateStackInfo() {
        Stack stk = new Stack();
        //最终结果以逗号分割，如：1 + 2 * 3 分割成 1,2,3,*,+
        StringJoiner joiner = new StringJoiner(String.valueOf(CHARACTER_COMMA));
        char[] charArr = fml.toCharArray();
        for (int i = 0; i < charArr.length; i++) {
            char cc = charArr[i];
            //处理整数及小数
            if (Character.isDigit(cc) || fml.charAt(i) == CHARACTER_POINT) {
                String tmpStr = STR_EMPTY;
                do {
                    tmpStr += charArr[i++];
                } while (i < charArr.length && (Character.isDigit(charArr[i]) || fml.charAt(i) == CHARACTER_POINT));
                i--;
                joiner.add(tmpStr);
            } else {
                switch (cc) {
                    case CHARACTER_LEFT_PARENTHESIS:
                        stk.push(String.valueOf(cc));
                        break;
                    //右括号到左括号之间的数据全部出栈
                    case CHARACTER_RIGHT_PARENTHESIS:
                        while ((!stk.isEmpty())
                                && (!String.valueOf(CHARACTER_LEFT_PARENTHESIS).equals(stk.peek()))) {
                            joiner.add(String.valueOf(stk.pop()));
                        }
                        stk.pop();
                        break;
                    //加减法为同一操作级别
                    case CHARACTER_ADD:
                    case CHARACTER_SUBTRACT:
                        while ((!stk.isEmpty())
                                && (!String.valueOf(CHARACTER_LEFT_PARENTHESIS).equals(stk.peek()))) {
                            joiner.add((String) stk.pop());
                        }
                        stk.push(String.valueOf(cc));
                        break;
                    //乘除法为同一操作级别
                    case CHARACTER_MULTIPLY:
                    case CHARACTER_DIVIDE:
                        //栈顶为乘除的，直接出栈
                        while ((!stk.empty())
                                && ((String.valueOf(CHARACTER_MULTIPLY).equals(stk.peek()))
                                    || (String.valueOf(CHARACTER_DIVIDE).equals(stk.peek())))) {
                            joiner.add((String) stk.pop());
                        }
                        stk.push(String.valueOf(cc));
                        break;
                }
            }
        }

        //将所有未出栈的数据，出栈拼接
        while (!stk.isEmpty()) {
            joiner.add(String.valueOf(stk.pop()));
        }
        return joiner.toString();
    }

    //判断是否是操作字符
    private boolean isOperator(char op) {
        return (op == CHARACTER_ADD || op == CHARACTER_SUBTRACT || op == CHARACTER_MULTIPLY || op == CHARACTER_DIVIDE);
    }

    //判断是否为计算表达式
    private boolean isExpression(String str) {
        return (StrUtil.isNotBlank(str) && (str.contains(String.valueOf(CHARACTER_ADD))
                || str.contains(String.valueOf(CHARACTER_SUBTRACT))
                || str.contains(String.valueOf(CHARACTER_MULTIPLY))
                || str.contains(String.valueOf(CHARACTER_DIVIDE))));
    }

    //执行加减乘除运算
    private BigDecimal calculate(BigDecimal a, BigDecimal b, char op) {
        switch (op) {
            case CHARACTER_ADD:
                return a.add(b);
            case CHARACTER_SUBTRACT:
                return a.subtract(b);
            case CHARACTER_MULTIPLY:
                return a.multiply(b);
            case CHARACTER_DIVIDE:
                return a.divide(b);
        }
        return new BigDecimal(-1);
    }

}