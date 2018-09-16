package javajob.thread.concurent;

import java.util.Random;
import java.util.concurrent.Exchanger;
import java.util.concurrent.TimeUnit;

/**
 * 表示一种两个线程可以进行互相交换对象的汇合点（感觉可以使用栅栏机制实现）
 */
public class ExchangerDemo {
    public static void main(String[] args) {
        Exchanger exchanger = new Exchanger();
        ExchangerRunnable exchangerRunnable1 =
                new ExchangerRunnable(exchanger, "A");
        ExchangerRunnable exchangerRunnable2 =
                new ExchangerRunnable(exchanger, "B");
        new Thread(exchangerRunnable1).start();
        new Thread(exchangerRunnable2).start();
    }
}


class ExchangerRunnable implements Runnable {
    Exchanger exchanger = null;
    Object object = null;

    public ExchangerRunnable(Exchanger exchanger, Object object) {
        this.exchanger = exchanger;
        this.object = object;
    }

    @Override
    public void run() {
        System.out.println(Thread.currentThread().getName()+" pre_has "+object);
        try {
            int time=new Random().nextInt(30);
            System.out.println(Thread.currentThread().getName()+" 将休息 "+ time+" 秒");
            TimeUnit.SECONDS.sleep(time);
            // 还是比较强大的，可以相互等待（就是所说的汇合）交换的是改变后的值
           // object="kingcall:"+time;
            this.object = this.exchanger.exchange(this.object);
            System.out.println(Thread.currentThread().getName()+" ofter_has "+object);

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

