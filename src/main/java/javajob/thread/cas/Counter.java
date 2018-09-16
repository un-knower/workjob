package javajob.thread.cas;

import parquet.org.slf4j.Logger;
import parquet.org.slf4j.LoggerFactory;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Copyright (C)
 *
 * @program: workjob
 * @description:演示线程不安全的例子
 * @author: 刘文强  kingcall
 * @create: 2018-08-01 18:50
 **/

public class Counter {
    private static final Logger LOGGER = LoggerFactory.getLogger(Counter.class);
    public static int count = 0;
    public static Random random = new Random();

    public static void main(String[] args) throws InterruptedException {
        test2();
    }


    static class add extends Thread {
        @Override
        public void run() {
            System.out.println("线程被启动");
            try {
                // 你获得的值的大小取决于每个线程的休息时间
                TimeUnit.SECONDS.sleep(random.nextInt(10));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Counter.count += 5;
        }

    }
    /**
     * 示例 1
     *
     * @throws InterruptedException
     */
    public static void test1() throws InterruptedException {
        for (int i = 0; i < 100; i++) {
            new Thread(new add()).start();
        }

        TimeUnit.SECONDS.sleep(5);
        System.out.println(Counter.count);
    }

    // 示例 2
    public static void test2() throws InterruptedException {
        ExecutorService es = Executors.newFixedThreadPool(100);
        for (int i = 0; i < 200; i++) {
            // 下面是lambda 表达式的方法引用，发现使用起来挺方便的，至少看起来不那么突兀，而且感觉很高级,发现对象的方法不可以，出现了混淆
            es.submit(Counter::inc);
        }
        es.shutdown();
        es.awaitTermination(8, TimeUnit.SECONDS);
        System.out.println(Counter.count);
    }
    public static void inc() {
        try {
            TimeUnit.SECONDS.sleep(random.nextInt(5));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        count++;
    }
}
