package Client.service.filler;

import Client.model.User;
import Client.service.validation.UserValidator;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RandomFiller implements Filler {

    private final Random random;
    private int generatedCount = 0;
    private int attempts = 0;

    private static final String[] NAMES = {
            "Алексей", "Дмитрий", "Михаил", "Андрей", "Сергей",
            "Мария", "Анна", "Елена", "Ольга", "Екатерина"
    };

    private static final String[] DOMAINS = {
            "gmail.com", "yandex.ru", "mail.ru", "yahoo.com", "outlook.com"
    };

    public RandomFiller() {
        this.random = new Random();
    }

    @Override
    public List<User> fill(int count) {
        if (count <= 0) {
            System.out.println("Количество должно быть положительным");
            return new ArrayList<>();
        }

        resetStats();
        List<User> users = new ArrayList<>();
        System.out.println("\n" + getDescription());
        System.out.print("Генерация: ");

        while (users.size() < count && attempts < count * 10) {
            attempts++;

            String name = generateRandomName();
            String password = generateRandomPassword();
            String mail = generateRandomMail(name);

            if (UserValidator.isValidUser(name, password, mail)) {
                try {
                    User user = User.builder()
                            .name(name)
                            .password(password)
                            .mail(mail)
                            .build();
                    users.add(user);
                    generatedCount++;
                    if (generatedCount % 10 == 0) System.out.print(".");
                } catch (IllegalArgumentException e) {
                    // игнорируем
                }
            }
        }

        printStats();
        return users;
    }

    @Override
    public String getDescription() {
        return "=== Генерация случайных пользователей ===";
    }

    private void resetStats() {
        generatedCount = 0;
        attempts = 0;
    }

    private void printStats() {
        System.out.println("\nСгенерировано: " + generatedCount +
                " (попыток: " + attempts + ")");
    }

    private String generateRandomName() {
        return NAMES[random.nextInt(NAMES.length)];
    }

    private String generateRandomPassword() {
        int length = random.nextInt(10) + 6; // 6-15 символов
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder password = new StringBuilder();
        for (int i = 0; i < length; i++) {
            password.append(chars.charAt(random.nextInt(chars.length())));
        }
        return password.toString();
    }

    private String generateRandomMail(String name) {
        String latinName = transliterate(name.toLowerCase());
        if (random.nextBoolean()) {
            latinName += random.nextInt(1000);
        }
        return latinName + "@" + DOMAINS[random.nextInt(DOMAINS.length)];
    }

    private String transliterate(String text) {
        String[][] map = {
                {"а", "a"}, {"б", "b"}, {"в", "v"}, {"г", "g"}, {"д", "d"},
                {"е", "e"}, {"ё", "e"}, {"ж", "zh"}, {"з", "z"}, {"и", "i"},
                {"й", "y"}, {"к", "k"}, {"л", "l"}, {"м", "m"}, {"н", "n"},
                {"о", "o"}, {"п", "p"}, {"р", "r"}, {"с", "s"}, {"т", "t"},
                {"у", "u"}, {"ф", "f"}, {"х", "kh"}, {"ц", "ts"}, {"ч", "ch"},
                {"ш", "sh"}, {"щ", "sch"}, {"ъ", ""}, {"ы", "y"}, {"ь", ""},
                {"э", "e"}, {"ю", "yu"}, {"я", "ya"}
        };

        String result = text;
        for (String[] pair : map) {
            result = result.replace(pair[0], pair[1]);
        }
        return result;
    }
}