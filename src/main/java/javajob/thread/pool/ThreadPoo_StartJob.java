package javajob.thread.pool;

import java.util.concurrent.*;
import java.util.function.Function;

/**
 * Copyright (C)
 *
 * @program: workjob
 * @description:
 * @author: 刘文强  kingcall
 * @create: 2018-08-01 18:28
 **/

/**
 * 演示线程池的基本使用
 *      2. 线程池的 api
 */


public class ThreadPoo_StartJob {
    static Function function=(s)->{System.out.println("lamabda 表达式创建线程");return "";};

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        exexute();
    }


    /**
     *   1.   sumbit方法不会抛出异常（发生了也不会抛出）。除非你调用Future.get()
     *   2.   线程出错，你如果不调用get 也不会知道
     *   3.   方法的返回值 Future 可以像 FutureTask 一样 供你获取线程状态，操作线程
     *
     */
    public static void submit() throws ExecutionException, InterruptedException {
        ExecutorService service= Executors.newSingleThreadExecutor();
        Future future=service.submit(new Runnable() {
            @Override
            public void run() {
                System.out.println("程序开始启动");
            }
        });
        /**
         * 如果有null返回则证明线程执行完毕，没有错误
         */
       System.out.println( future.get());
        try {
            //close pool
            service.shutdown();
            service.awaitTermination(5, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            if (!service.isTerminated()) {
                service.shutdownNow();
            }
        }
    }


    /**
     * execute方法不关心返回值。excute方法会抛出异常(程序如果发生异常，会被自动抛出)
     */
    public static void exexute() {
        Thread thread= new Thread(()->{
            System.out.println("lamabda 表达式创建线程");
            int i=7/0;
        });
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(thread);
        try {
            //close pool
            executor.shutdown();
            executor.awaitTermination(5, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            if (!executor.isTerminated()) {
                executor.shutdownNow();
            }
        }

    }


}
