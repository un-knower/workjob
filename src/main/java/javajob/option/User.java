package javajob.option;

import java.util.Optional;

/**
 * Copyright (C)
 *
 * @program: workjob
 * @description: 测试对象
 * @author: 刘文强  kingcall
 * @create: 2018-07-29 15:51
 **/

/**
 * 手机和邮箱不是一个人的必须有的，所以我们利用 Optional 类定义
 */
public class User {
    private long id;
    private String name;
    private int age;
    private Optional<Long> phone;
    private Optional<String> email;

    public User(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public Optional<Long> getPhone() {
        return phone;
    }

    public void setPhone(Optional<Long> phone) {
        this.phone = phone;
    }

    public Optional<String> getEmail() {
        return email;
    }

    public void setEmail(Optional<String> email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", age=" + age +
                ", phone=" + phone +
                ", email=" + email +
                '}';
    }
}
