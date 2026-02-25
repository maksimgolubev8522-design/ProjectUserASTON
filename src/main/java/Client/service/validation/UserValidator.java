package Client.service.validation;

import java.util.regex.Pattern;

/**
 * Класс для валидации данных пользователя.
 * Используется для проверки существующих данных и валидации в Builder.
 */
public class UserValidator {

    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");

    private static final int MIN_PASSWORD_LENGTH = 6;
    private static final int MAX_PASSWORD_LENGTH = 100;
    private static final int MIN_NAME_LENGTH = 2;
    private static final int MAX_NAME_LENGTH = 50;
    private static final int MAX_MAIL_LENGTH = 100;

    /**
     * Проверяет валидность имени пользователя
     * @param name имя для проверки
     * @return true если валидно
     */
    public static boolean isValidName(String name) {
        return name != null &&
                !name.trim().isEmpty() &&
                name.length() >= MIN_NAME_LENGTH &&
                name.length() <= MAX_NAME_LENGTH;
    }

    /**
     * Проверяет валидность пароля
     * @param password пароль для проверки
     * @return true если валиден
     */
    public static boolean isValidPassword(String password) {
        return password != null &&
                !password.trim().isEmpty() &&
                password.length() >= MIN_PASSWORD_LENGTH &&
                password.length() <= MAX_PASSWORD_LENGTH;
    }

    /**
     * Проверяет валидность email
     * @param mail email для проверки
     * @return true если валиден
     */
    public static boolean isValidMail(String mail) {
        return mail != null &&
                !mail.trim().isEmpty() &&
                EMAIL_PATTERN.matcher(mail).matches() &&
                mail.length() <= MAX_MAIL_LENGTH;
    }

    /**
     * Полная валидация всех полей пользователя
     * @param name имя
     * @param password пароль
     * @param mail email
     * @return true если все поля валидны
     */
    public static boolean isValidUser(String name, String password, String mail) {
        return isValidName(name) && isValidPassword(password) && isValidMail(mail);
    }

    // ========== Методы для получения сообщений об ошибках ==========

    /**
     * Возвращает сообщение об ошибке для имени
     */
    public static String getNameErrorMessage(String name) {
        if (name == null || name.trim().isEmpty()) {
            return "Имя пользователя не может быть пустым";
        }
        if (name.length() < MIN_NAME_LENGTH) {
            return String.format("Имя пользователя должно содержать минимум %d символа", MIN_NAME_LENGTH);
        }
        if (name.length() > MAX_NAME_LENGTH) {
            return String.format("Имя пользователя не должно превышать %d символов", MAX_NAME_LENGTH);
        }
        return "Некорректное имя пользователя";
    }

    /**
     * Возвращает сообщение об ошибке для пароля
     */
    public static String getPasswordErrorMessage(String password) {
        if (password == null || password.trim().isEmpty()) {
            return "Пароль не может быть пустым";
        }
        if (password.length() < MIN_PASSWORD_LENGTH) {
            return String.format("Пароль должен содержать минимум %d символов", MIN_PASSWORD_LENGTH);
        }
        if (password.length() > MAX_PASSWORD_LENGTH) {
            return String.format("Пароль не должен превышать %d символов", MAX_PASSWORD_LENGTH);
        }
        return "Некорректный пароль";
    }

    /**
     * Возвращает сообщение об ошибке для email
     */
    public static String getMailErrorMessage(String mail) {
        if (mail == null || mail.trim().isEmpty()) {
            return "Email не может быть пустым";
        }
        if (!EMAIL_PATTERN.matcher(mail).matches()) {
            return "Email имеет некорректный формат: " + mail;
        }
        if (mail.length() > MAX_MAIL_LENGTH) {
            return String.format("Email не должен превышать %d символов", MAX_MAIL_LENGTH);
        }
        return "Некорректный email";
    }
}