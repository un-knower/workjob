package javajob.serializable;

import java.io.*;

/**
 * Copyright (C)
 *
 * @program: workjob
 * @description:
 * @author: 刘文强  kingcall
 * @create: 2018-08-01 09:48
 **/

/**
 *  1.  父类的get,set 等方法也会被继承
 *  2.  在父类没有实现 Serializable 接口时，虚拟机是不会序列化父对象的，而一个 Java 对象的构造必须先有父对象，才有子对象，反序列化也不例外。所以反序列化时，为了构造父对象，
 *      只能调用父类的无参构造函数作为默认的父对象。因此当我们取 父对象的变量值时，它的值是调用父类无参构造函数后的值。如果你考虑到这种序列化的情况，在父类无参构造函数中对变量进行初始化，否则的话，父类变量值都 是默认声明的值。
 *
 *      如果想让子类保持所有的信息都被序列化，就让父类 implements Serializable
 *  3.  Transient 关键字的作用是控制变量的序列化，在变量声明前加上该关键字，可以阻止该变量被序列化到文件中，在被反序列化后，transient 变量的值被设为初始值，如 int 型的是 0，对象型的是 null
 *      使用序列化时父类不会被序列化的特点可以完成Transient 的功能 （如果很多类的对象都有相同的字段被Transient修饰，那么可以让他们继承同一个父类 ）
 *
 */
public class SchoolBoy_Serialize {
    public static void main(String[] args) throws Exception {
        SerializePerson();
        SchoolBoy p = DeserializePerson();
        System.out.println(p);
    }

    /**
     * MethodName: SerializePerson
     * Description: 序列化Person对象
     * @author xudp
     * @throws FileNotFoundException
     * @throws IOException
     */
    private static void SerializePerson() throws FileNotFoundException,
            IOException {
        SchoolBoy person = new SchoolBoy();
        person.setAge("20");
        person.setName("kingcall");
        person.setSex("男");
        person.setSchool("宁县二中");
        person.setBanji("高三三");
        // ObjectOutputStream 对象输出流，将Person对象存储到E盘的Person.txt文件中，完成对Person对象的序列化操作
        ObjectOutputStream oo = new ObjectOutputStream(new FileOutputStream(
                new File("E:/schoolboy.txt")));
        oo.writeObject(person);
        System.out.println("SchoolBoy对象序列化成功！");
        oo.flush();
        oo.close();
        System.out.println(person);
    }

    /**
     * MethodName: DeserializePerson
     * Description: 反序列Perons对象
     * @author xudp
     * @return
     * @throws Exception
     * @throws IOException
     */
    private static SchoolBoy DeserializePerson() throws Exception, IOException {
        ObjectInputStream ois = new ObjectInputStream(new FileInputStream(
                new File("E:/schoolboy.txt")));
        SchoolBoy person = (SchoolBoy) ois.readObject();
        System.out.println("SchoolBoy对象反序列成！");
        return person;
    }
}
