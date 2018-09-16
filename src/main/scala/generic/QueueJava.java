package generic;

import java.util.ArrayList;
import java.util.List;

/**
 * Copyright (C)
 * 用Java的范型所定义的类或接口并不是协变的
 * 对于Java5之前就存在的数组来说，数组的元素类型A如果是数组元素类型B的子类，那么A的数组类型也是B的数组类型的子类，也就是 说Java中的数组是协变的。
 **/
public class QueueJava {
    public static void main(String[] args) {
        String[] a1={"abc"};
        Object[] a2=new Object[2];
        a2=a1;
        System.out.println(a2.getClass());
        System.out.println(a2[0]);
        List<Object> list=new ArrayList<>();
        List<String> list2=new ArrayList<>();
        // 发现下面的操作就不行，也就是list 不行，但是数组型
        //list=list2;
    }
}
