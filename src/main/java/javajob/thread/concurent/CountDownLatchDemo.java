package javajob.thread.concurent;

/**
 * Copyright (C)
 *
 * @program: workjob
 * @description:
 * @author: 刘文强  kingcall
 * @create: 2018-08-03 10:07
 **/

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * CountDownLatch 以一个给定的数量初始化。countDown() 每被调用一次，这一数量就减一。通过调用 await() 方法之一，线程可以阻塞等待这一数量到达零。
 *      这个对象本身可以等待（等待其值为零，利用这个特性让一个线程进行了等待）
 *  名字：倒计时+ 门栓
 */
public class CountDownLatchDemo {
    public static void main(String[] args) throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(3);
        Waiter  waiter  = new Waiter(latch);
        Decrementer decrementer = new Decrementer(latch);
        new Thread(waiter).start();
        new Thread(decrementer).start();
        TimeUnit.SECONDS.sleep(10);
    }
}

class Waiter implements Runnable{
    CountDownLatch latch = null;
    public Waiter(CountDownLatch latch) {
        this.latch = latch;
    }
    @Override
    public void run() {
        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Waiter Released");
    }
}
class Decrementer implements Runnable {
    CountDownLatch latch = null;
    public Decrementer(CountDownLatch latch) {
        this.latch = latch;
    }
    @Override
    public void run() {
        try {
            Thread.sleep(1000);
            this.latch.countDown();
            System.out.println("还得等待："+latch.getCount()+" 秒");
            Thread.sleep(1000);
            this.latch.countDown();
            System.out.println("还得等待："+latch.getCount()+" 秒");
            Thread.sleep(1000);
            this.latch.countDown();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

