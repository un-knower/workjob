package javajob.thread.thread;

import java.util.concurrent.*;

/**
 * FutureTask类实现了RunnableFuture接口，而RunnnableFuture接口继承了Runnable和Future接口，所以说FutureTask是一个提供异步计算的结果的任务。
 */
public class CallableDemo  {
    static class SumTask implements Callable<Long> {
        @Override
        public Long call() throws Exception {

            long sum = 0;
            for (int i = 0; i < 9000; i++) {
                sum += i;
            }
            return sum;
        }
    }
    public static void main(String[] args) throws InterruptedException, ExecutionException, TimeoutException {
        start_job2();
    }

    /**
     * 线程池的方式启动
     * @throws ExecutionException
     * @throws InterruptedException
     */
    public static void start_job2() throws ExecutionException, InterruptedException {
        ExecutorService service=Executors.newSingleThreadExecutor();
        FutureTask<Long> futureTask=new FutureTask<Long>(new SumTask());
        service.execute(futureTask);
        System.out.println(futureTask.get());

    }

    /**
     * 利用普通线程的启动方法启动
     * @throws ExecutionException
     * @throws InterruptedException
     * @throws TimeoutException
     */
    public static void start_job() throws ExecutionException, InterruptedException, TimeoutException {
     FutureTask<Integer> futureTask=  new FutureTask<>(()->{
         System.out.println("线程开始执行");
            int sum = 0;
            for (int i = 0; i <= 100000; i++) {
                sum += i;
            }
            return sum;
        });
       new Thread(futureTask).start();
       System.out.println( futureTask.get());
    }


}
