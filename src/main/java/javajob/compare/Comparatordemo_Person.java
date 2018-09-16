package javajob.compare;

import java.util.Comparator;

/**
 * Copyright (C)
 *
 * @program: workjob
 * @description:
 * @author: 刘文强  kingcall
 * @create: 2018-07-29 21:42
 **/

/**
 * 在前面的演示中，在每次需要的地方都会创建一个匿名的比较对象，所以这个我们将这个匿名的对象保存下来，多次使用——比较器
 */
public class Comparatordemo_Person implements Comparator<Person> {
    @Override
    public int compare(Person o1, Person o2) {
        if (o1.getName().equals(o2.getName())){
            if (o1.getAge()>=o2.getAge()){
                return 1;
            }else {
                return 0;
            }
        }else {
            return o1.getName().compareToIgnoreCase(o2.getName());
        }
    }
    public static void main(String[] args) {
        Comparatordemo_Person comparatordemo_person=new Comparatordemo_Person();
        Person person1=new Person("kingcall",20);
        Person person2=new Person("kingcall",20);
        System.out.println(comparatordemo_person.compare(person1,person2));
    }
}
