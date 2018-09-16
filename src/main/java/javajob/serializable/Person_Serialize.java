package javajob.serializable;

import java.io.*;
import java.text.MessageFormat;

/**
 * 1. 文件流中的class和classpath中的class，也就是修改过后的class，不兼容了，处于安全机制考虑，程序抛出了错误，并且拒绝载入,因此，只要我们自己指定了serialVersionUID，就可以在序列化后，
 *    去添加一个字段，或者方法，而不会影响到后期的还原，还原后的对象照样可以使用，而且还多了方法或者属性可以用。
 * 2. 序列化保存的是对象的状态，静态变量属于类的状态，因此 序列化并不保存静态变量
 * 3. Transient 关键字的作用是控制变量的序列化，在变量声明前加上该关键字，可以阻止该变量被序列化到文件中，在被反序列化后，transient 变量的值被设为初始值，如 int 型的是 0，对象型的是 null
 * 4. Java 序列化机制为了节省磁盘空间，具有特定的存储规则，当写入文件的为同一对象时，并不会再将对象的内容进行存储，而只是再次存储一份引用。所以存储的永远是第一份数据，但是连个不同的对象就可以存储
 * 5. 实现Externalizable接口的类完全由自身来控制序列化的行为，而仅实现Serializable接口的类可以采用默认的序列化方式
 *
 */
public class Person_Serialize {
    public static void main(String[] args) throws Exception {
        SerializePerson();
        Person p = DeserializePerson();
        System.out.println(MessageFormat.format("name={0},age={1},sex={2}",
                p.getName(), p.getAge(), p.getSex()));
        System.out.println();
    }

    private static void SerializePerson() throws FileNotFoundException,
            IOException {
        Person person = new Person();
        person.setName("gacl");
        person.setAge(25);
        person.setSex("男");
        // ObjectOutputStream 对象输出流，将Person对象存储到E盘的Person.txt文件中，完成对Person对象的序列化操作
        ObjectOutputStream oo = new ObjectOutputStream(new FileOutputStream(
                new File("E:/Person.txt"),true));
        oo.writeObject(person);
        oo.writeObject(new Password("kingcall"));
        System.out.println("Person对象序列化成功！");
        oo.flush();
        oo.close();
    }

    private static Person DeserializePerson() throws Exception, IOException {
        ObjectInputStream ois = new ObjectInputStream(new FileInputStream(
                new File("E:/Person.txt")));
        Person person = (Person) ois.readObject();
        Password person2 = (Password) ois.readObject();
        System.out.println(person);
        System.out.println(person2);
        System.out.println("Person对象反序列化成功！");
        return person;
    }

}
