package javajob.thread.thread;

/**
 * join 给你演示实例
 *  1.  它可以使得线程之间的并行执行变为串行执行
 */
public class JoinDemo {

    public static void main(String [] args) throws InterruptedException {
        jointest2();
    }
    public static void jointest2() throws InterruptedException {
        ThreadJoinTest t1 = new ThreadJoinTest("小明");
        ThreadJoinTest t2 = new ThreadJoinTest("小东");
        /**join方法可以在start方法前调用时，并不能起到同步的作用
         */
        t1.join();
        t1.start();
        //Thread.yield();
        t2.start();

    }
    public static void jointest1() throws InterruptedException {
        ThreadJoinTest t1 = new ThreadJoinTest("小明");
        ThreadJoinTest t2 = new ThreadJoinTest("小东");
        t1.start();
        /**
         join的意思是使得放弃当前线程的执行，并返回对应的线程，例如下面代码的意思就是：
         程序在main线程中调用t1线程的join方法，则main线程放弃cpu控制权，并返回t1线程继续执行直到线程t1执行完毕，所以结果是t1线程执行完后，才到主线程执行，相当于在main线程中同步t1线程，t1执行完了，main线程才有执行的机会
         join方法中如果传入参数，则表示这样的意思：如果A线程中掉用B线程的join(10)，则表示A线程会等待B线程执行10毫秒，10毫秒过后，A、B线程并行执行
         */
        t1.join();
        t2.start();
    }
}
class ThreadJoinTest extends Thread{
    public ThreadJoinTest(String name){
        super(name);
    }
    @Override
    public void run(){
        for(int i=0;i<1000;i++){
            System.out.println(this.getName() + ":" + i);
        }
    }
}
