package javajob.serializable;

import java.io.Serializable;

/**
 * Copyright (C)
 *
 * @program: workjob
 * @description:
 * @author: 刘文强  kingcall
 * @create: 2018-08-01 09:44
 **/
public class SchoolBoy extends Student implements Serializable {
    String name;
    String age;
    String sex;

    public SchoolBoy(String banji, String school, String name, String age, String sex) {
        super(banji, school);
        this.name = name;
        this.age = age;
        this.sex = sex;
    }

    public SchoolBoy(String name, String age, String sex) {
        this.name = name;
        this.age = age;
        this.sex = sex;
    }

    public SchoolBoy() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    @Override
    public String toString() {
        return "SchoolBoy{" +
                "name='" + name + '\'' +
                ", age='" + age + '\'' +
                ", sex='" + sex + '\'' +
                ", banji='" + banji + '\'' +
                ", school='" + school + '\'' +
                '}';
    }
}
