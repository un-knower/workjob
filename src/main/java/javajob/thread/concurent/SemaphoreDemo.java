package javajob.thread.concurent;


import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

/**
 * 1.   是一个计数信号量
 *      计数信号量由一个指定数量的 "许可" 初始化。每调用一次 acquire()，一个许可会被调用线程取走。每调用一次 release()，一个许可会被返还给信号量。
 *      因此，在没有任何 release() 调用时，最多有 N 个线程能够通过 acquire() 方法，N 是该信号量初始化时的许可的指定数量。这些许可只是一个简单的计数器。
 * 2.   信号量主要有两种用途：
 *         1. 保护一个重要(代码)部分防止一次超过 N 个线程进入
 *              Semaphore semaphore = new Semaphore(1) 一次只有有个线程进入，就是线程安全的
 *         2. 在两个线程之间发送信号
 *              如果你将一个信号量用于在两个线程之间传送信号，通常你应该用一个线程调用 acquire() 方法，而另一个线程调用 release() 方法。如果没有可用的许可，a
 *              cquire() 调用将会阻塞，直到一个许可被另一个线程释放出来。同理，如果无法往信号量释放更多许可时，一个 release() 调用也会阻塞。
 * 3.   公平
 *          1. 没有办法保证线程能够公平地可从信号量中获得许可。也就是说，无法担保掉第一个调用 acquire() 的线程会是第一个获得一个许可的线程。如果第一个线程在等待一个许可时发生阻塞，
 *          而第二个线程前来索要一个许可的时候刚好有一个许可被释放出来，那么它就可能会在第一个线程之前获得许可。
 *          2. Semaphore semaphore = new Semaphore(1, true);
 *              如果你想要强制公平，Semaphore 类有一个具有一个布尔类型的参数的构造子，通过这个参数以告知 Semaphore 是否要强制公平。强制公平会影响到并发性能
 *
 */
public class SemaphoreDemo {
    static Semaphore semaphore = new Semaphore(1,true);
    public static void main(String[] args) throws InterruptedException {
       show();
    }

    /**
     * 演示公平信号量和普通信号量
     * @throws InterruptedException
     */
    public static void show() throws InterruptedException {
        base_op(new Semaphore(1));
        TimeUnit.SECONDS.sleep(10);
        System.out.println("===============================================================");
        base_op(new Semaphore(1,true));
    }
    //一个线程等待另一个线程
    public static void base_op(Semaphore semaphore) throws InterruptedException {
        new Thread(new Produce(semaphore)).start();
        new Thread(new Consume(semaphore)).start();
        TimeUnit.SECONDS.sleep(7);
    }
}

class Produce extends Thread{
    Semaphore semaphore;

    public Produce(Semaphore semaphore) {
        this.semaphore = semaphore;
    }
    @Override
    public void run() {
        try {
            semaphore.acquire();
            System.out.println("我已经生成了一个苹果，但是你得等到 6 秒后才可以吃");
            TimeUnit.SECONDS.sleep(6);
            semaphore.release();
            semaphore.acquire();
            System.out.println("我已经生产了第二个苹果，你可以吃了不用等待");
            semaphore.release();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
class Consume extends Thread{
    Semaphore semaphore;

    public Consume(Semaphore semaphore) {
        this.semaphore = semaphore;
    }
    @Override
    public void run() {
        try {
            System.out.println("来到了苹果树下");
            semaphore.acquire();
            System.out.println("我已经吃到苹果了,等待消化...............,你 5 秒钟后才可以生产");
            TimeUnit.SECONDS.sleep(5);
            semaphore.release();
            semaphore.acquire();
            System.out.println("苹果我吃饱了，不用生产了");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

