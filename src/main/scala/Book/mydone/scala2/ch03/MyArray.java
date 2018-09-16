package Book.mydone.scala2.ch03;

/**
 * @program: workjob
 * @description: 测试java 数组 可传父类
 * @author: 刘文强  kingcall
 * @create: 2018-03-27 23:10
 **/
public class MyArray {

    public static void main(String[] args) {
        String[] s = new String[10];
        s[0] = "a";
        s[1] = "b";
        s[2] = "c";
        s[3] = "d";
        s[4] = "e";
        s[5] = "f";
        s[6] = "i";
        s[7] = "z";
        // 返回的是找到的下标
        int m = java.util.Arrays.binarySearch(s, "i");
        System.out.println(m);
    }
}
