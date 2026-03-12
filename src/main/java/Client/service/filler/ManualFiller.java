package Client.service.filler;

import Client.model.User;
import Client.service.filler.temp.TempValidator;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ManualFiller implements DataFiller {
    private final Scanner scanner;

    public ManualFiller(Scanner scanner) {
        this.scanner = scanner;
    }

    @Override
    public List<User> fill(int count) {
        if (count <= 0) {
            throw new IllegalArgumentException("Количество пользователей должно быть положительным");
        }

        List<User> users = new ArrayList<>();
        System.out.println("Введите данные пользователей:");

        for (int i = 1; i <= count; i++) {
            System.out.printf("\n--- Пользователь %d ---%n", i);
            User user = readSingleUser();
            if (user != null) {
                users.add(user);
            } else {
                System.out.println("Ошибка создания пользователя. Пропускаем...");
                i--;
            }
        }
        return users;
    }

    private User readSingleUser() {
        while (true) {
            System.out.print("Имя: ");
            String name = scanner.nextLine().trim();

            System.out.print("Пароль: ");
            String password = scanner.nextLine();

            System.out.print("Email: ");
            String mail = scanner.nextLine().trim();

            // Валидация через TempValidator
            if (!TempValidator.isValidName(name)) {
                System.out.println("Ошибка: " + TempValidator.getNameErrorMessage(name));
                continue;
            }

            if (!TempValidator.isValidPassword(password)) {
                System.out.println("Ошибка: " + TempValidator.getPasswordErrorMessage(password));
                continue;
            }

            if (!TempValidator.isValidMail(mail)) {
                System.out.println("Ошибка: " + TempValidator.getMailErrorMessage(mail));
                continue;
            }

            try {
                return User.builder()
                        .name(name)
                        .password(password)
                        .mail(mail)
                        .build();
            } catch (IllegalArgumentException e) {
                System.out.println("Ошибка создания пользователя: " + e.getMessage());
                System.out.println("Повторите ввод.");
            }
        }
    }
}