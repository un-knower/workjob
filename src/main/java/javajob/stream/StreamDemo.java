package javajob.stream;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by kingcall 2017年-08月-03日,17时-15分
 *
 */
public class StreamDemo {
    //支持顺序和并行聚合操作的一系列元素
    //集合和流动，同时具有一些表面上的相似之处，具有不同的目标。 集合主要关注其元素的有效管理和访问。 相比之下，流不提供直接访问或操纵其元素的
    //手段，而是关心声明性地描述其源和将在该源上进行聚合的计算操作。
    public static void main(String[] args) {
        stream_Paralize();

    }
    public static void op_Base() {


    }

    /**
     * 流的顺序执行与并行执行
     *      一般情况下流都是按照顺序执行的
     *
     */
    public static void stream_Paralize(){
        List<Integer> nums=new java.util.ArrayList<>(10);
        nums.add(0);nums.add(1);nums.add(2);nums.add(3);nums.add(4);nums.add(5);nums.add(6);nums.add(7);nums.add(10);nums.add(10);
        nums.parallelStream().collect(Collectors.toList()).forEach(x->System.out.println());
        nums.stream().collect(Collectors.toList()).forEach(x->System.out.println());


    }
}
