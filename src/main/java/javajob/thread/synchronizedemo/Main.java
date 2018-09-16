package javajob.thread.synchronizedemo;

/**
 * Copyright (C)
 *
 * @program: workjob
 * @description:
 * @author: 刘文强  kingcall
 * @create: 2018-08-01 17:51
 **/
class Sync {
    public synchronized void test() {
        synchronized(this) {
            System.out.println("test开始..");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("test结束..");
        }
    }
}

class MyThread extends Thread {
    @Override
    public void run() {
        Sync sync = new Sync();
        sync.test();
    }
}
class MyThread2 extends Thread {

    private Sync sync;

    public MyThread2(Sync sync) {
        this.sync = sync;
    }

    @Override
    public void run() {
        sync.test();
    }
}

public class Main {
    // 错误示例
    public static void test1() {
        for (int i = 0; i < 3; i++) {
            Thread thread = new MyThread();
            thread.start();
        }
    }

    public static void test2() {
        Sync sync=new Sync();
        for (int i = 0; i < 3; i++) {
            Thread thread = new MyThread2(sync);
            thread.start();
        }
    }
    public static void main(String[] args) {
        test1();

    }
}
