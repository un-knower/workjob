package util;

import redis.clients.jedis.Jedis;

import java.util.List;
import java.util.Set;

public class RedisUtli {
    public static void main(String[] args) {
        setList("kc", "刘文强", "刘备", "刘邦", "刘婵", "刘谦");
        List<String> list = getList("kc", 0, 20);
        for (int i = 0; i < list.size(); i++) {
            System.out.println(list.get(i));
        }
    }

    public static Jedis getJedis() {
        //连接本地的 Redis 服务
        Jedis jedis = new Jedis("localhost");
        System.out.println("Connection to server sucessfully");
        //查看服务是否运行
        System.out.println("Server is running: " + jedis.ping());
        return jedis;
    }

    public static void setString(String key, String value) {
        getJedis().set(key, value);
    }

    public static String getString(String key) {
        String value = getJedis().get(key);
        return value;
    }

    public static void setList(String key, String... value) {
        getJedis().lpush(key, value);
    }

    public static List<String> getList(String key, int start, int end) {
        return getJedis().lrange(key, start, end);
    }

    public static int getKeysCount() {
        return getJedis().keys("*").size();
    }

    public static Set<String> getKeys() {
        return getJedis().keys("*");
    }

    public static int getKeysCount(String pattern) {
        return getJedis().keys(pattern).size();
    }

    public static Set<String> getKeys(String pattern) {
        return getJedis().keys(pattern);
    }


}
