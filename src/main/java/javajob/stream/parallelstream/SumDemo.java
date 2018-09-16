package javajob.stream.parallelstream;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kingcall 2017年-08月-09日,08时-34分
 * Descibe
 */
public class SumDemo {
    public static void main(String[] args) {
        long start=System.currentTimeMillis ();
        List<Integer>arrays=new ArrayList<> (10000000  );
        long sum=0;
        for (int i=0;i<100000000;i++){
            arrays.add ( i );
        }
        for (int J:arrays)
            sum+=J;
        long end=System.currentTimeMillis ();
        System.out.println ("运行时间是："+(end-start));
        System.out.println ("运行结果是："+sum);
    }
}
