package javajob.thread.thread;


public class CtronalThread2 {
    public static void main(String[] args) {
        Thread T1 =new  Thread(){
            @Override
            public void run() {
                //T3线程中要处理的东西
                System.out.println("T1线程执行");
            }
        };
        Thread T2 =new  Thread(){
            @Override
            public void run() {
                try {
                    T1.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                //T3线程中要处理的东西
                System.out.println("T2线程执行");
            }
        };
        Thread T3 =new  Thread(){
            @Override
            public void run() {
                //T3线程中要处理的东西
                try {
                    T2.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("T3线程执行");
            }
        };

        T2.start();
        T1.start();
        T3.start();

    }
}
