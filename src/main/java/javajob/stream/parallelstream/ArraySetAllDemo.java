package javajob.stream.parallelstream;

import java.util.Arrays;

/**
 * Created by kingcall 2017年-08月-09日,09时-00分
 * Descibe
 */
public class ArraySetAllDemo {
    public static void main(String[] args) {
        long start=System.currentTimeMillis ();
        par ( 100000000 ,10);
        long end=System.currentTimeMillis ();
        System.out.println ("运行总时间："+(end-start));
    }
    public static double[] par(int size,int m){
        double [] valuse=new double[size];
        Arrays.parallelSetAll (valuse,i->m);
        return valuse;
    }
}