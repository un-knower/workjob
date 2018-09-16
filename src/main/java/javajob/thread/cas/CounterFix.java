package javajob.thread.cas;

import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Copyright (C)
 *
 * @program: workjob
 * @description:线程不安全示例即及修正
 * @author: 刘文强  kingcall
 * @create: 2018-08-01 17:22
 **/

public class CounterFix {
    private static class MyCount1 {
        static int count = 0;
    }
    private static class MyCount2 {
        static AtomicInteger count = new AtomicInteger();
    }
    public static void add_common(){
        try {
            TimeUnit.SECONDS.sleep(new Random(5).nextInt(5));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        MyCount1.count++;
    }
    public static void add_atomic() {
        try {
            TimeUnit.SECONDS.sleep(new Random(5).nextInt(5));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        MyCount2.count.getAndIncrement();
    }
    public static void common(){
        for (int i = 1; i <= 100; i++) {
            new Thread(CounterFix::add_common).start();
        }
    }
    public static void atomic() {
        for (int i = 1; i <= 100; i++) {
            new Thread(CounterFix::add_atomic).start();
        }
    }
    public static void main(String[] args) throws InterruptedException {
        common();
        atomic();
        TimeUnit.SECONDS.sleep(10);
        System.out.println(MyCount1.count);
        System.out.println(MyCount2.count.get());
    }


}
