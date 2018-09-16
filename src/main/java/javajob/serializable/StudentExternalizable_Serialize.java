package javajob.serializable;

import java.io.*;

/**
 * Copyright (C)
 *
 * @program: workjob
 * @description:
 * @author: 刘文强  kingcall
 * @create: 2018-08-01 11:30
 **/
public class StudentExternalizable_Serialize {
    public static void main(String[] args) throws Exception {
        SerializePerson();
        StudentExternalizable p = DeserializePerson();
        System.out.println(p);
    }

    /**
     * MethodName: SerializePerson
     * Description: 序列化Person对象
     * @author xudp
     * @throws FileNotFoundException
     * @throws IOException
     */
    private static void SerializePerson() throws Exception {
        StudentExternalizable person = new StudentExternalizable("kingcall","kingcall_pass",24);
        // ObjectOutputStream 对象输出流，将Person对象存储到E盘的Person.txt文件中，完成对Person对象的序列化操作
        ObjectOutputStream oo = new ObjectOutputStream(new FileOutputStream(
                new File("E:/StudentExternalizable.txt")));
        person.writeExternal(oo);
        System.out.println("StudentExternalizable对象序列化成功！");
    }

    /**
     * MethodName: DeserializePerson
     * Description: 反序列Perons对象
     * @author xudp
     * @return
     * @throws Exception
     * @throws IOException
     */
    private static StudentExternalizable DeserializePerson() throws Exception, IOException {
        ObjectInputStream ois = new ObjectInputStream(new FileInputStream(
                new File("E:/StudentExternalizable.txt")));
        StudentExternalizable person = new StudentExternalizable();
        System.out.println(person);
        person.readExternal(ois);
        System.out.println("StudentExternalizable对象反序列成功！");
        return person;
    }

    /**
     * 发现这样对象，不能再通过普通的反序列化进行反序列化了，起到了保密作用
     * @return
     * @throws Exception
     * @throws IOException
     */
    private static  StudentExternalizable StudentExternalizable_force() throws Exception, IOException {
        ObjectInputStream ois = new ObjectInputStream(new FileInputStream(
                new File("E:/StudentExternalizable.txt")));
        StudentExternalizable person = (StudentExternalizable) ois.readObject();
        System.out.println("StudentExternalizable对象强制反序列成功！");
        return person;
    }

}
