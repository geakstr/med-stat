package ru.aspu.medstat.utils;

import java.util.regex.Pattern;

public class EmailUtils {
    public static final String PATTERN = "^[a-zA-Z0-9.!#$%&’*+/=?^_`{|}~-]+@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*$";
    private static final Pattern pattern = Pattern.compile(PATTERN);

    public static boolean validate(final String email) {
        return pattern.matcher(email).matches();
    }
}
