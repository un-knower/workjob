package javajob.stream.parallelstream;

import java.util.Arrays;
import java.util.stream.IntStream;

/**
 * Created by kingcall 2017年-08月-09日,09时-23分
 * Descibe
 */
public class sequentialSumOfSquares {
    public static void main(String[] args) {
        int[] rs=new int[100000];
        Arrays.parallelSetAll (rs,i->i);
        int m=multi (Arrays.stream ( rs )  );
        System.out.println (m);

    }
    public static int multi(IntStream range){
        return range.parallel ().map ( x->x*x ).sum ();//普通流，调用并行方法
    }
}
