package Client.service.filler;

import Client.model.User;
import Client.service.validation.UserValidator;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RandomFiller implements Filler {

    private final Random random;
    private int totalAttempts = 0;
    private int validationFailures = 0;

    private static final String[] NAMES = {
            "Алексей", "Дмитрий", "Михаил", "Андрей", "Сергей", "Павел", "Николай",
            "Мария", "Анна", "Елена", "Ольга", "Наталья", "Татьяна", "Екатерина",
            "Иван", "Петр", "Максим", "Владимир", "Константин", "Юлия", "Светлана"
    };

    private static final String[] DOMAINS = {
            "gmail.com", "yandex.ru", "mail.ru", "yahoo.com", "outlook.com",
            "bk.ru", "inbox.ru", "list.ru", "rambler.ru", "hotmail.com"
    };

    public RandomFiller() {
        this.random = new Random();
    }

    @Override
    public List<User> fill(int count) {
        if (count <= 0) {
            System.out.println("❌ Количество пользователей должно быть положительным");
            return new ArrayList<>();
        }

        resetStatistics();
        List<User> users = new ArrayList<>();
        System.out.println("\n" + getDescription());
        System.out.println("Генерируется: " + count + " пользователей");

        while (users.size() < count && totalAttempts < count * 20) {
            totalAttempts++;

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
                    System.out.print(".");

                } catch (IllegalArgumentException e) {
                    validationFailures++;
                    System.out.print("!");
                }
            } else {
                validationFailures++;
                System.out.print("?");
            }
        }

        printStatistics();
        return users;
    }

    @Override
    public String getDescription() {
        return "=== Генерация случайных пользователей ===";
    }

    private void resetStatistics() {
        totalAttempts = 0;
        validationFailures = 0;
    }

    private void printStatistics() {
        System.out.println("\n\n📊 Статистика генерации:");
        System.out.println("  - Всего попыток: " + totalAttempts);
        System.out.println("  - Провалов валидации: " + validationFailures);
        System.out.println("  - Процент успеха: " +
                String.format("%.1f%%", (totalAttempts - validationFailures) * 100.0 / totalAttempts));
        System.out.println("✓ Сгенерировано пользователей: " + (totalAttempts - validationFailures));
    }

    private String generateRandomName() {
        return NAMES[random.nextInt(NAMES.length)];
    }

    private String generateRandomPassword() {
        int length = random.nextInt(15) + 6;
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

        String domain = DOMAINS[random.nextInt(DOMAINS.length)];
        return latinName + "@" + domain;
    }

    private String transliterate(String text) {
        String[] cyrillic = {"а", "б", "в", "г", "д", "е", "ё", "ж", "з", "и", "й",
                "к", "л", "м", "н", "о", "п", "р", "с", "т", "у", "ф",
                "х", "ц", "ч", "ш", "щ", "ъ", "ы", "ь", "э", "ю", "я"};
        String[] latin = {"a", "b", "v", "g", "d", "e", "e", "zh", "z", "i", "y",
                "k", "l", "m", "n", "o", "p", "r", "s", "t", "u", "f",
                "kh", "ts", "ch", "sh", "sch", "", "y", "", "e", "yu", "ya"};

        String result = text;
        for (int i = 0; i < cyrillic.length; i++) {
            result = result.replace(cyrillic[i], latin[i]);
        }
        return result;
    }
}
