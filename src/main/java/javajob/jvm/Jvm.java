package javajob.jvm;

/**
 * Copyright (C)
 *
 * @program: workjob
 * @description: jvm参数
 * @author: 刘文强  kingcall
 * @create: 2018-07-31 17:30
 **/
public class Jvm {
    public static void main(String[] args) {
        System.out.println( Runtime.getRuntime().maxMemory());
        System.out.println( Runtime.getRuntime().freeMemory());
        System.out.println(Runtime.getRuntime().totalMemory());

    }
}
