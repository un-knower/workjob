package javajob.serializable;

import java.io.*;

/**
 * Copyright (C)
 *
 * @program: workjob
 * @description:
 * @author: 刘文强  kingcall
 * @create: 2018-08-01 11:22
 **/

/**
 * 1. Externalizable进行序列化和反序列化会比较麻烦，因为需要重写序列化和反序列化的方法，序列化的细节需要手动完成。当读取对象时，会调用被序列化类的无参构造器去创建一个新的对象，
 *    然后再将被保存对象的字段的值分别填充到新对象中。因此，实现Externalizable接口的类必须要提供一个无参的构造器，且它的访问权限为public。其实所有的都一样
 */
public class StudentExternalizable implements Externalizable,Serializable{
    private static final long serialVersionUID = 5382223347946263671L;
    private String userName;
    private String password;
    private int age;

    public StudentExternalizable() {
    }

    public StudentExternalizable(String userName, String password, int age) {

        this.userName = userName;
        this.password = password;
        this.age = age;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeObject(userName);
        //对密码进行加密，干扰
        out.writeObject(password+":ps");
        out.writeInt(age);
        out.flush();
        out.close();

    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        userName = (String) in.readObject();
        password = in.readObject().toString().split(":")[0];
        age = in.readInt();
    }

    @Override
    public String toString() {
        return "StudentExternalizable{" +
                "userName='" + userName + '\'' +
                ", password='" + password + '\'' +
                ", age=" + age +
                '}';
    }
}
