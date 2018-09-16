package javajob.thread.concurent;
import java.util.LinkedList;
import java.util.Queue;

/**
 * 1 . 队列是一种特殊的线性表，它只允许在表的前端（front）进行删除操作，而在表的后端（rear）进行插入操作。进行插入操作的端称为队尾，进行删除操作的端称为队头。队列中没有元素时，称为空队列
 * 2. Queue使用时要尽量避免Collection的add()和remove()方法，而是要使用offer()来加入元素，使用poll()来获取并移出元素。它们的优点是通过返回值可以判断成功与否，
 *      add()和remove()方法在失败的时候会抛出异常。 如果要使用前端而不移出该元素，使用element()或者peek()方法
 */
public class QueueDemo {
   public static Queue<String> queue = new LinkedList<String>();
    public static void main(String[] args) {
        base_op();
    }
    /**
     * 对队列的基本操作
     */
    public static void base_op() {
        // 添加元素，成功之后返回true
        queue.offer("a");queue.offer("b");queue.offer("c");queue.offer("d");queue.offer("e");
        //下面两个都会取出第一个元素但是不会删除
        System.out.println("peek="+queue.peek());
        //在集合是空的时候回抛出异常
        System.out.println("element="+queue.element());
        // 遍历队列元素，有序的从头到尾
        for(String q : queue){
            System.out.println(q);
        }
        // 返回第一个元素，并在队列中删除，发现即使元素被取完了也不会抛出异常
        for (int i = 0; i <10 ; i++) {
            System.out.println("poll="+queue.poll());
        }
        // remove 当没有元素的时候会报错
        for (int i = 0; i <10 ; i++) {
            System.out.println("poll="+queue.remove());
        }
    }
}


