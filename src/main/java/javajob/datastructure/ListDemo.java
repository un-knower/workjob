package javajob.datastructure;

import java.util.*;

/**
 * 1.   记住vector 是动态数组，在读取的时候速度比 LinkedList块
 * 2.   LinkedList 再插入一个新节点。双向链表查找index位置的节点时，有一个加速动作：若index < 双向链表长度的1/2，则从前向后查找; 否则，从后向前查找
 */
public class ListDemo {
    private static final int COUNT = 100000;
    private static LinkedList linkedList = new LinkedList();
    private static ArrayList arrayList = new ArrayList();
    private static Vector vector = new Vector();
    private static Stack stack = new Stack();

    public static void main(String[] args) {
        // 换行符
        System.out.println();
        // 插入
        insertByPosition(stack) ;
        insertByPosition(vector) ;
        insertByPosition(linkedList) ;
        insertByPosition(arrayList) ;

        // 换行符
        System.out.println();
        // 随机读取
        readByPosition(stack);
        readByPosition(vector);
        readByPosition(linkedList);
        readByPosition(arrayList);

        // 换行符
        System.out.println();
        // 删除
        deleteByPosition(stack);
        deleteByPosition(vector);
        deleteByPosition(linkedList);
        deleteByPosition(arrayList);
    }

    /**
     * 获取list的名称
     * @param list
     * @return
     */
    private static String getListName(List list) {
        if (list instanceof LinkedList) {
            return "LinkedList";
        } else if (list instanceof ArrayList) {
            return "ArrayList";
        } else if (list instanceof Stack) {
            return "Stack";
        } else if (list instanceof Vector) {
            return "Vector";
        } else {
            return "List";
        }
    }

    /**
     * 向list的指定位置插入COUNT个元素，并统计时间
     * @param list
     */
    private static void insertByPosition(List list) {
        long startTime = System.currentTimeMillis();

        // 向list的位置0插入COUNT个数
        for (int i=0; i<COUNT; i++){
            list.add(0, i);
        }

        long endTime = System.currentTimeMillis();
        long interval = endTime - startTime;
        System.out.println(getListName(list) + " : 插入 "+COUNT+" elements into the 1st position use time：" + interval+" ms");
    }

    /**
     * 从list的指定位置删除COUNT个元素，并统计时间
     * @param list
     */
    private static void deleteByPosition(List list) {
        long startTime = System.currentTimeMillis();

        // 删除list第一个位置元素
        for (int i=0; i<COUNT; i++){
            list.remove(0);
        }

        long endTime = System.currentTimeMillis();
        long interval = endTime - startTime;
        System.out.println(getListName(list) + " : 删除 "+COUNT+" elements from the 1st position use time：" + interval+" ms");
    }

    /**
     * 根据position，不断从list中读取元素，并统计时间
     * @param list
     */
    private static void readByPosition(List list) {
        long startTime = System.currentTimeMillis();

        // 读取list元素
        for (int i=0; i<COUNT; i++){
            list.get(i);
        }

        long endTime = System.currentTimeMillis();
        long interval = endTime - startTime;
        System.out.println(getListName(list) + " : 读取 "+COUNT+" elements by position use time：" + interval+" ms");
    }
}
