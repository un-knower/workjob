package javajob.enumdemo;

/**
 * Copyright (C)
 *
 * @program: workjob
 * @description:
 * @author: 刘文强  kingcall
 * @create: 2018-07-24 10:49
 **/
public class EnumSeason_test {
    public static void main(String[] args) {
        System.out.println(EnumSeason.SPRING);
        System.out.println(EnumSeason.SPRING.getName());
        test(EnumSeason.SPRING);
    }

    public static void test(EnumSeason season) {
        System.out.println(season.getName());
    }
}
