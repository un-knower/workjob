package javajob.thread.lock.readwrite;


import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 关于多线程读取是可以不加锁的，下面之所以加上是为了展示效果
 */
public class ReentrantReadWriteLockDemo {
    private ReentrantReadWriteLock rwl = new ReentrantReadWriteLock();
    public static void main(String[] args) {
        synchronized_op();
    }
    // 效果的展示可能需要多次运行，之所以会有效果是运行有多个线程进行读取
    public static void lock_op() {
        final ReentrantReadWriteLockDemo test = new ReentrantReadWriteLockDemo();
        new Thread(){
            @Override
            public void run() {
                test.lockget(Thread.currentThread());
            };
        }.start();

        new Thread(){
            @Override
            public void run() {
                test.lockget(Thread.currentThread());
            };
        }.start();
    }
    public static void synchronized_op() {
        final ReentrantReadWriteLockDemo test = new ReentrantReadWriteLockDemo();
        new Thread(){
            @Override
            public void run() {
                test.get(Thread.currentThread());
            };
        }.start();

        new Thread(){
            @Override
            public void run() {
                test.get(Thread.currentThread());
            };
        }.start();
    }
    /**
     * synchronized 关键字实现
     * @param thread
     */
    public synchronized void get(Thread thread)  {
        long start = System.currentTimeMillis();
        while(System.currentTimeMillis() - start <= 100) {
            try {
                TimeUnit.MILLISECONDS.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(thread.getName()+"正在进行读操作");
        }
        System.out.println(thread.getName()+"读操作完毕");
    }

    public void lockget(Thread thread){
        rwl.readLock().lock();
        try {
            long start = System.currentTimeMillis();
            while(System.currentTimeMillis() - start <= 100) {
                TimeUnit.MILLISECONDS.sleep(5);
                System.out.println(thread.getName()+"正在进行读操作");
            }
            System.out.println(thread.getName()+"读操作完毕");
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            rwl.readLock().unlock();
        }
    }
}
