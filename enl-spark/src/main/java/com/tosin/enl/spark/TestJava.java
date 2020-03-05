package com.tosin.enl.spark;

import com.tosin.enl.common.time.TimeTranstationUtils;

public class TestJava {
    public static void main(String[] args) {
        String info = TimeTranstationUtils.Date2yyyyMMdd_HHmmss(Long.valueOf("1234569000"+"000"));
        System.out.println(info);
    }
}
