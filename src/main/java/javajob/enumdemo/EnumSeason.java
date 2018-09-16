package javajob.enumdemo;

/**
 * 为什么不用静态类型的常亮代替枚举类型
 * 1. 类型不安全。若一个方法中要求传入季节这个参数，用常量的话，形参就是int类型，开发者传入任意类型的int类型值就行，但是如果是枚举类型的话，就只能传入枚举类中包含的对象。
 * 　2. 没有命名空间。开发者要在命名的时候以SEASON_开头，这样另外一个开发者再看这段代码的时候，才知道这四个常量分别代表季节。
 * 特点
 * 枚举类的所有实例都必须放在第一行展示，不需使用new 关键字，不需显式调用构造器。自动添加public static final修饰。
 * 举类的构造器只能是私有的。无法在外部调用枚举的构造函数（因为枚举的构造函数都是私有的，只有内部才能调用）
 * 之所以特殊是因为它既是一种类(class)类型却又比类类型多了些特殊的约束，但是这些约束的存在也造就了枚举类型的简洁性、安全性以及便捷性
 * 无法再继承其他类或者枚举（但是可以实现接口），因为它默认继承了java.lang.Enum
 */
public enum EnumSeason {
    //下面就是所有的实例
    SPRING("春天"), SUMMER("夏天"), FALL("秋天"), WINTER("冬天");
    private final String name;

    private EnumSeason(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
