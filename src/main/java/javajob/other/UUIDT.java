package javajob.other;

import java.util.UUID;

public class UUIDT {

    public static void main(String[] args) {
        for (int i = 0; i < 10; i++) {
            String uuid = UUID.randomUUID().toString();
            System.out.println(uuid);
        }
    }
}
