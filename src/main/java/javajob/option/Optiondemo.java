package javajob.option;

import java.util.Optional;
import java.util.concurrent.Callable;

/**
 * Copyright (C)
 *
 * @program: workjob
 * @description: option 演示
 * @author: 刘文强  kingcall
 * @create: 2018-07-29 15:25
 **/
public class Optiondemo {
    public static void main(String[] args) {
        op_1();
    }

    public static void op_1() {
        String s1=null;
        String s2="kingcall";
        Optional<String> optStr1 = Optional.empty();
        /*擦书不能为空*/
        Optional<String> optStr2 = Optional.of(s2);
        /*参数可以为空*/
        Optional<String> optStr3 = Optional.ofNullable(s1);
        /*对于可为空的：空则等价于empty()方法的返回值;不空则等价于of的返回 */
        Optional<String> optStr4 = Optional.ofNullable(s2);
        System.out.println(optStr1);
        System.out.println(optStr2);
        System.out.println(optStr3);
        System.out.println(optStr4);

        /*当不存的时候就执行一段操作，并返回值,提供的是一个函数接口 Supplier*/
        System.out.println(optStr1.orElseGet(()->{
            System.out.println("这里没有值");
            return new User("kingcalll",34).toString();
        }));
        System.out.println(optStr1.orElseGet(()->"哈哈 你好啊  哈哈娃"));
        /*当不存的时候就返回你给的，定义是三元操作符*/
        System.out.println(optStr1.orElse("哈哈"));
        /*当不存的时候机会报错*/
        System.out.println(optStr1.get());
    }

    public static void op_Map() {
        User user=new User("kingcall",24);
        String name = Optional.ofNullable(user).map(User::getName).orElse("no name");
        System.out.println(name);
        /**
         * 这样当入参 user 不为空的时候则返回其 name，否则返回 no name。如我我们希望通过上面方式得到 phone 或 email，
         * 利用上面的方式则行不通了，因为 map 之后返回的是 Optional，我们把这种称为 Optional 嵌套，我们必须再 map 一次才能拿到我们想要的结果
         */
        String email = Optional.ofNullable(user).map(User::getEmail).map(Optional::get).orElse("no email");
        System.out.println(email);
        /* flatmap 在这里更好*/
        email = Optional.ofNullable(user).flatMap(User::getEmail).orElse("no email");
        System.out.println(email);


    }

}
