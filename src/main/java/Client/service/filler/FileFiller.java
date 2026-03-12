package Client.service.filler;

import Client.model.User;
import Client.service.file.FileReaderService;
import Client.service.validation.UserValidator;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FileFiller implements Filler {

    private final FileReaderService fileReader;
    private String lastFilePath;
    private int totalLines = 0;
    private int validLines = 0;

    public FileFiller() {
        this.fileReader = new FileReaderService();
    }

    @Override
    public List<User> fill(int count) {
        System.out.println("❌ Для файлового заполнения используйте fillFromPath(String filePath)");
        System.out.println("   Параметр count игнорируется - загружаются все пользователи из файла");
        return new ArrayList<>();
    }

    public List<User> fillFromPath(String filePath) {
        if (filePath == null || filePath.trim().isEmpty()) {
            System.out.println("❌ Не указан путь к файлу");
            return new ArrayList<>();
        }

        this.lastFilePath = filePath;
        this.totalLines = 0;
        this.validLines = 0;

        System.out.println("\n" + getDescription());
        System.out.println("Файл: " + filePath);

        if (!checkFileAvailability(filePath)) {
            return new ArrayList<>();
        }

        try {
            List<User> users = readAndValidateUsers(filePath);

            if (users.isEmpty()) {
                System.out.println("❌ Не удалось загрузить пользователей из файла.");
                return new ArrayList<>();
            }

            printStatistics();
            return users;

        } catch (IOException e) {
            System.out.println("❌ Ошибка чтения файла: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    @Override
    public String getDescription() {
        return "=== Заполнение из файла ===";
    }

    private boolean checkFileAvailability(String filePath) {
        if (!fileReader.fileExists(filePath)) {
            System.out.println("❌ Файл не найден: " + fileReader.getAbsolutePath(filePath));
            return false;
        }

        if (!fileReader.isReadable(filePath)) {
            System.out.println("❌ Файл недоступен для чтения: " + filePath);
            return false;
        }

        return true;
    }

    private List<User> readAndValidateUsers(String filePath) throws IOException {
        List<User> users = new ArrayList<>();
        List<String> lines = java.nio.file.Files.readAllLines(java.nio.file.Paths.get(filePath));

        totalLines = lines.size();
        System.out.println("Всего строк в файле: " + totalLines);

        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i).trim();


            if (line.isEmpty() || line.startsWith("#")) {
                continue;
            }

            User user = parseAndValidateLine(line, i + 1);
            if (user != null) {
                users.add(user);
                validLines++;
            }
        }

        return users;
    }

    private User parseAndValidateLine(String line, int lineNumber) {
        String[] parts = line.split(";");

        if (parts.length != 3) {
            System.out.println("⚠️ Строка " + lineNumber + ": неверный формат. Ожидается: name;password;mail");
            return null;
        }

        String name = parts[0].trim();
        String password = parts[1].trim();
        String mail = parts[2].trim();

        if (!UserValidator.isValidUser(name, password, mail)) {
            System.out.println("⚠️ Строка " + lineNumber + ": невалидные данные:");
            if (!UserValidator.isValidName(name)) {
                System.out.println("   - " + UserValidator.getNameErrorMessage(name));
            }
            if (!UserValidator.isValidPassword(password)) {
                System.out.println("   - " + UserValidator.getPasswordErrorMessage(password));
            }
            if (!UserValidator.isValidMail(mail)) {
                System.out.println("   - " + UserValidator.getMailErrorMessage(mail));
            }
            return null;
        }

        try {
            return User.builder()
                    .name(name)
                    .password(password)
                    .mail(mail)
                    .build();
        } catch (IllegalArgumentException e) {
            System.out.println("⚠️ Строка " + lineNumber + ": ошибка создания - " + e.getMessage());
            return null;
        }
    }

    private void printStatistics() {
        System.out.println("\n📊 Статистика загрузки:");
        System.out.println("  - Всего обработано строк: " + totalLines);
        System.out.println("  - Валидных пользователей: " + validLines);
        System.out.println("  - Процент валидных: " +
                String.format("%.1f%%", totalLines > 0 ? validLines * 100.0 / totalLines : 0));
        System.out.println("✓ Успешно загружено: " + validLines);
    }

    public boolean fileExists(String filePath) {
        return fileReader.fileExists(filePath);
    }

    public String getAbsolutePath(String filePath) {
        return fileReader.getAbsolutePath(filePath);
    }

    public String getLastFilePath() {
        return lastFilePath;
    }
}