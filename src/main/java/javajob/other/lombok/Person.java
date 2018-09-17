package javajob.other.lombok;


import lombok.*;

/**
 * 生成的快捷方法都支持 ctrl+f12
 */

/**
 * @Getter和@Setter
 * 该注解使用在类或者属性上，该注解可以使用在类上也可以使用在属性上。生成的getter遵循布尔属性的约定。例如：boolean类型的sex,getter方法为isSex而不是getSex
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
 *
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
