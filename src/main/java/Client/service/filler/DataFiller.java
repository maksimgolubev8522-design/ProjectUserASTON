package Client.service.filler;

import Client.model.User;
import java.util.List;
import java.util.Scanner;

public class DataFiller {
    private final Scanner scanner;

    public DataFiller() {
        this.scanner = new Scanner(System.in);
    }

    public List<User> chooseFillingSource() {
        while (true) {
            System.out.println("\n=== Выбор источника данных ===");
            System.out.println("1. Ручной ввод");
            System.out.println("2. Случайная генерация");
            System.out.println("3. Загрузка из файла");
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
                        return fillFromFile();
                    case 0:
                        System.out.println("Операция отменена");
                        return null;
                    default:
                        System.out.println("Неверный выбор. Попробуйте снова.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Введите число от 0 до 3");
            }
        }
    }

    private List<User> fillManually() {
        System.out.print("Введите количество пользователей: ");
        try {
            int count = Integer.parseInt(scanner.nextLine());
            return new ManualFiller().fill(count);
        } catch (NumberFormatException e) {
            System.out.println("Введите корректное число");
            return null;
        }
    }

    private List<User> fillRandomly() {
        System.out.print("Введите количество пользователей: ");
        try {
            int count = Integer.parseInt(scanner.nextLine());
            return new RandomFiller().fill(count);
        } catch (NumberFormatException e) {
            System.out.println("Введите корректное число");
            return null;
        }
    }

    private List<User> fillFromFile() {
        System.out.print("Введите путь к файлу: ");
        String filePath = scanner.nextLine().trim();
        return new FileFiller().fillFromPath(filePath);
    }
}