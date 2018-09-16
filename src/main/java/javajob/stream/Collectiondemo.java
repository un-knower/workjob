package javajob.stream;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Copyright (C)
 *
 * @program: workjob
 * @description: 流处理集合
 * @author: 刘文强  kingcall
 * @create: 2018-07-29 14:08
 *
 *      流式处理让集合操作变得简洁了许多，通常我们需要多行代码才能完成的操作，借助于流式处理可以在一行中实现
 **/
public class Collectiondemo {
    public  static  List<Student> students = new ArrayList<Student>() {
        {
            add(new Student(20160001, "孔明", 20, 1, "土木工程", "武汉大学"));
            add(new Student(20160002, "伯约", 21, 2, "信息安全", "武汉大学"));
            add(new Student(20160003, "玄德", 22, 3, "经济管理", "武汉大学"));
            add(new Student(20160004, "云长", 21, 2, "信息安全", "武汉大学"));
            add(new Student(20161001, "翼德", 21, 2, "机械与自动化", "华中科技大学"));
            add(new Student(20161002, "元直", 23, 4, "土木工程", "华中科技大学"));
            add(new Student(20161003, "奉孝", 23, 4, "计算机科学", "华中科技大学"));
            add(new Student(20162001, "仲谋", 22, 3, "土木工程", "浙江大学"));
            add(new Student(20162002, "鲁肃", 23, 4, "计算机科学", "浙江大学"));
            add(new Student(20163001, "丁奉", 24, 5, "土木工程", "南京大学"));
        }
    };
    public static void main(String[] args) {
        filter_op();
    }

    public static void filter_op(){
        List<Integer> nums=new ArrayList<>(10);
        nums.add(0);nums.add(1);nums.add(2);nums.add(3);nums.add(4);nums.add(5);nums.add(6);nums.add(7);nums.add(10);nums.add(10);
        List<Integer> evens = nums.stream().filter(num -> num % 2 == 0).collect(Collectors.toList());
        evens.forEach(x-> System.out.print(x+"\t"));System.out.println();
        /*去重操作*/
        evens = nums.stream().filter(num -> num % 2 == 0).distinct().collect(Collectors.toList());
        evens.forEach(x-> System.out.print(x+"\t"));System.out.println();
        /*limit 操作*/
        evens = nums.stream().filter(num -> num % 2 == 0).distinct().limit(2).collect(Collectors.toList());
        evens.forEach(x-> System.out.print(x+"\t"));System.out.println();
        evens.forEach(System.out::print);System.out.println();

        /* allMatch 操作 和 filter 的操作类似，参数是一样的 */
        String[] strings=new String[]{"abc","sdf","fdds","dsfsdff"};
        System.out.println (Arrays.stream ( strings ).allMatch (s->s.length ()>2 ));
        Predicate<String> predicate=new Predicate<String> () {
            @Override
            public boolean test(String s) {
                return s.length ()>2;
            }
        };
        System.out.println (Arrays.stream (strings ).allMatch ( predicate));

        Function<String,String> sFunction=new Function<String, String> () {
            @Override
            public String apply(String s) {
                return s+"中国人";
            }
        };


    }
    public static void filter_op2(){
        List<Student> whuStudents = students.stream()
                .filter(student -> "武汉大学".equals(student.getSchool()))
                .collect(Collectors.toList());
        whuStudents.forEach(x-> System.out.println(x));

        /*排序操作*/
        List<Student> sortedCivilStudents = students.stream()
                .filter(student -> "土木工程".equals(student.getMajor())).sorted((s1, s2) -> s1.getAge() - s2.getAge()).collect(Collectors.toList());
        sortedCivilStudents.forEach(x-> System.out.println(x));
    }

    /**
     * 在SQL中，借助SELECT关键字后面添加需要的字段名称，可以仅输出我们需要的字段数据，而流式处理的映射操作也是实现这一目的，在java8的流式处理中，主要包含两类映射操作：map和flatMap
     *  只输出我们想要的字段
     */
    public static void map_op() {
        List<String> names = students.stream()
                .filter(student -> "计算机科学".equals(student.getMajor()))
                .map(Student::getName).collect(Collectors.toList());
        names.forEach(x-> System.out.println(x));
        int totalAge = students.stream()
                .filter(student -> "计算机科学".equals(student.getMajor()))
                .mapToInt(Student::getAge).sum();

    }



}
