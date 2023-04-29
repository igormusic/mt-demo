package com.example.appmock;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Random;

public class Utility {

    public static final String BASE62_CHARS = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    public static final String SENDER_BIC = "BICFOOYYAXXX";

    public static String generateReferenceNumber() {

        LocalDateTime dateTime = LocalDateTime.of(2100,1,1,0,0);
        long millisSinceEpoch = dateTime.toInstant(ZoneOffset.UTC).toEpochMilli();

        StringBuilder sb = new StringBuilder();
        sb.append(toBase62(millisSinceEpoch));
        Random random = new Random();

        for (int i = 0; i < 8; i++) {
            int randomIndex = random.nextInt(BASE62_CHARS.length());
            char randomChar = BASE62_CHARS.charAt(randomIndex);
            sb.append(randomChar);
        }
        return sb.toString();
    }
    public static <T> T getRandomRecord(List<T> list) {
        int index = new Random().nextInt(list.size());
        return list.get(index);
    }

    public static String toBase62(long num) {
        StringBuilder sb = new StringBuilder();
        do {
            sb.append(BASE62_CHARS.charAt((int)(num % 62)));
            num /= 62;
        } while (num > 0);
        return sb.reverse().toString();
    }

    public static long fromBase62(String str) {
        long result = 0;
        for (int i = 0; i < str.length(); i++) {
            result = result * 62 + BASE62_CHARS.indexOf(str.charAt(i));
        }
        return result;
    }

}
