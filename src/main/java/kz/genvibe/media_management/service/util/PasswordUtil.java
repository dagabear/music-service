package kz.genvibe.media_management.service.util;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.stream.Collectors;

public class PasswordUtil {

    private static final String UPPER = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String LOWER = "abcdefghijklmnopqrstuvwxyz";
    private static final String DIGITS = "0123456789";
    private static final String SPECIAL = "!@#$%^&*()-_=+";
    private static final String ALL = UPPER + LOWER + DIGITS + SPECIAL;

    private static final SecureRandom RANDOM = new SecureRandom();

    public static String generateRandomPassword(int length) {
        var passwordChars = new ArrayList<>();

        passwordChars.add(UPPER.charAt(RANDOM.nextInt(UPPER.length())));
        passwordChars.add(LOWER.charAt(RANDOM.nextInt(LOWER.length())));
        passwordChars.add(DIGITS.charAt(RANDOM.nextInt(DIGITS.length())));
        passwordChars.add(SPECIAL.charAt(RANDOM.nextInt(SPECIAL.length())));

        for (int i = 4; i < length; i++) {
            passwordChars.add(ALL.charAt(RANDOM.nextInt(ALL.length())));
        }

        Collections.shuffle(passwordChars, RANDOM);

        return passwordChars.stream()
            .map(String::valueOf)
            .collect(Collectors.joining());
    }

}
