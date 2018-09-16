package javajob.cmd;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;

public class Cmd {

    public static void main(String[] args) {
        test4();
    }

    public static void test() {
        String command = "ipconfig -all";
        String s = "IPv4";
        String line = null;
        StringBuilder sb = new StringBuilder();
        Runtime runtime = Runtime.getRuntime();
        try {
            Process process = runtime.exec(command);
            // 下面这里由于是从命令行里读取出来的  所以可能发生乱码
            BufferedReader bufferedReader = new BufferedReader
                    (new InputStreamReader(process.getInputStream(), "GBK"));


            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line + "\n");
                if (line.contains(s)) {
                    System.out.println(line);
                }
            }
        } catch (IOException e) {
            // TODO 自动生成的 catch 块
            e.printStackTrace();
        }
        System.out.println(command);
        System.out.println("============================================================");
        System.out.println(sb);
    }

    public static void tests() {
        String line = null;
        String command = "CHCP";
        StringBuilder sb = new StringBuilder();
        Runtime runtime = Runtime.getRuntime();
        try {
            Process process = runtime.exec(command);
            BufferedReader bufferedReader = new BufferedReader
                    (new InputStreamReader(process.getInputStream(), "GBK"));


            while ((line = bufferedReader.readLine()) != null) {
                System.out.println(line);
            }
        } catch (IOException e) {
            // TODO 自动生成的 catch 块
            e.printStackTrace();
        }
    }

    public static void test2() {
        String startday = "2018-04-06";
        String endday = "2018-04-09";
        String userids = "23501,477991,1150561,319602,366602,1028832,91183,433814,584684,38076,578106,957396,2099026,2099966,97297,155107,668347,903347,910397,36198,257568,111400,483171,643442,806062,45603,705483,2098563," +
                "695144,547565,2098095,356276,408446,829096,616657,794787,863877,2838, 1037308,2098568,282489,486939,56420,460150,1321190,1332000,778811,983262,375083,2097633,1217794,2099514, 98725,494935,2098105," +
                "235496,243016,249126,505746,389657,2099518,222809,1311800,733701,669782,2098582,283913,324803,913713,2099053,312114,541004,760964,822535, 1111585,1067406,1317446,157018,2100938,547120,2098120,219061," + "" +
                "341731,617621,1312281,366172,36233,195093,241623,446543,1061303,114254,243974,312124,1316044,1323094,48378497,130520079";
        String basesql = "bash /opt/presto/presto --server 192.168.7.23:1031 --catalog hive --schema ods --execute ";
        String qusersql = "\"select user_id,count_if(is_block=1) as block1,count_if(is_block=0) as block0 ,count(distinct room_id) as room from ods_goim_history where day>=" + "\'" + startday + "\'" + " and day<= " + "\'" + endday +
                "\'" + " and user_id in (" + userids + ") group by user_id;\"";

        System.out.println(basesql + qusersql);
    }

    public static void test3() {
        String s = "\"130520079\",\"1502\",\"8860\",\"1\"";
        String[] ss = s.split(",");
        HashMap<String, String> map = new HashMap<>();
        map.put(ss[0].replace("\"", ""), ss[1].replace("\"", ""));
        map.put(ss[2].replace("\"", ""), ss[3].replace("\"", ""));
        System.out.println(map);
        System.out.println(s);

    }

    public static void test4() {
        String startday = "2018-04-08";
        String endday = "2018-04-10";
        String userids = "1,2,3";
        String s = "按时发生胜多负少的wewqe".replaceAll("[^\\u4e00-\\u9fa5]", "");
        System.out.println(s);
        String basesql = "bash /opt/presto/presto --server 192.168.7.23:1031 --catalog hive --schema ods --execute";
        String qusersql = "\"select user_id,count_if(is_block=1) as block1,count_if(is_block=0) as block0 count(distinct regexp_replace( msg,'[^\\u4e00-\\u9fa5]','')) as cnt,count(distinct room_id) as room from ods_goim_history where day>=" + "\'" + startday + "\'" + " and day<= " + "\'" + endday +
                "\'" + " and user_id in (" + userids + ") group by user_id;\"";
        String resultsql = basesql + qusersql;

        System.out.println("要执行的sql 语句\n" + resultsql);
    }
}
