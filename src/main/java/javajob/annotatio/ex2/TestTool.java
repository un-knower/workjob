package javajob.annotatio.ex2;

import java.lang.reflect.Method;

/**
 * Copyright (C)
 *
 * @program: workjob
 * @description: 测试被注解类
 * @author: 刘文强  kingcall
 * @create: 2018-07-10 12:58
 **/
public class TestTool {
    public static void main(String[] args) {
        NoBug testobj = new NoBug();
        Class clazz = testobj.getClass();
        Method[] method = clazz.getDeclaredMethods();
        //用来记录测试产生的 log 信息
        StringBuilder log = new StringBuilder();
        // 记录异常的次数
        int errornum = 0;
        for (Method m : method) {
            // 只有被 @Jiancha 标注过的方法才进行测试
            if (m.isAnnotationPresent(Jiancha.class)) {
                try {
                    m.setAccessible(true);
                    m.invoke(testobj, null);

                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    //e.printStackTrace();
                    errornum++;
                    //通过方法对象获取方法的名称
                    log.append(m.getName());
                    log.append(" ");
                    log.append("has error:");
                    log.append("\n\r  caused by ");
                    //记录测试过程中，发生的异常的名称
                    log.append(e.getCause().getClass().getSimpleName());
                    log.append("\n\r");
                    //记录测试过程中，发生的异常的具体信息
                    log.append(e.getCause().getMessage());
                    log.append("\n\r");
                }
            }
        }
        log.append(clazz.getSimpleName());
        log.append(" has  ");
        log.append(errornum);
        log.append(" error.");
        // 生成测试报告
        System.out.println(log.toString());
    }
}
