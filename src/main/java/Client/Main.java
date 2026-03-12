package Client;

import Client.collection.UserList;
import Client.model.User;
import Client.service.file.FileWriterService;
import Client.service.filler.*;
import Client.strategy.*;

import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

public class Main {

    private static final Scanner scanner = new Scanner(System.in);
    private static UserList userList = new UserList();
    private static SortStrategy currentStrategy = new InsertionSort();

    public static void main(String[] args) {

        while (true) {
            printMenu();

            int choice = readInt();
            scanner.nextLine();

            switch (choice) {
                case 1 -> fillUsers(new ManualFiller());
                case 2 -> fillUsers(new RandomFiller());
                case 3 -> fillUsers(new FileFiller());
                case 4 -> chooseSortStrategy();
                case 5 -> sortUsers();
                case 6 -> saveUsers();
                case 0 -> {
                    System.out.println("Выход из программы...");
                    return;
                }
                default -> System.out.println("Неверный пункт меню.");
            }
        }
    }

    private static void printMenu() {
        System.out.println("\n====== МЕНЮ ======");
        System.out.println("1. Ввести пользователей вручную");
        System.out.println("2. Сгенерировать случайных пользователей");
        System.out.println("3. Загрузить пользователей из файла");
        System.out.println("4. Выбрать стратегию сортировки");
        System.out.println("5. Отсортировать пользователей");
        System.out.println("6. Сохранить в файл");
        System.out.println("0. Выход");
        System.out.print("Выберите пункт: ");
    }

    private static int readInt() {
        while (!scanner.hasNextInt()) {
            System.out.println("Введите число.");
            scanner.next();
        }
        return scanner.nextInt();
    }

    private static void fillUsers(Filler filler) {
        List<User> newUsers = filler.fill(0); // параметр не используется для FileFiller

        if (newUsers != null && !newUsers.isEmpty()) {
            for (User user : newUsers) {
                userList.addUser(user);
            }
            System.out.println("Данные успешно добавлены. Всего пользователей: " + userList.size());
            printUsers();
        } else {
            System.out.println("Не удалось добавить пользователей.");
        }
    }

    private static void chooseSortStrategy() {
        System.out.println("\nВыберите алгоритм сортировки:");
        System.out.println("1. Bubble Sort");
        System.out.println("2. Insertion Sort");
        System.out.println("3. Even-Odd Sort");
        System.out.print("Ваш выбор: ");

        int choice = readInt();
        scanner.nextLine();

        switch (choice) {
            case 1 -> currentStrategy = new BubbleSort();
            case 2 -> currentStrategy = new InsertionSort();
            case 3 -> currentStrategy = new EvenOddSort();
            default -> {
                System.out.println("Неверный выбор. Оставлена текущая стратегия.");
                return;
            }
        }
        System.out.println("Выбрана стратегия: " + currentStrategy.getClass().getSimpleName());
    }

    private static void sortUsers() {
        if (userList.isEmpty()) {
            System.out.println("Список пользователей пуст.");
            return;
        }

        Comparator<User> comparator = Comparator
                .comparing(User::getName)
                .thenComparing(User::getPassword)
                .thenComparing(User::getMail);

        userList.sort(currentStrategy, comparator);
        System.out.println("Сортировка выполнена. Стратегия: " + currentStrategy.getClass().getSimpleName());
        printUsers();
    }

    private static void saveUsers() {
        if (userList.isEmpty()) {
            System.out.println("Нет данных для сохранения.");
            return;
        }

        System.out.print("Введите путь для сохранения файла: ");
        String filePath = scanner.nextLine().trim();

        FileWriterService writerService = new FileWriterService();

        System.out.println("Выберите режим записи:");
        System.out.println("1. Переписать файл");
        System.out.println("2. Дописать в конец");
        System.out.print("Ваш выбор: ");

        try {
            int mode = Integer.parseInt(scanner.nextLine());

            if (mode == 1) {
                writerService.writeToFile(userList.getUsers(), filePath);
                System.out.println("Данные сохранены в файл: " + filePath);
            } else if (mode == 2) {
                writerService.appendToFile(userList.getUsers(), filePath);
                System.out.println("Данные добавлены в файл: " + filePath);
            } else {
                System.out.println("Неверный выбор.");
            }
        } catch (Exception e) {
            System.out.println("Ошибка при сохранении: " + e.getMessage());
        }
    }

    private static void printUsers() {
        if (userList.isEmpty()) {
            System.out.println("Список пользователей пуст.");
            return;
        }

        System.out.println("\n--- ПОЛЬЗОВАТЕЛИ (" + userList.size() + ") ---");
        for (int i = 0; i < userList.size(); i++) {
            System.out.println((i + 1) + ". " + userList.getUser(i));
        }
    }
}