package javajob.stream.lambda;

/**
 * Copyright (C)
 *
 * @program: workjob
 * @description: 接口展示：接口的静态方法和默认方法
 * @author: 刘文强  kingcall
 * @create: 2018-08-01 08:41
 **/

/**
 * 在jdk8之前，interface之中可以定义变量和方法，变量必须是public、static、final的，方法必须是public、abstract的
 * 静态方法，只能通过接口名调用，不可以通过实现类的类名或者实现类的对象调用。default方法，只能通过接口实现类的对象来调用
 */
public class Interfacedemo implements DefaultInterface,StaticInterface{
    public static void main(String[] args) {
        Interfacedemo demo=new Interfacedemo();
        demo.showDefault();
        StaticInterface.showStatic();
    }
}

//函数接口
@FunctionalInterface
interface Funcational{
    void show(String info);
}
interface DefaultInterface{
    default void showDefault(){
        System.out.println("默认方法输出");
    }
}
//接口的静态方法在继承之后还是无法使用的
interface StaticInterface{
    static void showStatic(){
        System.out.println("静态方法输出");
    }
}

