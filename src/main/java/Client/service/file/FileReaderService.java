package Client.service.file;

import Client.model.User;
import Client.service.validation.UserValidator;

import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

public class FileReaderService {

    public List<User> readFromFile(String filePath) throws IOException {
        List<User> users = new ArrayList<>();
        List<String> lines = Files.readAllLines(Paths.get(filePath));

        System.out.println("Чтение файла: " + filePath);
        System.out.println("Всего строк: " + lines.size());

        int validCount = 0;
        int invalidCount = 0;

        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i).trim();

            if (line.isEmpty() || line.startsWith("#")) {
                continue;
            }

            String[] parts = line.split(";");

            if (parts.length != 3) {
                System.err.println("Строка " + (i + 1) + ": неверный формат. Ожидается: name;password;mail");
                invalidCount++;
                continue;
            }

            String name = parts[0].trim();
            String password = parts[1].trim();
            String mail = parts[2].trim();

            if (!UserValidator.isValidUser(name, password, mail)) {
                System.err.println("Строка " + (i + 1) + ": невалидные данные:");
                if (!UserValidator.isValidName(name)) {
                    System.err.println("  - " + UserValidator.getNameErrorMessage(name));
                }
                if (!UserValidator.isValidPassword(password)) {
                    System.err.println("  - " + UserValidator.getPasswordErrorMessage(password));
                }
                if (!UserValidator.isValidMail(mail)) {
                    System.err.println("  - " + UserValidator.getMailErrorMessage(mail));
                }
                invalidCount++;
                continue;
            }

            try {
                User user = User.builder()
                        .name(name)
                        .password(password)
                        .mail(mail)
                        .build();
                users.add(user);
                validCount++;
            } catch (IllegalArgumentException e) {
                System.err.println("Строка " + (i + 1) + ": ошибка создания пользователя - " + e.getMessage());
                invalidCount++;
            }
        }

        System.out.println("Результат чтения:");
        System.out.println("  - Успешно загружено: " + validCount);
        System.out.println("  - Пропущено (ошибки): " + invalidCount);

        return users;
    }

    public boolean fileExists(String filePath) {
        return Files.exists(Paths.get(filePath));
    }

    public String getAbsolutePath(String filePath) {
        return Paths.get(filePath).toAbsolutePath().toString();
    }

    public boolean isReadable(String filePath) {
        Path path = Paths.get(filePath);
        return Files.exists(path) && Files.isReadable(path);
    }
}
