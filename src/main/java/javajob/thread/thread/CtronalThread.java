package javajob.thread.thread;

public class CtronalThread {
    static CtronalThread t=new CtronalThread();
    class T1 extends Thread{
        @Override
        public void run() {
            //T3线程中要处理的东西
            System.out.println("T1线程执行");
        }
    }

    class T2 extends Thread{
        @Override
        public void run() {
            //T3线程中要处理的东西
            System.out.println("T2线程执行");
            t.new T1().start();
        }
    }

    class T3 extends Thread{
        @Override
        public void run() {
            //T3线程中要处理的东西
            System.out.println("T3线程执行");
            t.new T2().start();
        }

    }

    public static void main(String[] args) {
        t.new T3().start();
    }

}
