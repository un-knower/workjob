package javajob.thread.pool;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Copyright (C)
 *
 * @program: workjob
 * @description: 探索线程池
 * @author: 刘文强  kingcall
 * @create: 2018-08-02 10:08
 **/
public class ThreadPool {
    public static void main(String[] args) {
        System.out.println( Runtime.getRuntime().availableProcessors());
        newScheduledThreadPool();

    }

    /**
     * 创建一个定长的线程池，而且支持定时的以及周期性的任务执行
     */
    public static void newScheduledThreadPool() {
        ScheduledExecutorService scheduledThreadPool = Executors.newScheduledThreadPool(5);
        for (int i = 0; i < 6; i++) {
            scheduledThreadPool.schedule(new Runnable() {
                @Override
                public void run() {
                    System.out.println("delay 3 seconds");
                }
            }, 3, TimeUnit.SECONDS);
        }
         //延迟三秒每个一秒执行一次
        for (int i = 0; i < 6; i++) {
            scheduledThreadPool.scheduleAtFixedRate(new Runnable() {
                @Override
                public void run() {
                    System.out.println("delay 3 seconds and with 1 rate");
                }
            }, 3,1, TimeUnit.SECONDS);
        }
        // 首次延迟一秒 以后延迟三秒
        for (int i = 0; i < 6; i++) {
            scheduledThreadPool.scheduleWithFixedDelay(new Runnable() {
                @Override
                public void run() {
                    System.out.println("delay 3 seconds");
                }
            }, 1,3, TimeUnit.SECONDS);
        }
    }

    /**
     * 只创建唯一的工作者线程来执行任务，它只会用唯一的工作线程来执行任务，保证所有任务按照指定顺序(FIFO, LIFO, 优先级)执行。如果这个线程异常结束，会有另一个取代它，
     * 保证顺序执行。单工作线程最大的特点是可保证顺序地执行各个任务，并且在任意给定的时间不会有多个线程是活动的
     */
    public static void newSingleThreadExecutor() {
        ExecutorService singleThreadExecutor = Executors.newSingleThreadExecutor();
        for (int i = 0; i < 10; i++) {
            final int index = i;
            singleThreadExecutor.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        System.out.println(index);
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    /**
     * 创建一个指定工作线程数量的线程池。每当提交一个任务就创建一个工作线程，如果工作线程数量达到线程池初始的最大数，则将提交的任务存入到池队列中。
     * FixedThreadPool是一个典型且优秀的线程池，它具有线程池提高程序效率和节省创建线程时所耗的开销的优点。但是，在线程池空闲时，即线程池中没有可运行任务时，它不会释放工作线程，还会占用一定的系统资源
     */
    public static void newFixedThreadPool() {
        ExecutorService fixedThreadPool = Executors.newFixedThreadPool(3);
        for (int i = 0; i < 10; i++) {
            final int index = i;
            fixedThreadPool.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        System.out.println(index);
                        System.out.println( "=========="+Runtime.getRuntime().availableProcessors());
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    /**
     *  工作线程的创建数量几乎没有限制(其实也有限制的,数目为Interger. MAX_VALUE), 这样可灵活的往线程池中添加线程
     *  如果长时间没有往线程池中提交任务，即如果工作线程空闲了指定的时间(默认为1分钟)，则该工作线程将自动终止。终止后，如果你又提交了新的任务，则线程池重新创建一个工作线程(方法里面有一些参数决定的)
     *  在使用CachedThreadPool时，一定要注意控制任务的数量，否则，由于大量线程同时运行，很有会造成系统瘫痪(因为它相当于没有限制)
     */
    public static void newCachedThreadPool() {
        ExecutorService cachedThreadPool = Executors.newCachedThreadPool();
        for (int i = 0; i < 10; i++) {
            final int index = i;
            try {
                Thread.sleep(index * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            cachedThreadPool.execute(new Runnable() {
                @Override
                public void run() {
                    System.out.println(index);
                }
            });
        }
    }
}
