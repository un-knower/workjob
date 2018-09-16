package javajob.jvm;

/**
 * Copyright (C)
 *
 * @program: workjob
 * @description: 引用计数法
 * @author: 刘文强  kingcall
 * @create: 2018-08-05 14:42
 **/
public class ReferenceCount {
    public Object instance = null;
    private static final int _1MB=1024*1024;
    public static void main(String[] args) {
        GC();

    }
    public static void GC() {
        ReferenceCount a=new ReferenceCount();
        ReferenceCount b=new ReferenceCount();
        a.instance=b;
        b.instance=a;
        a=null;b=null;
        System.gc();
    }
}
