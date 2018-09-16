package javajob.serializable;

import java.io.*;

/**
 * Copyright (C)
 *
 * @program: workjob
 * @description:
 * @author: 刘文强  kingcall
 * @create: 2018-08-01 10:30
 **/
public class Password_Serialize {
    public static void main(String[] args) throws Exception {
        SerializePerson();
        Password p = DeserializePerson();
        System.out.println(p);
    }

    private static void SerializePerson() throws FileNotFoundException,
            IOException {
        Password person = new Password("kingcall_pass");
        ObjectOutputStream oo = new ObjectOutputStream(new FileOutputStream(
                new File("E:/password.txt")));
        //就会去找对象的writeObject方法，否则就会调用调用ObjectOutputStream的默认方法
        oo.writeObject(person);
        System.out.println("Password对象序列化成功！");
        oo.flush();
        oo.close();
    }

    private static Password DeserializePerson() throws Exception, IOException {
        ObjectInputStream ois = new ObjectInputStream(new FileInputStream(
                new File("E:/password.txt")));
        Password person = (Password) ois.readObject();
        System.out.println("Password对象反序列化成功！！");
        return person;
    }
}
