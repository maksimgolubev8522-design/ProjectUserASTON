package Client;

import Client.model.User;
import Client.service.file.FileWriterService;
import Client.service.filler.DataFiller;
import Client.service.filler.FileFiller;
import Client.service.filler.ManualFiller;
import Client.service.filler.RandomFiller;
import Client.strategy.BubbleSort;
import Client.strategy.InsertionSort;
import Client.strategy.SortStrategy;

import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

public class Main {

    private static final Scanner scanner = new Scanner(System.in);

    private static List<User> users;

    public static void main(String[] args) {

        FileWriterService writerService = new FileWriterService();

        while (true) {

            printMenu();

            int choice = readInt();

            switch (choice) {

                case 1 -> fillUsers(new ManualFiller());

                case 2 -> fillUsers(new RandomFiller());

                case 3 -> fillUsers(new FileFiller());

                case 4 -> sortUsers();

                case 5 -> saveUsers(writerService);

                case 0 -> {
                    System.out.println("Выход из программы...");
                    return;
                }

                default -> System.out.println("Неверный пункт меню.");
            }
        }
    }

    private static void printMenu() {

        System.out.println("\n====== MENU ======");
        System.out.println("1. Ввести пользователей вручную");
        System.out.println("2. Сгенерировать случайных пользователей");
        System.out.println("3. Загрузить пользователей из файла");
        System.out.println("4. Отсортировать пользователей");
        System.out.println("5. Сохранить в файл");
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

    private static void fillUsers(DataFiller filler) {

        System.out.print("Введите количество пользователей: ");

        int size = readInt();
        scanner.nextLine();

        users = filler.fill(size);

        System.out.println("Данные успешно заполнены.");
        printUsers();
    }

    private static void sortUsers() {

        if (users == null || users.isEmpty()) {
            System.out.println("Список пуст.");
            return;
        }

        System.out.println("Выберите алгоритм сортировки:");
        System.out.println("1. Bubble Sort");
        System.out.println("2. Insertion Sort");

        int sortChoice = readInt();

        SortStrategy strategy;

        switch (sortChoice) {
            case 1 -> strategy = new BubbleSort();
            case 2 -> strategy = new InsertionSort();
            default -> {
                System.out.println("Неверный выбор.");
                return;
            }
        }

        Comparator<User> comparator =
                Comparator.comparing(User::getName)
                        .thenComparing(User::getPassword)
                        .thenComparing(User::getMail);

        users = strategy.sort(users, comparator);

        System.out.println("Сортировка выполнена.");
        printUsers();
    }

    private static void saveUsers(FileWriterService writerService) {

        if (users == null || users.isEmpty()) {
            System.out.println("Нет данных для сохранения.");
            return;
        }

        writerService.writeUsers(users);

        System.out.println("Данные сохранены в файл.");
    }

    private static void printUsers() {

        if (users == null || users.isEmpty()) {
            System.out.println("Список пуст.");
            return;
        }

        System.out.println("\n--- USERS ---");

        for (User user : users) {
            System.out.println(user);
        }
    }
}