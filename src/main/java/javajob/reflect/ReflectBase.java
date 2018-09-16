package javajob.reflect;

import com.mysql.jdbc.MiniAdmin;
import javajob.jvm.classloder.FileClassLoader;

import javax.sound.midi.Soundbank;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

/**
 * Copyright (C)
 *
 * @program: workjob
 * @description: 反射基础
 * @author: 刘文强  kingcall
 * @create: 2018-08-09 14:32
 **/

/**
 * 1.   在运行期间，一个类，只有一个Class对象产生(和类加载那一节联系起来)
 * 2.   三种方式常用第三种，第一种对象都有了还要反射干什么。第二种需要导入类的包，依赖太强，不导包就抛编译错误。一般都第三种，一个字符串可以传入也可写在配置文件中等多种方法。
 * 3.   获取的Object对象都可以都可以强转成对应的对象
 * 4.   基本使用方法
 *      1.   obj = con.newInstance("kingcall");构造方法
 *      2.   m.invoke(obj, "刘德华");          成员方法
 *      3.   f.set(obj, "刘德华");             成员变量
 *  5.  和 new 对象的区别
 *      1.  newInstance( )是一个方法，而new是一个关键字；
 *      2.  其次 Class下的newInstance()的使用有局限，因为它生成对象只能调用无参的构造函数，而使用 new关键字生成对象没有这个限制。
 *      3.  newInstance(): 弱类型,低效率,只能调用无参构造。new: 强类型,相对高效,能调用任何public构造。
 */
public class ReflectBase {
    public static void main(String[] args) throws Exception {
        use1();
    }

    /**
     * main 方法的被调用意味着静态方法也可以被调用
     */
    public static void baseGetMainMethodUse() {
        try {
            //1、获取Student对象的字节码
            Class clazz = Class.forName("javajob.reflect.Student");

            //2、获取main方法 第一个参数：方法名称，第二个参数：方法形参的类型，
            Method methodMain = clazz.getMethod("main", String[].class);
            //3、调用main方法
            //第一个参数，对象类型，因为方法是static静态的，所以为null可以，第二个参数是String数组，这里要注意在jdk1.4时是数组，jdk1.5之后是可变参数
            methodMain.invoke(null, (Object)new String[]{"a","b","c"});
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 获取所有的方法的时候,会将子类的方法也获取
     * @throws ClassNotFoundException
     * @throws NoSuchMethodException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws InstantiationException
     */
    public static void baseGetCommonMethodUse() throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        //1.获取Class对象
        Class stuClass = Class.forName("javajob.reflect.Student");
        //2.获取所有公有方法
        System.out.println("***************获取所有的”公有“方法*******************");
        stuClass.getMethods();
        Method[] methodArray = stuClass.getMethods();
        for(Method m : methodArray){
            System.out.println(m);
        }
        System.out.println("***************获取所有的方法，包括私有的*******************");
        methodArray = stuClass.getDeclaredMethods();
        for(Method m : methodArray){
            System.out.println(m);
        }

        System.out.println("***************获取公有的show1()方法*******************");
        Method m = stuClass.getMethod("show1", String.class);
        System.out.println(m);
        //实例化一个Student对象
        Object obj = stuClass.getConstructor().newInstance();
        m.invoke(obj, "刘德华");

        System.out.println("***************获取私有的show4()方法******************");
        m = stuClass.getDeclaredMethod("show4", int.class);
        System.out.println(m);
        //解除私有限定
        m.setAccessible(true);
        //需要两个参数，一个是要调用的对象（获取有反射），一个是实参
        Object result = m.invoke(obj, 20);
        System.out.println("返回值：" + result);
    }

    public static void baseGetProperty() throws ClassNotFoundException, NoSuchFieldException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        Class stuClass = Class.forName("javajob.reflect.Student");
        //2.获取字段,数组里面的每一个元素就是一个字段，但是存储的信息可不仅仅是字段本身
        System.out.println("************获取所有公有的字段********************");
        Field[] fieldArray = stuClass.getFields();
        for(Field f : fieldArray){
            System.out.println(f);
        }
        System.out.println("************获取所有的字段(包括私有、受保护、默认的)********************");
        fieldArray = stuClass.getDeclaredFields();
        for(Field f : fieldArray){
            System.out.println(f);
        }
        System.out.println("*************获取公有字段**并调用***********************************");
        Field f = stuClass.getField("name");
        System.out.println(f);
        //获取一个对象
        Object obj = stuClass.getConstructor().newInstance();
        //为字段设置值，需要对象
        f.set(obj, "刘德华");
        //验证，将父类强制转化成了子类
        Student stu = (Student)obj;
        System.out.println("验证姓名：" + stu.name);
        System.out.println(obj);

        System.out.println("**************获取私有字段****并调用********************************");
        f = stuClass.getDeclaredField("phoneNum");
        System.out.println(f);
        f.setAccessible(true);
        //暴力反射，解除私有限定
        f.set(obj, "18888889999");
        System.out.println("验证电话：" + stu);

    }


    public static void baseGetConstructMethodUse() throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        Class clazz = Class.forName("javajob.reflect.Student");
        //2.获取所有公有构造方法
        System.out.println("**********************所有公有构造方法*********************************");
        Constructor[] conArray = clazz.getConstructors();
        for(Constructor c : conArray){
            System.out.println(c);
        }
        System.out.println("************所有的构造方法(包括：私有、受保护、默认、公有)***************");
        conArray = clazz.getDeclaredConstructors();
        for(Constructor c : conArray){
            System.out.println(c);
        }

        System.out.println("*****************获取公有、无参的构造方法*******************************");
        Constructor con = clazz.getConstructor(null);
        //1>、因为是无参的构造方法所以类型是一个null,不写也可以：这里需要的是一个参数的类型，切记是类型
        //2>、返回的是描述这个无参构造函数的类对象。

        System.out.println("con = " + con);
        //调用构造方法
        Object obj = con.newInstance();
        System.out.println("******************获取私有构造方法，并调用*******************************");
        con = clazz.getDeclaredConstructor(String.class);
        System.out.println(con);
        //调用构造方法 暴力访问(忽略掉访问修饰符)
        con.setAccessible(true);
        obj = con.newInstance("kingcall");
        // 感觉是父类调用了子类的toString 方法
        System.out.println(obj.toString());
        Student student=(Student)obj;
        System.out.println(student);
    }

    public static void base_op1() throws NoSuchMethodException {
        //这一new 产生一个Student对象，一个Class对象。
        Student stu1 = new Student();
        Class stuClass = stu1.getClass();
        System.out.println(stuClass.getName());
        //第二种方式获取Class对象
        Class stuClass2 = Student.class;
        //判断第一种方式获取的Class对象和第二种方式获取的是否是同一个
        System.out.println(stuClass == stuClass2);
        System.out.println(stuClass2.getName());
        //第三种方式获取Class对象
        try {
            ////注意此字符串必须是真实路径，就是带包名的类路径，包名.类名
            Class stuClass3 = Class.forName("javajob.reflect.Student");
            System.out.println(stuClass3 == stuClass2);
            System.out.println(stuClass3.getName());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        String rootDir="D:/";
        FileClassLoader loader = new FileClassLoader(rootDir);
        try {
            Class<?> object1=loader.loadClass("Student");
            System.out.println("==========================================================");
            System.out.println(object1.getClass().getName());
            System.out.println(object1.getClassLoader());
            Constructor con=object1.getConstructor(null);
            Student student=(Student)con.newInstance();
            student.setAge(24);
            student.setName("刘文强");
            System.out.println(student);
            System.out.println(con);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    /**
     * 利用反射，越过类型检查，原理是：泛型用在编译期，编译过后泛型擦除（消失掉）。所以是可以通过反射越过泛型检查的
     * @throws Exception
     */
    public static void use1() throws Exception{
        ArrayList<String> strList = new ArrayList<>();
        strList.add("aaa");
        strList.add("bbb");
        //	strList.add(100);
        //获取ArrayList的Class对象，反向的调用add()方法，添加数据
        Class listClass = strList.getClass();
        //获取add()方法
        Method m = listClass.getMethod("add", Object.class);
        //调用add()方法
        m.invoke(strList, 100);
        //遍历集合
        for(Object obj : strList){
            System.out.println(obj);
        }
    }

}
