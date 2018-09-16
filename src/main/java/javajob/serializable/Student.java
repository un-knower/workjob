package javajob.serializable;

/**
 * Copyright (C)
 *
 * @program: workjob
 * @description:
 * @author: 刘文强  kingcall
 * @create: 2018-08-01 09:42
 **/
public class Student{
    String banji;
    String school;
    public Student() {
    }

    public Student(String banji, String school) {
        this.banji = banji;
        this.school = school;
    }

    public String getBanji() {
        return banji;
    }

    public void setBanji(String banji) {
        this.banji = banji;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }
}
