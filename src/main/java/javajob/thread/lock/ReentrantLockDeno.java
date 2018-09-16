package javajob.thread.lock;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Copyright (C)
 *
 * @program: workjob
 * @description:
 * @author: 刘文强  kingcall
 * @create: 2018-08-03 14:11
 **/
public class ReentrantLockDeno {
    private ArrayList<Integer> arrayList = new ArrayList<Integer>();
    static final ReentrantLockDeno test = new ReentrantLockDeno();
    Lock lock = new ReentrantLock();
    public static void main(String[] args) throws InterruptedException {
        test_insert();
    }


    public static void test_insert() throws InterruptedException {
        new Thread(){
            @Override
            public void run() {
                //test.insert(Thread.currentThread());
                test.insert_trylock(Thread.currentThread());
            };
        }.start();

        new Thread(){
            @Override
            public void run() {
                //test.insert(Thread.currentThread());
                test.insert_trylock(Thread.currentThread());
            };
        }.start();
        TimeUnit.SECONDS.sleep(20);
        System.out.println(test.arrayList.size());
    }
    public static void test_insert2() throws InterruptedException {
        ExecutorService es = Executors.newFixedThreadPool(100);
        for (int i=0;i<2;i++){
            es.submit(new Runnable() {
                @Override
                public void run() {
                    test.insert();
                }
            });
        }
        es.shutdown();
        es.awaitTermination(15,TimeUnit.SECONDS);
        System.out.println(test.arrayList.size());
    }

    /**
     * 这种使用方法看起来不那么爽
     * @param thread
     */
    public void insert(Thread thread) {
        //错误的演示 每个线程各自拿到了锁
        //Lock lock = new ReentrantLock();
        lock.lock();
        try {
            System.out.println(thread.getName()+"得到了锁");
            for(int i=0;i<5;i++) {
                TimeUnit.SECONDS.sleep(1);
                arrayList.add(i);
            }
        } catch (Exception e) {
        }finally {
            System.out.println(thread.getName()+"释放了锁");
            lock.unlock();
        }
    }
    // 把该方法传入到了线程的run方法
    public void insert() {
        //错误的演示 每个线程各自拿到了锁
        //Lock lock = new ReentrantLock();
        lock.lock();
        try {
            System.out.println(Thread.currentThread().getName()+"得到了锁");
            for(int i=0;i<5;i++) {
                TimeUnit.SECONDS.sleep(1);
                arrayList.add(i);
            }
        } catch (Exception e) {
        }finally {
            System.out.println(Thread.currentThread().getName()+"释放了锁");
            lock.unlock();
        }
    }
    public void insert_trylock(Thread thread) {
        // 没有等待，立即返回
        if(lock.tryLock()) {
            try {
                System.out.println(thread.getName()+"得到了锁");
                for(int i=0;i<5;i++) {
                    arrayList.add(i);
                }
            } catch (Exception e) {
            }finally {
                System.out.println(thread.getName()+"释放了锁");
                lock.unlock();
            }
        } else {
            System.out.println(thread.getName()+"获取锁失败");
        }
    }
}
