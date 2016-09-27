package com.wang.constant;

/**
 * by 王荣俊  on 2016/6/30.
 */
public class HttpUrl {

    public static final String exampleIsbn = "9787111128069";

    public static String searchBookUrl(String isbn) {
        return "https://api.douban.com/v2/book/isbn/:" + isbn;
    }

}
