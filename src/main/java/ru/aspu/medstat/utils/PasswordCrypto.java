package ru.aspu.medstat.utils;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class PasswordCrypto {
    private static PasswordCrypto instance;
    private static PasswordEncoder passwordEncoder;

    public static PasswordCrypto getInstance() {
        if (instance == null) {
            instance = new PasswordCrypto();
            passwordEncoder = new BCryptPasswordEncoder();
        }

        return instance;
    }

    public PasswordEncoder getPasswordEncoder() {
        return passwordEncoder;
    }

    public String encrypt(String str) {
        return passwordEncoder.encode(str);
    }
}