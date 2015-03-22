package ru.aspu.medstat.utils;

public class TelephoneUtil {
    public static String normalize(String telephone) {
        return telephone.replaceAll("[^0-9]", "");
    }
}
