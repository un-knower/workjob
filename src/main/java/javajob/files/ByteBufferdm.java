package javajob.files;

import org.xerial.snappy.Snappy;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * Copyright (C)
 *
 * @program: workjob
 * @description: 演示ByteBuffer操作文件
 * @author: 刘文强  kingcall
 * @create: 2018-04-16 15:05
 **/
public class ByteBufferdm {
    public static void main(String[] args) throws IOException {
        FileInputStream fileInputStream = new FileInputStream("D:" + File.separator + "logs/000000_0");
        FileOutputStream fileOutputStream = new FileOutputStream("D:" + File.separator + "logs/3.txt");
        FileChannel inChannel = fileInputStream.getChannel();
        FileChannel outChannel = fileOutputStream.getChannel();

        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(1024);
        ByteBuffer byteBuffer2 = ByteBuffer.allocateDirect(1024);

        long start = System.currentTimeMillis();
        while (true) {
            int eof = inChannel.read(byteBuffer);
            if (eof == -1) {
                break;
            }
            Snappy.uncompress(byteBuffer, byteBuffer2);
            byteBuffer.flip();
            byteBuffer2.flip();
            outChannel.write(byteBuffer2);
            byteBuffer.clear();
        }
        System.out.println("spending : " + (System.currentTimeMillis() - start));
        inChannel.close();
        outChannel.close();
    }
}
