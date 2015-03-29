package ru.aspu.medstat.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.security.SecureRandom;

public class PasswordUtils {

    private final static SecureRandom random = new SecureRandom();

    public static String generate(int len) {
        char[] chars = "aAbBcCdDeEfFgGhHiIjJkKlLmMnNoOpPqQrRsStTuUvVwWxXyYzZ".toCharArray();

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < len; i++) {
            char c = chars[random.nextInt(chars.length)];
            sb.append(c);
        }
        char[] passwordChars = sb.toString().toCharArray();


        int[] indices = new int[len];
        for (int i = 0; i < len; i++) {
            indices[i] = i;
        }

        for (int i = 0, r = len; i < len / 2; i++, r--) {
            int ri = random.nextInt(r);

            int idx = indices[ri];
            indices[ri] = indices[r - 1];
            indices[r - 1] = idx;

            passwordChars[idx] = (char) (random.nextInt(10) + 48);
        }

        StringBuilder password = new StringBuilder();
        for (char ch : passwordChars) {
            password.append(ch);
        }
        return password.toString();
    }


}

