package javajob.thread.concurent;

import java.util.HashMap;

/**
 * Copyright (C)
 *
 * @program: workjob
 * @description:
 * @author: 刘文强  kingcall
 * @create: 2018-08-02 18:51
 **/
public class HashMapDemo {
    public static void main(String[] args) {
        HashMap<String,String>map=new HashMap<>(10);
        ProducerMap producerMap = new ProducerMap("produce", map);
        ConsumerMap consumerMap = new ConsumerMap("consume", map);
        for (int i=0;i<100;i++){
            Thread thread1=new Thread(producerMap);
            Thread thread2=new Thread(consumerMap);
            thread1.setName("produce"+i);
            thread2.setName("consume"+i);
            thread1.start();thread2.start();
        }
    }
    public static void base_op() {
        HashMap<String,String> map=new HashMap(10);
        map.put("a", "b");
        map.put("b", "b");
        System.out.println(map);
    }
}
class ProducerMap extends Thread{
    private String name;
    private HashMap<String,String> map;

    public ProducerMap(String name, HashMap<String, String> map) {
        this.name = name;
        this.map = map;
    }

    @Override
    public void run() {
        for (int i = 0; i < 100; i++) {
            System.out.println(Thread.currentThread().getName()+": "+map.size());
            map.put(i+"a","kingcall");

        }
    }
}
class ConsumerMap extends Thread{
    private String name;
    private HashMap<String,String> map;

    public ConsumerMap(String name, HashMap<String, String> map) {
        this.name = name;
        this.map = map;
    }

    @Override
    public void run() {
        for (int i = 0; i < 100; i++) {
            System.out.println(Thread.currentThread().getName()+": "+map.size());
            map.remove(i+"a");

        }
    }
}