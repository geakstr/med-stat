package ru.aspu.medstat.utils;

import java.util.regex.Pattern;

public class EmailUtils {
    public static final String PATTERN = "^[а-яёА-ЯЁa-zA-Z0-9.!#$%&’*+/=?^_`{|}~-]+@[а-яёА-ЯЁa-zA-Z0-9-]+(?:\\.[а-яёА-ЯЁa-zA-Z0-9-]+)*$";
    private static final Pattern pattern = Pattern.compile(PATTERN);

    public static boolean validate(final String email) {
        return pattern.matcher(email).matches();
    }
}
