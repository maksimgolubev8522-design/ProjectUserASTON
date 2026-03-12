package Client.service.filler;

import Client.model.User;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ManualFiller implements Filler {

    private final Scanner scanner;

    public ManualFiller() {
        this.scanner = new Scanner(System.in);
    }

    @Override
    public List<User> fill(int count) {
        if (count <= 0) {
            System.out.println("Количество пользователей должно быть положительным");
            return new ArrayList<>();
        }

        List<User> users = new ArrayList<>();
        System.out.println("\n" + getDescription());
        System.out.println("Всего нужно ввести: " + count + " пользователей");
        System.out.println("Правила валидации:");
        System.out.println("  - Имя: от 2 до 50 символов, не пустое");
        System.out.println("  - Пароль: от 6 до 100 символов, не пустой");
        System.out.println("  - Email: корректный формат (пример: user@domain.com)");

        int added = 0;
        while (added < count) {
            System.out.println("\n--- Пользователь " + (added + 1) + " ---");

            System.out.print("Введите имя: ");
            String name = scanner.nextLine().trim();

            System.out.print("Введите пароль: ");
            String password = scanner.nextLine().trim();

            System.out.print("Введите email: ");
            String mail = scanner.nextLine().trim();

            try {
                User user = User.builder()
                        .name(name)
                        .password(password)
                        .mail(mail)
                        .build();

                users.add(user);
                added++;
                System.out.println("✓ Пользователь успешно добавлен");

            } catch (IllegalArgumentException e) {
                System.out.println("✗ Ошибка: " + e.getMessage());
                System.out.println("Повторите ввод для этого пользователя");
            }
        }

        System.out.println("\n✓ Ручной ввод завершен. Добавлено пользователей: " + users.size());
        return users;
    }

    @Override
    public String getDescription() {
        return "=== Ручной ввод пользователей ===";
    }
}