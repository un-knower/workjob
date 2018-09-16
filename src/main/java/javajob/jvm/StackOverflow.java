package javajob.jvm;

/**
 * Copyright (C)
 *
 * @program: workjob
 * @description:
 * @author: 刘文强  kingcall
 * @create: 2018-08-05 12:21
 **/
public class StackOverflow {
    private int stackLength=1;

    public void stackLength() {
       stackLength++;
       stackLength();
    }
    public static void main(String[] args) {
        StackOverflow oom=new StackOverflow();

        try {
            oom.stackLength();
        } catch (Exception e) {
            System.out.println("stack length:"+oom.stackLength);
            throw e;
        }
    }
}
