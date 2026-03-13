package Client;

import Client.collection.CustomUserCollection;
import Client.model.User;
import Client.service.file.FileSaverService;
import Client.service.filler.*;
import Client.service.thread.UserCounterService;
import Client.service.stream.UserStreamProcessor;
import Client.strategy.*;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.function.ToIntFunction;

public class Main {

    private static final Scanner scanner = new Scanner(System.in);
    private static CustomUserCollection userCollection = new CustomUserCollection();
    private static SortStrategy currentStrategy = new InsertionSort();
    private static final FileSaverService fileSaver = new FileSaverService();
    private static final UserCounterService counterService = new UserCounterService();
    private static final UserStreamProcessor streamProcessor = new UserStreamProcessor();

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
                case 6 -> sortUsersEvenOdd();
                case 7 -> saveUsers();
                case 8 -> countUserOccurrences();
                case 9 -> demonstrateStreamOperations();
                case 0 -> {
                    System.out.println(" Выход из программы...");
                    return;
                }
                default -> System.out.println(" Неверный пункт меню.");
            }
        }
    }

    private static void printMenu() {
        System.out.println("\n🔷🔷🔷🔷🔷🔷🔷🔷🔷🔷🔷🔷🔷🔷🔷🔷🔷");
        System.out.println("            ГЛАВНОЕ МЕНЮ");
        System.out.println("🔷🔷🔷🔷🔷🔷🔷🔷🔷🔷🔷🔷🔷🔷🔷🔷🔷");
        System.out.println("1.  Ввести пользователей вручную");
        System.out.println("2.  Сгенерировать случайных пользователей");
        System.out.println("3.  Загрузить пользователей из файла (Stream)");
        System.out.println("4.  Выбрать стратегию сортировки");
        System.out.println("5.  Обычная сортировка");
        System.out.println("6.  Сортировка с условием (чет/нечет)");
        System.out.println("7.  Сохранить в файл");
        System.out.println("8.  Подсчитать вхождения пользователя (многопоточно)");
        System.out.println("9.  Демонстрация Stream операций");
        System.out.println("0.  Выход");
        System.out.print(" Выберите пункт: ");
    }

    private static int readInt() {
        while (!scanner.hasNextInt()) {
            System.out.println(" Введите число.");
            scanner.next();
        }
        return scanner.nextInt();
    }

    private static void fillUsers(Filler filler) {
        List<User> newUsers = filler.fill(0);

        if (newUsers != null && !newUsers.isEmpty()) {
            userCollection = CustomUserCollection.fromStream(newUsers.stream());
            System.out.println(" Данные успешно добавлены через Stream. Всего пользователей: " + userCollection.size());
            printUsers();
        } else {
            System.out.println(" Не удалось добавить пользователей.");
        }
    }

    private static void chooseSortStrategy() {
        System.out.println("\n Выберите алгоритм сортировки:");
        System.out.println("1. Bubble Sort");
        System.out.println("2. Insertion Sort");
        System.out.println("3. Even-Odd Sort");
        System.out.print(" Ваш выбор: ");

        int choice = readInt();
        scanner.nextLine();

        String strategyName;
        switch (choice) {
            case 1 -> {
                currentStrategy = new BubbleSort();
                strategyName = "Bubble Sort";
            }
            case 2 -> {
                currentStrategy = new InsertionSort();
                strategyName = "Insertion Sort";
            }
            case 3 -> {
                currentStrategy = new EvenOddSort();
                strategyName = "Even-Odd Sort";
            }
            default -> {
                System.out.println(" Неверный выбор. Оставлена текущая стратегия.");
                return;
            }
        }
        System.out.println(" Выбрана стратегия: " + strategyName);
    }

    private static void sortUsers() {
        if (userCollection.isEmpty()) {
            System.out.println(" Список пользователей пуст.");
            return;
        }

        Comparator<User> comparator = Comparator
                .comparing(User::getName)
                .thenComparing(User::getPassword)
                .thenComparing(User::getMail);

        List<User> sorted = currentStrategy.sort(
                userCollection.toList(),
                comparator
        );

        userCollection = CustomUserCollection.fromStream(sorted.stream());
        System.out.println(" Сортировка выполнена. Стратегия: " + currentStrategy.getClass().getSimpleName());
        printUsers();
    }

    private static void sortUsersEvenOdd() {
        if (userCollection.isEmpty()) {
            System.out.println(" Список пользователей пуст.");
            return;
        }

        System.out.println("\n Выберите числовое поле для условия чет/нечет:");
        System.out.println("1. Длина имени");
        System.out.println("2. Длина пароля");
        System.out.println("3. Длина email");
        System.out.print(" Ваш выбор: ");

        int fieldChoice = readInt();
        scanner.nextLine();

        ToIntFunction<User> fieldExtractor = switch (fieldChoice) {
            case 1 -> user -> user.getName().length();
            case 2 -> user -> user.getPassword().length();
            case 3 -> user -> user.getMail().length();
            default -> {
                System.out.println(" Неверный выбор, используется длина имени");
                yield user -> user.getName().length();
            }
        };

        Comparator<User> comparator = Comparator
                .comparing(User::getName)
                .thenComparing(User::getPassword)
                .thenComparing(User::getMail);

        List<User> sorted;

        if (currentStrategy instanceof BubbleSort) {
            sorted = ((BubbleSort) currentStrategy).sortWithEvenOdd(
                    userCollection.toList(),
                    comparator,
                    fieldExtractor
            );
        } else if (currentStrategy instanceof InsertionSort) {
            sorted = ((InsertionSort) currentStrategy).sortWithEvenOdd(
                    userCollection.toList(),
                    comparator,
                    fieldExtractor
            );
        } else if (currentStrategy instanceof EvenOddSort) {
            sorted = ((EvenOddSort) currentStrategy).sortWithEvenOdd(
                    userCollection.toList(),
                    comparator,
                    fieldExtractor
            );
        } else {
            sorted = currentStrategy.sortWithEvenOddCondition(
                    userCollection.toList(),
                    comparator,
                    fieldExtractor
            );
        }

        userCollection = CustomUserCollection.fromStream(sorted.stream());
        System.out.println(" Сортировка с условием чет/нечет выполнена.");
        printUsers();
    }

    private static void saveUsers() {
        if (userCollection.isEmpty()) {
            System.out.println(" Нет данных для сохранения.");
            return;
        }

        System.out.print(" Введите путь для сохранения файла: ");
        String filePath = scanner.nextLine().trim();

        System.out.println("\n📝 Выберите режим записи:");
        System.out.println("1. Переписать файл");
        System.out.println("2. Дописать в конец");
        System.out.println("3. Дописать с временной меткой");
        System.out.print(" Ваш выбор: ");

        try {
            int mode = Integer.parseInt(scanner.nextLine());
            FileSaverService.SaveMode saveMode = switch (mode) {
                case 1 -> FileSaverService.SaveMode.OVERWRITE;
                case 2 -> FileSaverService.SaveMode.APPEND;
                case 3 -> FileSaverService.SaveMode.APPEND_WITH_TIMESTAMP;
                default -> {
                    System.out.println(" Неверный выбор, используется OVERWRITE");
                    yield FileSaverService.SaveMode.OVERWRITE;
                }
            };

            fileSaver.saveToFile(userCollection.toList(), filePath, saveMode);
            System.out.println(" Данные сохранены в файл: " + filePath);

        } catch (Exception e) {
            System.out.println(" Ошибка при сохранении: " + e.getMessage());
        }
    }

    private static void countUserOccurrences() {
        if (userCollection.isEmpty()) {
            System.out.println(" Список пользователей пуст.");
            return;
        }

        System.out.println("\n Введите данные пользователя для поиска:");

        System.out.print("Имя: ");
        String name = scanner.nextLine().trim();

        System.out.print("Пароль: ");
        String password = scanner.nextLine().trim();

        System.out.print("Email: ");
        String mail = scanner.nextLine().trim();

        try {
            User target = User.builder()
                    .name(name)
                    .password(password)
                    .mail(mail)
                    .build();

            System.out.println("\n⚙️ Выберите метод подсчета:");
            System.out.println("1. Многопоточный (по задачам)");
            System.out.println("2. Parallel Stream");
            System.out.print(" Ваш выбор: ");

            int method = readInt();
            scanner.nextLine();

            long startTime = System.currentTimeMillis();
            int count;
            String methodName;

            if (method == 1) {
                count = counterService.countOccurrencesParallel(userCollection, target);
                methodName = "многопоточный (задачи)";
            } else {
                count = counterService.countByPredicateParallel(
                        userCollection,
                        user -> user.equals(target)
                );
                methodName = "Parallel Stream";
            }

            long endTime = System.currentTimeMillis();

            System.out.println("\n РЕЗУЛЬТАТ:");
            System.out.println("   Пользователь: " + target.getName() + " <" + target.getMail() + ">");
            System.out.println("   Метод: " + methodName);
            System.out.println("   Найден: " + count + " раз(а)");
            System.out.println("   Время выполнения: " + (endTime - startTime) + " мс");

        } catch (IllegalArgumentException e) {
            System.out.println(" Ошибка при создании пользователя: " + e.getMessage());
        }
    }

    private static void demonstrateStreamOperations() {
        if (userCollection.isEmpty()) {
            System.out.println(" Список пользователей пуст. Сначала добавьте пользователей.");
            return;
        }

        System.out.println("\n ДЕМОНСТРАЦИЯ STREAM ОПЕРАЦИЙ");
        System.out.println("─────────────────────────────");

        System.out.println("\n1⃣ Фильтрация (пароль длиннее 8 символов):");
        CustomUserCollection filtered = streamProcessor.filterByPasswordLength(userCollection, 8);
        System.out.println("   Найдено: " + filtered.size() + " пользователей");

        System.out.println("\n Группировка по доменам email:");
        Map<String, List<User>> byDomain = streamProcessor.groupByEmailDomain(userCollection);
        byDomain.forEach((domain, users) ->
                System.out.println("   " + domain + ": " + users.size() + " пользователей"));

        System.out.println("\n Статистика по длине имени:");
        Map<String, Double> stats = streamProcessor.getNameLengthStatistics(userCollection);
        stats.forEach((key, value) ->
                System.out.println("   " + key + ": " + String.format("%.2f", value)));

        System.out.println("\n Уникальные имена:");
        CustomUserCollection unique = streamProcessor.getUniqueNames(userCollection);
        System.out.println("   Всего уникальных имен: " + unique.size());
        unique.stream().map(User::getName).distinct().limit(5).forEach(name ->
                System.out.println("   • " + name));
        if (unique.size() > 5) System.out.println("   ... и еще " + (unique.size() - 5));
    }

    private static void printUsers() {
        if (userCollection.isEmpty()) {
            System.out.println(" Список пользователей пуст.");
            return;
        }

        System.out.println("\n ПОЛЬЗОВАТЕЛИ (" + userCollection.size() + ")");
        System.out.println("─────────────────────────────");
        int i = 1;
        for (User user : userCollection) {
            System.out.printf("%2d. %-15s | %-20s | %s%n",
                    i++,
                    user.getName(),
                    user.getMail(),
                    "***".repeat(Math.min(3, user.getPassword().length()/3)));
        }
    }
}