package sparkdemo;

import java.util.HashMap;

public class test {
    public static void main(String[] args) {
        HashMap<String, Integer> map = new HashMap();
        HashMap<String, Integer> map2 = new HashMap();
        map2.put("a", 10);
        map.put("a", 1);
        map.put("b", 2);
        map.put("c", 3);
        map2.putAll(map);

        for (String i : map2.keySet()) {
            System.out.println(map.get(i));
        }

        // merge 不存在放进去  存在 合并（抽象意义上的）
        Integer s = map2.merge("a", 10, (x, y) -> x + y);
        System.out.println(s);
        Integer s2 = map2.merge("d", 10, (x, y) -> x + y);
        System.out.println(s2);
        System.out.println(map2.get("a"));
    }
}
