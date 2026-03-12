package Client.service.filler.temp;

import Client.model.User;


import java.io.*;
import java.util.ArrayList;
import java.util.List;


public class TempFileReader implements TemporaryCode {

    private static final String DELIMITER = "\\|";
    private static final String ENCODING = "UTF-8";


    public List<User> readUsersFromFile(String filename) throws IOException {
        List<User> users = new ArrayList<>();
        File file = new File(filename);

        if (!file.exists()) {
            throw new FileNotFoundException("Файл не найден: " + filename);
        }

        if (!file.canRead()) {
            throw new IOException("Нет прав на чтение файла: " + filename);
        }

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(new FileInputStream(file), ENCODING))) {

            String line;
            int lineNumber = 0;
            int validUsers = 0;
            int skippedUsers = 0;

            while ((line = reader.readLine()) != null) {
                lineNumber++;
                line = line.trim();

                if (line.isEmpty() || line.startsWith("#")) {
                    continue;
                }

                try {
                    User user = parseUserFromLine(line, lineNumber);
                    if (user != null) {
                        users.add(user);
                        validUsers++;
                    } else {
                        skippedUsers++;
                    }
                } catch (IllegalArgumentException e) {
                    System.err.println("Ошибка в строке " + lineNumber + ": " + e.getMessage());
                    System.err.println("Строка: " + line);
                    skippedUsers++;
                }
            }

            System.out.printf("Файл '%s' прочитан: всего строк %d, валидных пользователей %d, пропущено %d%n",
                    filename, lineNumber, validUsers, skippedUsers);

        } catch (UnsupportedEncodingException e) {
            throw new IOException("Неподдерживаемая кодировка: " + ENCODING, e);
        } catch (IOException e) {
            throw new IOException("Ошибка при чтении файла " + filename + ": " + e.getMessage(), e);
        }

        return users;
    }


    private User parseUserFromLine(String line, int lineNumber) throws IllegalArgumentException {
        String[] parts = line.split(DELIMITER, -1); // -1 чтобы сохранить пустые значения

        if (parts.length != 3) {
            throw new IllegalArgumentException(String.format(
                    "Неверный формат строки. Ожидается 3 поля (name|password|mail), получено %d",
                    parts.length));
        }

        String name = parts[0].trim();
        String password = parts[1];
        String mail = parts[2].trim();

        if (name.isEmpty()) {
            throw new IllegalArgumentException("Имя не может быть пустым");
        }
        if (password.isEmpty()) {
            throw new IllegalArgumentException("Пароль не может быть пустым");
        }
        if (mail.isEmpty()) {
            throw new IllegalArgumentException("Email не может быть пустым");
        }

        if (!TempValidator.isValidName(name)) {
            throw new IllegalArgumentException("Некорректное имя: " + TempValidator.getNameErrorMessage(name));
        }
        if (!TempValidator.isValidPassword(password)) {
            throw new IllegalArgumentException("Некорректный пароль: " + TempValidator.getPasswordErrorMessage(password));
        }
        if (!TempValidator.isValidMail(mail)) {
            throw new IllegalArgumentException("Некорректный email: " + TempValidator.getMailErrorMessage(mail));
        }

        try {
            return User.builder()
                    .name(name)
                    .password(password)
                    .mail(mail)
                    .build();
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Ошибка создания пользователя: " + e.getMessage());
        }
    }


    public boolean isValidFileFormat(String filename) {
        try {
            List<User> users = readUsersFromFile(filename);
            return !users.isEmpty();
        } catch (IOException e) {
            return false;
        }
    }

    public FileStats getFileStats(String filename) throws IOException {
        File file = new File(filename);
        if (!file.exists()) {
            throw new FileNotFoundException("Файл не найден: " + filename);
        }

        int totalLines = 0;
        int validUsers = 0;
        int invalidLines = 0;
        int emptyLines = 0;

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(new FileInputStream(file), ENCODING))) {

            String line;
            while ((line = reader.readLine()) != null) {
                totalLines++;
                line = line.trim();

                if (line.isEmpty() || line.startsWith("#")) {
                    emptyLines++;
                    continue;
                }

                try {
                    User user = parseUserFromLine(line, totalLines);
                    if (user != null) {
                        validUsers++;
                    }
                } catch (IllegalArgumentException e) {
                    invalidLines++;
                }
            }
        }

        return new FileStats(filename, totalLines, validUsers, invalidLines, emptyLines);
    }


    public static class FileStats implements TemporaryCode {
        private final String filename;
        private final int totalLines;
        private final int validUsers;
        private final int invalidLines;
        private final int emptyLines;

        public FileStats(String filename, int totalLines, int validUsers, int invalidLines, int emptyLines) {
            this.filename = filename;
            this.totalLines = totalLines;
            this.validUsers = validUsers;
            this.invalidLines = invalidLines;
            this.emptyLines = emptyLines;
        }

        @Override
        public String toString() {
            return String.format(
                    "Статистика файла '%s':\n" +
                            "  Всего строк: %d\n" +
                            "  Валидных пользователей: %d\n" +
                            "  Невалидных строк: %d\n" +
                            "  Пустых строк/комментариев: %d",
                    filename, totalLines, validUsers, invalidLines, emptyLines);
        }

        public String getFilename() { return filename; }
        public int getTotalLines() { return totalLines; }
        public int getValidUsers() { return validUsers; }
        public int getInvalidLines() { return invalidLines; }
        public int getEmptyLines() { return emptyLines; }
    }
}