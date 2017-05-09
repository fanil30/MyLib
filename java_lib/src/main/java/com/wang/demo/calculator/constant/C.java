package com.wang.demo.calculator.constant;

import java.io.File;

public class C {

    public static String path = new File("").getAbsolutePath();
    public static String dataFileName = "signList.txt";
    public static String binDir = C.path + "/cache/";
    public static String javaFilePath = C.path + "/cache/" + "Fun.java";
    public static String classFilePath = C.path + "/cache/" + "Fun.class";
    public static String helpFilePath = C.path + "/help.txt";

    public static String classFileName = "Fun";

    public static int UNKNOW_SIGN = -3;
    public static int UNKNOW_ERROR = -4;
    public static int ZERO = -2;

    public static int OK = 3;
    public static int ERROR = -1;

    public static int SQSTACK_MAXSIZE = 100;

    public static String preCode = "public class Fun implements com.wang.demo.calculator.util.Compiler.FunInterface{"
            + " @Override " + "public double fun(double n1, double n2) {";

    public static String postCode = "}}";

    public static String TIP_PRIOR_ERROR = "请输入正确的优先级";
    public static String TIP_SIGN_ERROR = "运算符只能是一个字符，且不能是数字，小数点，括号和小写字母";
    public static String TIP_SIGN_EXISTED_ERROR = "运算符已存在";
    public static String TIP_COMPILE_ERROR = "编译出错,无法保存";
    public static String TIP_ERROR = "请输入正确的信息";
    public static String TIP_NEW_SUCCESS = "新建成功";
    public static String TIP_UPDATE_SUCCESS = "修改成功";

    public static String MSG_ERROR = "表达式或赋值出错";


    public static String welcomeWord = "欢迎使用英俊计算机 v1.0";
    public static String programmerInfo = "作者：小梦游记\nQQ:915249493";

    public static String tip1_1 = "注：1.运算符只能是一个字符，且不能";
    public static String tip1_2 = "           是数字，小数点，括号和小写字母";
    public static String tip2_1 = "        2.选择单目时，只有n1有效，且优先级";
    public static String tip2_2 = "           必须比所有已定义的双目运算符高";

    public static int maxPrior = 20;
    public static int minPrior = 1;

}
