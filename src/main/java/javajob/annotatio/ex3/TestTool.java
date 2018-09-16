package javajob.annotatio.ex3;

/**
 * Copyright (C)
 *
 * @program: workjob
 * @description: 测试注解
 * @author: 刘文强  kingcall
 * @create: 2018-07-10 13:15
 * 获取注解，并获取注解里面的值，然后决定运行那个方法
 **/
public class TestTool {

    public static void main(String[] args) {
        FiledClass filedClass = new FiledClass();
        boolean flag = filedClass.getClass().isAnnotationPresent(Fileds.class);
        if (flag) {
            Fileds fileds = filedClass.getClass().getAnnotation(Fileds.class);
            if (fileds.id() == 1) {
                filedClass.test1();
            } else {
                filedClass.test2();
            }
        }
    }
}
