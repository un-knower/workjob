package javajob.thread.thread;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

/**
 * Copyright (C)
 *
 * @program: workjob
 * @description:
 * @author: 刘文强  kingcall
 * @create: 2018-08-01 19:40
 **/

/**
 *   1.  一旦调用get，那么该线程就会阻塞
 *   2. FutureTask 实现了 RunnableFuture，而runable 继承了 Runnable 和 Future
 */
public class FutureTaskDemo {
   static FutureTask<Integer> futureTask=  new FutureTask<>(()->{
        System.out.println("线程开始执行");
        int sum = 0;
        for (int i = 0; i <= 100000; i++) {
            sum += i;
        }
        return sum;
    });


    public static void main(String[] args) throws ExecutionException, InterruptedException {
        cancell();
    }

    /**
     * 当线程不取消执行时，调用get方法就会报错
     * @throws ExecutionException
     * @throws InterruptedException
     */
    public static void cancell() throws ExecutionException, InterruptedException {
        new Thread(futureTask).start();
        if (!futureTask.isDone()&&!futureTask.isCancelled()){
            futureTask.cancel(true);
        }
        System.out.println(futureTask.isCancelled());
        System.out.println(futureTask.get());
    }

    /**
     * 当将FutureTask提交给Executor后，Executor执行FutureTask时会执行其run方法,而run方法则会调用callable的call 方法，也就是启动线程
     */
    public static void run() throws ExecutionException, InterruptedException {
        new Thread(futureTask).start();
        System.out.println(futureTask.isCancelled());
        System.out.println(futureTask.isDone());
        System.out.println(futureTask.get());
        System.out.println(futureTask.isDone());
    }


}
