package javajob.thread.concurent;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.*;

/**
 * Copyright (C)
 *
 * @program: workjob
 * @description: 阻塞 队列的使用方法
 * @author: 刘文强  kingcall
 * @create: 2018-08-02 13:06
 **/

/**
 *  1.  队列相比集合提供了它特殊的方法(add->offser,elemet->peek,remove->poll)，阻塞式队列相比普通队列提供了它特殊的方法（put,get 并且这连个方法都支持tineout 参数,所有就会有异常抛出）
 *  2. 类别：
 *      1.  ArrayBlockingQueue
 *      2.  DelayQueue
 *      3.  LinkedBlockingQueue
 *      4.  PriorityBlockingQueue
 */
public class BlockingQueueDemo {

    public static void main(String[] args) throws InterruptedException {
        base_op_SynchronousQueue();
    }


    /**
     *  1.  ArrayBlockingQueue 是一个有界的阻塞队列，其内部实现是将对象放到一个数组里。有界也就意味着，它不能够存储无限多数量的元素。
     *      它有一个同一时间能够存储元素数量的上限。你可以在对其初始化的时候设定这个上限，但之后就无法对这个上限进行修改了
     * @throws InterruptedException
     */
    public static void base_op_ArrayBlockingQueue() throws InterruptedException {
        BlockingQueue queue = new ArrayBlockingQueue(1024);
        Producer producer = new Producer(queue);
        Consumer consumer = new Consumer(queue);
        new Thread(producer).start();
        new Thread(consumer).start();
        TimeUnit.SECONDS.sleep(5);
    }
    /**
     *  1.  DelayQueue 对元素进行持有直到一个特定的延迟到期。注入其中的元素必须实现 java.util.concurrent.Delayed 接口
     *  2.  一个无界的BlockingQueue，用于放置实现了Delayed接口的对象，其中的对象只能在其到期时才能从队列中取走。这种队列是有序的，即队头对象的延迟到期时间最长。注意：不能将null元素放置到这种队列中。
     */
    public static void base_op_DelayQueue() throws InterruptedException {
        DelayQueue<Student> queue = new DelayQueue();
        queue.add(new Student("刘文强",10L,TimeUnit.SECONDS));
        queue.add(new Student("刘备",20L,TimeUnit.SECONDS));
        queue.add(new Student("刘邦",30L,TimeUnit.SECONDS));
        while(!queue.isEmpty()) {
            try {
                Student student = queue.take();
                System.out.println(student);

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     *  1.  LinkedBlockingQueue 内部以一个链式结构(链接节点)对其元素进行存储。如果需要的话，这一链式结构可以选择一个上限。如果没有定义上限，将使用 Integer.MAX_VALUE 作为上限。
     *  2. 在普通意义上的使用时没有和ArrayBlockingQueue没有什么区别，就看你元素个数的多少
     * @throws InterruptedException
     */
    public static void base_op_LinkedBlockingQueue() throws InterruptedException {
        BlockingQueue queue = new LinkedBlockingQueue();
        Producer producer = new Producer(queue);
        Consumer consumer = new Consumer(queue);
        new Thread(producer).start();
        new Thread(consumer).start();
        TimeUnit.SECONDS.sleep(5);
    }

    /**
     * 优先级队列
     *      PriorityBlockingQueue 是一个无界的并发队列。它使用了和类 java.util.PriorityQueue 一样的排序规则
     *      所有插入到 PriorityBlockingQueue 的元素必须实现 java.lang.Comparable 接口。因此该队列中元素的排序（优先级）就取决于你自己的 Comparable 实现。
     * 下面的示例
     *      只展示了优先级，没有展示阻塞的效果
     */
    public static void base_op_PriorityBlockingQueue() {
        BlockingQueue<Teacher> queue=new PriorityBlockingQueue();
        queue.offer(new Teacher("kingcall",24));
        queue.offer(new Teacher("刘备",25));
        queue.offer(new Teacher("陆仟",27));
        for (int i = 0; i < 4; i++) {
            System.out.println(queue.poll());
        }

    }

    public static void base_op_SynchronousQueue() throws InterruptedException {
        SynchronousQueue<String> queue = new SynchronousQueue();
        System.out.println(queue.size());
        //为什么会阻塞呢
        queue.put("s");
        queue.take();
        System.out.println("执行完毕");

    }

}
class Teacher implements Comparable<Teacher>{
    String name;
    int age;

    public Teacher(String mame, int age) {
        this.name = mame;
        this.age = age;
    }

    @Override
    public int compareTo(Teacher o) {
       if (age>o.age){
           return 1;
       }else if (age<o.age){
           return -1;
       }else {
           return 0;
       }
    }
}
class Student implements Delayed {
    public static SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SS");
    public String name;
    public Long kaoTime;
    /**
     * 计算的时间单位
     */
    public TimeUnit delayTimeUnit;
    /**
     * 执行的时间戳（单位毫秒）
     */
    public Long executeTime;

    public Student(String name, Long kaoTime, TimeUnit delayTimeUnit) {
        this.name = name;
        this.kaoTime = kaoTime;
        this.delayTimeUnit = delayTimeUnit;
        this.executeTime = System.currentTimeMillis() + delayTimeUnit.toMillis(kaoTime);
    }
    @Override
    public String toString() {
        return "Student:"+name+"\t 考试时间到,请交卷,你的考试时间截止:"+format.format(new Date(executeTime))+",当前时间:"+format.format(new Date(System.currentTimeMillis()));
    }
    /**
     * getDelay方法的作用即是计算当前时间到执行时间之间还有多长时间
     *      在这里要注意，坑比较多 unit是一个纳秒级别的单位，所以返回的数字的意思是距离当前的纳秒数字，所以你要保证调用对象和传入对象是一致的(但是具体的原因还没有理解)
     * @param unit
     * @return
     */
    @Override
    public long getDelay(TimeUnit unit) {
        return unit.convert(executeTime-System.currentTimeMillis(),unit);
    }

    /**
     * compareTo方法的作用即是判断队列中元素的顺序谁前谁后。当前元素比队列元素后执行时，返回一个正数，比它先执行时返回一个负数，否则返回0.
     * @param o
     * @return
     */
    @Override
    public int compareTo(Delayed o) {
        if(this.getDelay(delayTimeUnit) > o.getDelay(delayTimeUnit)) {
            return 1;
        }else if(this.getDelay(delayTimeUnit) < o.getDelay(delayTimeUnit)) {
            return -1;
        }
        return 0;
    }
}

class Consumer implements Runnable{
    protected BlockingQueue queue = null;
    public Consumer(BlockingQueue queue) {
        this.queue = queue;
    }
    @Override
    public void run() {
        try {
            System.out.println(queue.take());
            System.out.println(queue.take());
            System.out.println(queue.take());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

class Producer implements Runnable{
    protected BlockingQueue<String> queue = null;

    public Producer(BlockingQueue queue) {
        this.queue = queue;
    }
    @Override
    public void run() {
        try {
            queue.put("1");
            Thread.sleep(10000);
            queue.put("2");
            Thread.sleep(10000);
            queue.put("3");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
