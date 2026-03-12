package Client.service.file;

import Client.model.User;
import Client.service.validation.UserValidator;

import java.io.IOException;
import java.nio.file.*;
import java.util.List;

/**
 * Сервис для записи пользователей в файл
 * Формат файла: name;password;mail
 */
public class FileWriterService {

    /**
     * Сохраняет пользователей в файл (перезаписывает)
     *
     * @param users    список пользователей
     * @param filePath путь к файлу
     * @throws IOException если ошибка записи
     */
    public void writeToFile(List<User> users, String filePath) throws IOException {
        // Валидируем всех пользователей перед записью
        for (User user : users) {
            if (!UserValidator.isValidUser(user.getName(), user.getPassword(), user.getMail())) {
                throw new IllegalArgumentException("Попытка сохранить невалидного пользователя: " + user);
            }
        }

        List<String> lines = users.stream()
                .map(this::formatUser)
                .toList();

        Path path = Paths.get(filePath);

        // Создаем родительские директории если нужно
        if (path.getParent() != null) {
            Files.createDirectories(path.getParent());
        }

        Files.write(path, lines, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);

        System.out.println("Сохранено пользователей: " + users.size());
        System.out.println("Файл: " + path.toAbsolutePath());
    }

    /**
     * Добавляет пользователей в конец файла
     *
     * @param users    список пользователей
     * @param filePath путь к файлу
     * @throws IOException если ошибка записи
     */
    public void appendToFile(List<User> users, String filePath) throws IOException {
        // Валидируем всех пользователей перед записью
        for (User user : users) {
            if (!UserValidator.isValidUser(user.getName(), user.getPassword(), user.getMail())) {
                throw new IllegalArgumentException("Попытка сохранить невалидного пользователя: " + user);
            }
        }

        List<String> lines = users.stream()
                .map(this::formatUser)
                .toList();

        Path path = Paths.get(filePath);

        // Создаем родительские директории если нужно
        if (path.getParent() != null && !Files.exists(path.getParent())) {
            Files.createDirectories(path.getParent());
        }

        // Если файл не существует, создаем с заголовком
        if (!Files.exists(path)) {
            List<String> header = List.of("# Файл с пользователями (name;password;mail)");
            Files.write(path, header, StandardOpenOption.CREATE);
        }

        Files.write(path, lines, StandardOpenOption.CREATE, StandardOpenOption.APPEND);

        System.out.println("Добавлено пользователей: " + users.size());
        System.out.println("Файл: " + path.toAbsolutePath());
    }

    /**
     * Форматирует пользователя для записи в файл
     *
     * @param user пользователь
     * @return строка в формате "name;password;mail"
     */
    private String formatUser(User user) {
        return String.format("%s;%s;%s",
                user.getName(),
                user.getPassword(),
                user.getMail());
    }

    /**
     * Проверяет доступен ли файл для записи
     *
     * @param filePath путь к файлу
     * @return true если можно писать
     */
    public boolean isWritable(String filePath) {
        Path path = Paths.get(filePath);

        // Если файл не существует, проверяем родительскую директорию
        if (!Files.exists(path)) {
            Path parent = path.getParent();
            return parent == null || Files.isWritable(parent);
        }

        return Files.isWritable(path);
    }
}
