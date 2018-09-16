package javajob.other;


import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.io.IOException;

/**
 * Copyright (C)
 *
 * @program: workjob
 * @description: 加密与解密
 * @author: 刘文强  kingcall
 * @create: 2018-04-20 13:21
 **/
public class DecodeAndEncode {
    public static final BASE64Encoder encoder = new BASE64Encoder();
    public static final BASE64Decoder decoder = new BASE64Decoder();

    public static void main(String[] args) throws IOException {
        Base64();
    }

    public static void Base64() throws IOException {

        byte[] s = decoder.decodeBuffer("eyJpZCI6MjQ5NzIwMTM2MDA5NDAsInR5cGUiOiJjaGF0IiwibXNnIjp7InRpbWUiOiJcL0RhdGUoMTUyNDIxMDgzMzAxMyswODAwKVwvIiwidXNlciI6eyJ1aWQiOjYzMjMzNDksInVzZXJuYW1lIjoi6b6Z54+g5b+r6YCS5aSn5ZOl5Li25LmD5LiAIiwiZ3JhZGUiOjExLCJuZXdHcmFkZSI6MTcsImF2YXRhciI6Imh0dHA6Ly9waWMucGx1cmVzLm5ldC91c2Vycy9hdmF0YXIvMDA2LzMyMy8zNDkvNjMyMzM0OS82OWE3MDFiZjY5ZmFlOTE4YzZkYjQ4ZjQ3NzAwNGM0YS5qcGcifSwibWVkYWwiOnsicm9vbWlkIjoyMjE0ODU5LCJkb21haW4iOiJ6MTk2OTg0IiwiZmFuIjozNzAwLCJsZXZlbCI6MSwibmFtZSI6IuWGrOelnuS4tiJ9LCJ2aWEiOjEsImNvbnRlbnQiOiLpmLLngavpmLLnm5fpmLLlvpDpg44iLCJjb2xvciI6IjB4ZmZmZmZmIiwic3R5bGUiOjEsIlJvb21JZCI6NDgzNzYsIlJvb21Eb21haW4iOiJ6MTk3MzYwIn0sInJvb21JZCI6NDgzNzZ9");
        System.out.println(new String(s));

    }
}
