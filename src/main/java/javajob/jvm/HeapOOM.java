package javajob.jvm;

import java.util.ArrayList;
import java.util.List;

/**
 * Copyright (C)
 *
 * @program: workjob
 * @description: 堆内存溢出
 * @author: 刘文强  kingcall
 * @create: 2018-08-05 11:37
 **/
public class HeapOOM {
    static class OOMObject{

    }
    public static void main(String[] args) {
        List<OOMObject> list=new ArrayList<>();
        while (true){
            list.add(new OOMObject());
        }
    }
}
