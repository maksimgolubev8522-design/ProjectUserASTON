package Client.service.file;

import Client.model.User;
import Client.service.validation.UserValidator;

import java.io.IOException;
import java.nio.file.*;
import java.util.List;

public class FileWriterService {

    public void writeToFile(List<User> users, String filePath) throws IOException {

        for (User user : users) {
            if (!UserValidator.isValidUser(user.getName(), user.getPassword(), user.getMail())) {
                throw new IllegalArgumentException("Попытка сохранить невалидного пользователя: " + user);
            }
        }

        List<String> lines = users.stream()
                .map(this::formatUser)
                .toList();

        Path path = Paths.get(filePath);

        if (path.getParent() != null) {
            Files.createDirectories(path.getParent());
        }

        Files.write(path, lines, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);

        System.out.println("Сохранено пользователей: " + users.size());
        System.out.println("Файл: " + path.toAbsolutePath());
    }

    public void appendToFile(List<User> users, String filePath) throws IOException {
        for (User user : users) {
            if (!UserValidator.isValidUser(user.getName(), user.getPassword(), user.getMail())) {
                throw new IllegalArgumentException("Попытка сохранить невалидного пользователя: " + user);
            }
        }

        List<String> lines = users.stream()
                .map(this::formatUser)
                .toList();

        Path path = Paths.get(filePath);

        if (path.getParent() != null && !Files.exists(path.getParent())) {
            Files.createDirectories(path.getParent());
        }

        if (!Files.exists(path)) {
            List<String> header = List.of("# Файл с пользователями (name;password;mail)");
            Files.write(path, header, StandardOpenOption.CREATE);
        }

        Files.write(path, lines, StandardOpenOption.CREATE, StandardOpenOption.APPEND);

        System.out.println("Добавлено пользователей: " + users.size());
        System.out.println("Файл: " + path.toAbsolutePath());
    }

    private String formatUser(User user) {
        return String.format("%s;%s;%s",
                user.getName(),
                user.getPassword(),
                user.getMail());
    }


    public boolean isWritable(String filePath) {
        Path path = Paths.get(filePath);

        if (!Files.exists(path)) {
            Path parent = path.getParent();
            return parent == null || Files.isWritable(parent);
        }

        return Files.isWritable(path);
    }
}
