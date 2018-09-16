package test;

import java.util.Calendar;

/**
 * Copyright (C)
 *
 * @program: workjob
 * @description:
 * @author: 刘文强  kingcall
 * @create: 2018-07-26 13:03
 **/
public class ss {
    public static void main(String[] args) {
        long now = System.currentTimeMillis() / 1000L;
        long daySecond = 60 * 60 * 24;
        long dayTime = now - (now + 8 * 3600) % daySecond;
        System.out.println(dayTime + 24 * 60 * 60);
        Calendar calendar = Calendar.getInstance();
        op("ss");
    }

    public static void op(String s) {
        System.out.println("szd");

    }
}
