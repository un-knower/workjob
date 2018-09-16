package javajob.stream.functiondemo;

/**
 * Created by Administrator on 2017/7/31.
 */
public class FormulaDemo implements Formula{
    public static void main(String[] args) {
        //接口中的方法默认是什么修饰的
        FormulaDemo fd=new FormulaDemo ();
        System.out.println (fd.calculate ( 10 ));
        System.out.println (fd.sqrt ( 10 ));
    }

    @Override
    public double calculate(int a) {
        return a*a;
    }
}

//发现我们可以利用default声明实例方法
interface Formula{
    double calculate(int a);
    default double sqrt(int a){
        return Math.sqrt ( a );
    }
}
