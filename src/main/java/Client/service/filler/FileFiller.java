package Client.service.filler;

import Client.model.User;
import Client.service.file.FileReaderService;
import Client.service.validation.UserValidator;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

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
        System.out.print("Введите путь к файлу: ");
        Scanner scanner = new Scanner(System.in);
        String filePath = scanner.nextLine().trim();
        return fillFromPath(filePath);
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
            // Используем Stream API для чтения и обработки
            List<User> users = readAndValidateUsersWithStream(filePath);

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

    // Новая версия с использованием Stream API
    private List<User> readAndValidateUsersWithStream(String filePath) throws IOException {
        List<String> lines = java.nio.file.Files.readAllLines(java.nio.file.Paths.get(filePath));
        totalLines = lines.size();

        System.out.println("Всего строк в файле: " + totalLines);

        List<User> users = lines.stream()
                .skip(0) // можно пропустить заголовки если нужно
                .map(String::trim)
                .filter(line -> !line.isEmpty() && !line.startsWith("#"))
                .map(this::parseLineToUser)
                .filter(user -> user != null)
                .toList();

        validLines = users.size();
        return users;
    }

    private User parseLineToUser(String line) {
        String[] parts = line.split(";");
        if (parts.length != 3) return null;

        String name = parts[0].trim();
        String password = parts[1].trim();
        String mail = parts[2].trim();

        if (!UserValidator.isValidUser(name, password, mail)) return null;

        try {
            return User.builder()
                    .name(name)
                    .password(password)
                    .mail(mail)
                    .build();
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    @Override
    public String getDescription() {
        return "=== Заполнение из файла (Stream API) ===";
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

    private void printStatistics() {
        System.out.println("\n📊 Статистика загрузки (Stream API):");
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