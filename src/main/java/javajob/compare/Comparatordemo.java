package javajob.compare;

import java.util.Arrays;
import java.util.Comparator;
import java.util.function.Consumer;

/**
 * Copyright (C)
 *
 * @program: workjob
 * @description:
 * @author: 刘文强  kingcall
 * @create: 2018-07-29 13:32
 **/
public class Comparatordemo {
    static Person p1=new Person("kingcall",20);
    static Person p2=new Person("kingcall",24);
    static Person[] people={p1,p2};
    static Person[] people2={p1,p2};
    public static void main(String[] args) {
        Arrays.sort(people, new Comparator<Person>() {
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
        });

        Arrays.stream(people).forEach(x-> {
            System.out.println(x);
        });

        Arrays.sort(people2,(s1,s2)->{
            if (s1.getName().equals(s2.getName())){
                if (s1.getAge()>=s1.getAge()){
                    return 1;
                }else {
                    return 0;
                }
            }else {
                return s1.getName().compareToIgnoreCase(s1.getName());
            }
        });

        Arrays.stream(people2).forEach(x->System.out.println(x));
    }
}
