package javajob.annotatio.ex2;

/**
 * Copyright (C)
 *
 * @program: workjob
 * @description: 被注解类
 * @author: 刘文强  kingcall
 * @create: 2018-07-10 12:56
 **/
public class NoBug {
    @Jiancha
    public void suanShu() {
        System.out.println("1234567890");
    }

    @Jiancha
    public void jiafa() {
        System.out.println("1+1=" + 1 + 1);
    }

    @Jiancha
    public void jiefa() {
        System.out.println("1-1=" + (1 - 1));
    }

    @Jiancha
    public void chengfa() {
        System.out.println("3 x 5=" + 3 * 5);
    }

    @Jiancha
    public void chufa() {
        System.out.println("6 / 0=" + 6 / 0);
    }

    public void ziwojieshao() {
        System.out.println("我写的程序没有 bug!");
    }

}
