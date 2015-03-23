package ru.aspu.medstat.utils;

public class FormatUtils {
    public static String normalizePhoneNumber(String phone) {
        return phone.replaceAll("[^0-9]", "");
    }
}
