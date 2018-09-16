package javajob.stream.lambda;

/**
 * Created by kingcall 2017年-08月-01日,09-50
 *  在Java 8里面，所有的Lambda的类型都是一个接口，而Lambda表达式本身，也就是”那段代码“，需要是这个接口的实现
 *  这种只有一个接口函数需要被实现的接口类型，我们叫它"函数式接口",ambda 表达式的使用需要借助于 函数式接口，也就是说只有函数式接口出现地方，我们才可以将其用 lambda 表达式进行简化
 *      为了避免后来的人在这个接口中增加接口函数导致其有多个接口函数需要被实现，变成"非函数接口”，我们可以在这个上面加上一个声明@FunctionalInterface,
 *      Java中的lambda无法单独出现，它需要一个函数式接口来盛放，lambda表达式方法体其实就是函数接口的实现,往往在需要的地方，已经定义了要适应怎么样的函数接口，然后用lambda 去填充
 *      lambda 表达式隐含了 return 关键字，所以在单个的表达式中，我们无需显式的写 return 关键字，但是当表达式是一个语句集合的时候则需要显式添加 return 关键字，并用花括号 {} 将多个表达式包围起来
 *      为了使现有函数更好的支持Lambda表达式，Java 8引入了函数式接口的概念。函数式接口就是只有一个方法的普通接口
 *  lambda 表达式的语法
 *          一个括号内用逗号分隔的形式参数，参数是函数式接口里面方法的参数
            一个箭头符号：->
            方法体：可以是表达式和代码块，方法体函数式接口里面方法的实现，如果是代码块，则必须用{}来包裹起来，且需要一个return 返回值，但有个例外，
                    若函数式接口里面方法返回值是void，则无需{}
 */
public class Interface_lambda {
    public static void main(String[] args) {
        //每个 lambda 表达式都能隐式地赋值给函数式接口,这主要是因为Lambda的类型都是一个接口,而Lambda 表达式 实现了函数接中的发方法
        Work work=(s)-> System.out.println(s);
        work.dowork("kingcall");
        // 由于Lambda可以直接赋值给一个变量，我们就可以直接把Lambda作为参数传给函数
        enact("kingcall",s -> System.out.println(s));
        thread_op(()->System.out.println("线程开始了"));
        System.out.println(add(2,3,(x,y)->x+y));
    }
    public static void thread_op(Runnable runnable){
        new Thread(runnable).start();
    }

    public static void enact(String s,Work work){
        work.dowork(s);
    }

    public static int add(int x,int y,add_interface add){
       return add.dowork(x,y);
    }

    /**
     * 方法的引用
     *  其实是lambda表达式的一个简化写法，所引用的方法其实是lambda表达式的方法体实现，语法也很简单，左边是容器（可以是类名，实例名），中间是"::"，右边是相应的方法名
     *      如果是静态方法，则是ClassName::methodName。如 Object ::equals
            如果是实例方法，则是Instance::methodName。如Object obj=new Object();obj::equals;
            构造函数.则是ClassName::new
     */
    public static void op_method() {

    }
    //声明了一个函数接口
    @FunctionalInterface
    public interface  Work{
        public  void dowork(String s) ;
    }
    @FunctionalInterface
    public interface  add_interface{
        public  int dowork(int x, int y) ;
    }

}

