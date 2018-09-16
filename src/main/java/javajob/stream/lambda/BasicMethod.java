package javajob.stream.lambda;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kingcall 2017年-08月-08日,09时-32分
 * Descibe
 */
public class BasicMethod {
    public static void main(String[] args) {
        show ();
    }
    //forEach   Consumer
    public static void show(){
        List<Integer>nums=new ArrayList<Integer> ( );
        nums.add ( 1 );nums.add ( 2 );nums.add ( 3 );nums.add ( 5 );
        nums.forEach (s-> System.out.println (s+1) );

    }
}
