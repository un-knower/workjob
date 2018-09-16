package sparkdemo.firstwork;

import com.alibaba.fastjson.JSON;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.io.*;
import java.util.Random;
import java.util.UUID;

/**
 * @author kingcall
 * @create 2017-12-11 12:15
 * Describe
 **/

public class SendMessage {

    public static Random rand = new Random();
    /*一些特定的host*/
    public static String[] hostname = {"api.longzhu.com", "api.plu.cn", "betapi.longzhu.com", "configapi.longzhu.com", "event-api.longzhu.com", "giftapi.plu.cn",
            "id-api.longzhu.com", "login.plu.cn", "mb.tga.plu.cn"};

    public static void main(String[] args) {
        try {
            sendMessage_from_file();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public static void sendMessage_from_file() throws IOException {
        KafkaProducer<String, String> producer = sparkdemo.firstwork.KafkaUtil.getProducer("localhost", 9092);
        System.out.println("信息发送开始");
        long cnt = 1;
        System.out.println("=========================即将发送消息==========================");
        File file = new File("src/main/resources/kafka/app_log_screenview.txt");
        BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
        while (true) {
            //消息躰,record的构造方法开可以再加一个参数，也就是第二个，是分区。
            String value = bufferedReader.readLine();
            if (value == null || value.isEmpty()) {
                break;
            }
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            ProducerRecord<String, String> message = new ProducerRecord<>("app_log_screenview2", String.valueOf(cnt), value);
            producer.send(message);
            System.out.println(cnt);
            cnt++;
        }
    }


    public static void sendMessage() throws InterruptedException {
        KafkaProducer<String, String> producer = sparkdemo.firstwork.KafkaUtil.getProducer("localhost", 9092);
        long cnt = 4709;
        System.out.println("=========================即将发送消息==========================");
        while (true) {
            //消息躰,record的构造方法开可以再加一个参数，也就是第二个，是分区。
            String value = createMessage();
            ProducerRecord<String, String> message = new ProducerRecord<>("king", String.valueOf(cnt), value);
            producer.send(message);
            System.out.println(cnt);
            cnt++;
            Thread.sleep(2000);
            if (cnt >= 10000) {
                break;
            }
        }
    }

    public static String createMessage() {
        String client_ip = rand.nextInt(9) + "" + rand.nextInt(9) + "" + rand.nextInt(9) + "." + rand.nextInt(9) + "" + rand.nextInt(9) + "" + rand.nextInt(9) + "." + rand.nextInt(9) + "" + rand.nextInt(9) + ""
                + rand.nextInt(9) + "." + rand.nextInt(9) + "" + rand.nextInt(9) + "" + rand.nextInt(9);
        String is_blocked = "1";
        String args = "kingcall";
        String status = "200";
        String uid = UUID.randomUUID().toString().replaceAll("-", "");
        String host = hostname[rand.nextInt(hostname.length)];
        sparkdemo.firstwork.OriginalMessageBean bean = new sparkdemo.firstwork.OriginalMessageBean(client_ip, is_blocked, args, status, uid, host, String.valueOf(System.currentTimeMillis()), rand.nextInt(100000));
        String s = JSON.toJSONString(bean);
        System.out.println(s);
        return s;

    }
}
