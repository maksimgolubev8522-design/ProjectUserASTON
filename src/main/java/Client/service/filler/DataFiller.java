package Client.service.filler;

import Client.model.User;
import Client.service.file.FileReaderService;
import Client.service.file.FileWriterService;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;

public class DataFiller {

    private final FileReaderService fileReader;
    private final FileWriterService fileWriter;
    private final Scanner scanner;

    private final ManualFiller manualFiller;
    private final RandomFiller randomFiller;
    private final FileFiller fileFiller;

    public DataFiller() {
        this.fileReader = new FileReaderService();
        this.fileWriter = new FileWriterService();
        this.scanner = new Scanner(System.in);

        this.manualFiller = new ManualFiller();
        this.randomFiller = new RandomFiller();
        this.fileFiller = new FileFiller();
    }

    public List<User> fillFromFile() {
        System.out.print("\nВведите путь к файлу для чтения: ");
        String filePath = scanner.nextLine().trim();

        try {
            if (!fileReader.fileExists(filePath)) {
                System.out.println("Файл не найден: " + fileReader.getAbsolutePath(filePath));
                return null;
            }

            if (!fileReader.isReadable(filePath)) {
                System.out.println("Файл недоступен для чтения: " + filePath);
                return null;
            }

            List<User> users = fileReader.readFromFile(filePath);

            if (users.isEmpty()) {
                System.out.println("Не удалось загрузить пользователей из файла.");
                return null;
            }

            System.out.println("Успешно загружено пользователей: " + users.size());
            return users;

        } catch (IOException e) {
            System.out.println("Ошибка чтения файла: " + e.getMessage());
            return null;
        }
    }

    public boolean saveToFile(List<User> users) {
        if (users == null || users.isEmpty()) {
            System.out.println("Нет данных для сохранения.");
            return false;
        }

        System.out.print("Введите путь для сохранения файла: ");
        String filePath = scanner.nextLine().trim();

        try {
            if (!fileWriter.isWritable(filePath)) {
                System.out.println("Файл недоступен для записи: " + filePath);
                return false;
            }

            System.out.println("\nВыберите режим записи:");
            System.out.println("1. Переписать файл");
            System.out.println("2. Дописать в конец");
            System.out.print("Ваш выбор: ");

            int mode = Integer.parseInt(scanner.nextLine());

            if (mode == 1) {
                fileWriter.writeToFile(users, filePath);
                System.out.println("Данные сохранены в файл: " + filePath);
                return true;
            } else if (mode == 2) {
                fileWriter.appendToFile(users, filePath);
                System.out.println("Данные добавлены в файл: " + filePath);
                return true;
            } else {
                System.out.println("Неправильный выбор. Сохранение отменено.");
                return false;
            }

        } catch (IOException e) {
            System.out.println("Ошибка при записи в файл: " + e.getMessage());
            return false;
        } catch (NumberFormatException e) {
            System.out.println("Ошибка: введите корректное число");
            return false;
        }
    }

    public List<User> chooseFillingSource() {
        while (true) {
            System.out.println("\n=== Выбор источника данных ===");
            System.out.println("1. " + manualFiller.getDescription());
            System.out.println("2. " + randomFiller.getDescription());
            System.out.println("3. " + fileFiller.getDescription());
            System.out.println("0. Отмена");
            System.out.print("Ваш выбор: ");

            try {
                int choice = Integer.parseInt(scanner.nextLine());

                switch (choice) {
                    case 1:
                        return fillManually();
                    case 2:
                        return fillRandomly();
                    case 3:
                        return fillFromFileNew();
                    case 0:
                        System.out.println("Операция отменена");
                        return null;
                    default:
                        System.out.println("❌ Неверный выбор. Попробуйте снова.");
                }
            } catch (NumberFormatException e) {
                System.out.println("❌ Введите число от 0 до 3");
            }
        }
    }

    private List<User> fillManually() {
        System.out.print("Введите количество пользователей для ввода: ");
        try {
            int count = Integer.parseInt(scanner.nextLine());
            return manualFiller.fill(count);
        } catch (NumberFormatException e) {
            System.out.println("❌ Введите корректное число");
            return null;
        }
    }

    private List<User> fillRandomly() {
        System.out.print("Введите количество пользователей для генерации: ");
        try {
            int count = Integer.parseInt(scanner.nextLine());
            return randomFiller.fill(count);
        } catch (NumberFormatException e) {
            System.out.println("❌ Введите корректное число");
            return null;
        }
    }

    private List<User> fillFromFileNew() {
        System.out.print("Введите путь к файлу для чтения: ");
        String filePath = scanner.nextLine().trim();
        return fileFiller.fillFromPath(filePath);
    }

    public boolean hasData(List<User> users) {
        return users != null && !users.isEmpty();
    }

    public String getLastFileStatistics() {
        String lastPath = fileFiller.getLastFilePath();
        if (lastPath == null) {
            return "Файлы еще не загружались";
        }
        return "Последний загруженный файл: " + lastPath;
    }
}