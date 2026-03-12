package Client.service.filler.temp;

import Client.model.User;
import java.io.PrintWriter;
import java.util.List;

public class TempFileReaderTest {

    public static void main(String[] args) {
        System.out.println("=== ТЕСТИРОВАНИЕ ВРЕМЕННОГО FILE READER ===\n");

        createTestFile();

        testFileReading();

        testFileStats();

        testNonExistentFile();
    }

    private static void createTestFile() {
        try (PrintWriter writer = new PrintWriter("temp_test_users.txt", "UTF-8")) {
            writer.println("# Тестовый файл пользователей");
            writer.println("Иван Петров|pass123|ivan@example.com");
            writer.println("Мария Сидорова|secure456|maria@test.org");
            writer.println("");
            writer.println("Петр Иванов|qwerty123|petr@demo.net");
            writer.println("Анна|short|anna@mail.ru");
            writer.println("Елена|password123|elena@gmail.com");
            writer.println("Олег|pass|oleg@mail.ru");
            writer.println("  Татьяна  |  pass12345  |  tatiana@example.com  ");
            System.out.println("Тестовый файл создан: temp_test_users.txt\n");
        } catch (Exception e) {
            System.err.println("Ошибка создания тестового файла: " + e.getMessage());
        }
    }

    private static void testFileReading() {
        System.out.println("--- Тест чтения файла ---");
        TempFileReader reader = new TempFileReader();

        try {
            List<User> users = reader.readUsersFromFile("temp_test_users.txt");
            System.out.println("Прочитано пользователей: " + users.size());
            for (int i = 0; i < users.size(); i++) {
                System.out.println((i + 1) + ". " + users.get(i));
            }
        } catch (Exception e) {
            System.err.println("Ошибка: " + e.getMessage());
        }
        System.out.println();
    }

    private static void testFileStats() {
        System.out.println("--- Тест статистики файла ---");
        TempFileReader reader = new TempFileReader();

        try {
            TempFileReader.FileStats stats = reader.getFileStats("temp_test_users.txt");
            System.out.println(stats);
        } catch (Exception e) {
            System.err.println("Ошибка: " + e.getMessage());
        }
        System.out.println();
    }

    private static void testNonExistentFile() {
        System.out.println("--- Тест с несуществующим файлом ---");
        TempFileReader reader = new TempFileReader();

        try {
            List<User> users = reader.readUsersFromFile("nonexistent.txt");
            System.out.println("Прочитано пользователей: " + users.size());
        } catch (Exception e) {
            System.out.println("Ожидаемая ошибка: " + e.getMessage());
        }
        System.out.println();
    }
}