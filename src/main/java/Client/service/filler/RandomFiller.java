package Client.service.filler;

import Client.model.User;
import Client.service.filler.temp.TempValidator;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RandomFiller implements DataFiller {
    private static final String LETTERS = "abcdefghijklmnopqrstuvwxyz";
    private static final String LETTERS_UPPER = LETTERS.toUpperCase();
    private static final String DIGITS = "0123456789";
    private static final String ALPHANUM = LETTERS + LETTERS_UPPER + DIGITS;
    private static final String[] DOMAINS = {"example.com", "test.org", "demo.net", "mail.ru", "gmail.com"};

    private final Random random = new Random();

    @Override
    public List<User> fill(int count) {
        if (count <= 0) {
            throw new IllegalArgumentException("Количество пользователей должно быть положительным");
        }

        List<User> users = new ArrayList<>();
        int attempts = 0;
        int maxAttempts = count * 100;

        while (users.size() < count && attempts < maxAttempts) {
            User user = generateRandomUser();
            if (user != null && TempValidator.isValidUser(user)) {
                users.add(user);
            }
            attempts++;
        }

        if (users.size() < count) {
            System.err.println("Предупреждение: удалось сгенерировать только " + users.size() +
                    " из " + count + " пользователей");
        }

        return users;
    }

    private User generateRandomUser() {
        try {
            String name = generateRandomName();
            String password = generateRandomPassword();
            String mail = generateRandomEmail();

            if (!TempValidator.isValidName(name) ||
                    !TempValidator.isValidPassword(password) ||
                    !TempValidator.isValidMail(mail)) {
                return null;
            }

            return User.builder()
                    .name(name)
                    .password(password)
                    .mail(mail)
                    .build();
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    private String generateRandomName() {
        int length = 3 + random.nextInt(8); // 3-10 символов
        StringBuilder sb = new StringBuilder(length);
        sb.append(LETTERS_UPPER.charAt(random.nextInt(LETTERS_UPPER.length())));
        for (int i = 1; i < length; i++) {
            sb.append(LETTERS.charAt(random.nextInt(LETTERS.length())));
        }
        return sb.toString();
    }

    private String generateRandomPassword() {
        int length = 6 + random.nextInt(7);
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(ALPHANUM.charAt(random.nextInt(ALPHANUM.length())));
        }
        return sb.toString();
    }

    private String generateRandomEmail() {
        String localPart = generateRandomString(3, 8, ALPHANUM + "._-");
        String domain = DOMAINS[random.nextInt(DOMAINS.length)];
        return localPart + "@" + domain;
    }

    private String generateRandomString(int minLen, int maxLen, String chars) {
        int length = minLen + random.nextInt(maxLen - minLen + 1);
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(chars.charAt(random.nextInt(chars.length())));
        }
        return sb.toString();
    }
}