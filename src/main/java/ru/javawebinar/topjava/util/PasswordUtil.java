package ru.javawebinar.topjava.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StringUtils;

public class PasswordUtil {
    public static final PasswordEncoder PASSWORD_ENCODER = new BCryptPasswordEncoder();

    public static PasswordEncoder getPasswordEncoder() {
        return PASSWORD_ENCODER;
    }

    public static String encode(String newPassword) {
        if (!StringUtils.hasText(newPassword)) {
            return null;
        }
        return PASSWORD_ENCODER.encode(newPassword);
    }

    public static boolean isMatch(String rawPassword, String password) {
        return PASSWORD_ENCODER.matches(rawPassword, password);
    }
}
