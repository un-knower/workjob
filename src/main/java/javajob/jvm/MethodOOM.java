package javajob.jvm;

import java.util.ArrayList;
import java.util.List;

/**
 * Copyright (C)
 *
 * @program: workjob
 * @description: 方法区（持久代）内存溢出示例
 * @author: 刘文强  kingcall
 * @create: 2018-08-05 12:30
 **/
public class MethodOOM {
    public static void main(String[] args) {
        List<String> list=new ArrayList<>();
        int i=0;
        while (true){
            list.add(String.valueOf(i).intern());
            System.out.println(i);
            i+=1;
        }
    }

}
