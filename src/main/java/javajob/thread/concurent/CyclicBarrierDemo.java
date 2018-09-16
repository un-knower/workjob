package javajob.thread.concurent;

/**
 * Copyright (C)
 *
 * @program: workjob
 * @description:
 * @author: 刘文强  kingcall
 * @create: 2018-08-03 10:19
 **/

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.TimeUnit;

/**
 * 是一种同步机制，它能够对处理一些算法的线程实现同步。换句话讲，它就是一个所有线程必须等待的一个栅栏，直到所有线程都到达这里，然后所有线程才可以继续做其他事情
 * 通过调用 CyclicBarrier 对象的 await() 方法，两个线程可以实现互相等待。一旦 N 个线程在等待 CyclicBarrier 达成，所有线程将被释放掉去继续运行。
 * 在创建一个 CyclicBarrier 的时候你需要定义有多少线程在被释放之前等待栅栏
 * CyclicBarrier 支持一个栅栏行动，栅栏行动是一个 Runnable 实例，一旦最后等待栅栏的线程抵达，该实例将被执
 * 你也可以为等待线程设定一个超时时间。等待超过了超时时间之后，即便还没有达成 N 个线程等待 CyclicBarrier 的条件，该线程也会被释放出来
 * 名字：栅栏
 */
public class CyclicBarrierDemo {
    public static void main(String[] args) {
        //屏幕栅栏行动
        Runnable runnable1=new Runnable() {
            @Override
            public void run() {
                System.out.println("屏幕相关组件已经生成");
            }
        };
        Runnable runnable2=new Runnable() {
            @Override
            public void run() {
                System.out.println("供能相关组件已经生成");
            }
        };
        CyclicBarrier barrier = new CyclicBarrier(2,runnable1);
        CyclicBarrier barrier2 = new CyclicBarrier(2,runnable2);
        new ProduceAmerica(barrier,barrier2).start();
        new ProduceChain(barrier,barrier2).start();
    }
}

class ProduceAmerica extends Thread{
    CyclicBarrier barrier;
    CyclicBarrier barrier2;

    public ProduceAmerica(CyclicBarrier barrier, CyclicBarrier barrier2) {
        this.barrier = barrier;
        this.barrier2 = barrier2;
    }

    @Override
    public void run() {
        System.out.println(Thread.currentThread().getName()+"  美国场：屏幕生成中.......");
        try {
            TimeUnit.SECONDS.sleep(10);
            //执行到这个地方的时候就会进行栅栏等待
            barrier.await();
        } catch (InterruptedException | BrokenBarrierException e) {
            e.printStackTrace();
        }
        System.out.println(Thread.currentThread().getName()+"  美国场：充电器生成中.......");
        try {
            TimeUnit.SECONDS.sleep(3);
            //执行到这个地方的时候就会进行栅栏等待
            barrier2.await();
        } catch (InterruptedException | BrokenBarrierException e) {
            e.printStackTrace();
        }
    }
}
class ProduceChain extends Thread{
    CyclicBarrier barrier;
    CyclicBarrier barrier2;

    public ProduceChain(CyclicBarrier barrier, CyclicBarrier barrier2) {
        this.barrier = barrier;
        this.barrier2 = barrier2;
    }
    @Override
    public void run() {
        System.out.println(Thread.currentThread().getName()+"   中国场：贴膜生成中.......");
        try {
            TimeUnit.SECONDS.sleep(3);
            // 执行到这个地方的时候就会进行栅栏等待,等待美国场的栅栏
            barrier.await();
        } catch (InterruptedException | BrokenBarrierException e) {
            e.printStackTrace();
        }
        System.out.println(Thread.currentThread().getName()+"   中国场：电线生成中.......");
        try {
            TimeUnit.SECONDS.sleep(5);
            barrier2.await();
        } catch (InterruptedException | BrokenBarrierException e) {
            e.printStackTrace();
        }

    }
}

