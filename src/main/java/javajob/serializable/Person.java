package javajob.serializable;

import java.io.Serializable;

/**
 * Copyright (C)
 *
 * @program: workjob
 * @description: 序列化演示
 * @author: 刘文强  kingcall
 * @create: 2018-08-01 09:03
 **/
public class Person  implements Serializable {


    private static final long serialVersionUID =1L ;
    private int age;
    private String name;
    private String sex;

    public Person() {
    }

    public Person (int age, String name, String sex) {
        this.age = age;
        this.name = name;
        this.sex = sex;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    @Override
    public String toString() {
        return "Person{" +
                "age=" + age +
                ", name='" + name + '\'' +
                ", sex='" + sex + '\'' +
                '}';
    }
}
