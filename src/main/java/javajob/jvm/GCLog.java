package javajob.jvm;

/**
 * Copyright (C)
 *
 * @program: workjob
 * @description:
 * @author: 刘文强  kingcall
 * @create: 2018-08-05 21:47
 **/
public class GCLog {
    private static final int _1MB=1024*1024*1024*5;
    public static void main(String[] args) {
        allocation();
    }
    public static void allocation() {
        byte[] a1,a2,a3,a4;
        a1=new byte[2*_1MB];
        a2=new byte[2*_1MB];
        a3=new byte[2*_1MB];
        a4=new byte[2*_1MB];
    }
}
