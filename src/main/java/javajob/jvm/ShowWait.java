package javajob.jvm;

import lombok.Synchronized;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.TimeUnit;

/**
 * Copyright (C)
 *
 * @program: workjob
 * @description: jconsole显示等待
 * @author: 刘文强  kingcall
 * @create: 2018-08-06 07:41
 **/
public class ShowWait {
    public static void createBusyThread() {
        Thread thread = new Thread(() -> {
            while (true) {
                ;
            }
        }, "BusyThread");
        thread.start();
    }

    public static void createLockThread(final Object lock) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized(lock) {
                    try {
                        lock.wait();
                        System.out.println("我正在等待被唤醒");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        },"lockThread");
        thread.start();
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        BufferedReader br=new BufferedReader(new InputStreamReader(System.in));
        br.readLine();
        createBusyThread();
        br.readLine();
        Object obj=new Object();
        createLockThread(obj);
        TimeUnit.SECONDS.sleep(5);
        System.out.println("线程即将被唤醒");
        obj.notifyAll();

    }




}
