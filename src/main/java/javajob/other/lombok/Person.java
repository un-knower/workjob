package javajob.other.lombok;


import lombok.*;

/**
 * 生成的快捷方法都支持 ctrl+f12
 */

/**
 * @Getter和@Setter
 * 该注解使用在类或者属性上，该注解可以使用在类上也可以使用在属性上。生成的getter遵循布尔属性的约定。例如：boolean类型的sex,getter方法为isSex而不是getSex
 * @Getter(lazy=true)
 * 如果getter方法计算值需要大量CPU，或者值占用大量内存，第一次调用这个getter，它将一次计算一个值，然后从那时开始缓存它
 * @Data
 * 该注解使用在类上，该注解会提供getter、setter、equals、canEqual、hashCode、toString方法。
 * @NonNull
 * 该注解使用在属性上，该注解用于属的非空检查，当放在setter方法的字段上，将生成一个空检查，如果为空，则抛出NullPointerException。  但是发现会生成一个有参构造
 * @EqualsAndHashCode
 * 该注解使用在类上，该注解在类级别注释会同时生成equals和hashCode。
 * @AllArgsConstructor
 * 该注解使用在类上，该注解提供一个全参数的构造方法，默认不提供无参构造。
 * @NoArgsConstructor
 * 该注解使用在类上，该注解提供一个无参构造
 * @Value
 * 这个注解用在 类 上，会生成含所有参数的构造方法，get 方法，此外还提供了equals、hashCode、toString 方法。
 * 释放资源   用于流等可以不需要关闭使用流对象.
 * @Cleanup InputStream in = new FileInputStream(args[0]);
 * @Cleanup OutputStream out = new FileOutputStream(args[1]);
 * toString ，还可以排除指定的列
 * @ToString(exclude="id")
 *
 * @log
 * 可以生成多种log 对象，Log作用于类，创建一个log属性
 * @NonNull
 * 在方法或构造函数的参数上使用@NonNull，lombok将生成一个空值检查语句。
 * @Builder：使用builder模式创建对象
 * @Accessors(chain = true)
 * 使用链式设置属性，set方法返回的是this对象。
 */
@Getter
@Setter
public class Person {
    private String name;
    private int age;
    private boolean sex;
}

@Data
@EqualsAndHashCode
class Person2{
    private String name;
    private int age;
    private boolean sex;
}
@Data
@AllArgsConstructor
@NoArgsConstructor
class Person3{
    private String name;
    private int age;
    private boolean sex;
}
