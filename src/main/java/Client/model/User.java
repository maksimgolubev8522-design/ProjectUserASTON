package Client.model;

import java.util.Objects;

/**
 * Класс пользователя с полями name, password, mail.
 * Класс является неизменяемым для безопасности данных
 */
public final class User {

    private final String name;
    private final String password;
    private final String mail;

    /**
     * Приватный конструктор — создание только через Builder
     */
    User(String name, String password, String mail) {
        this.name = name;
        this.password = password;
        this.mail = mail;
    }

    // Геттеры
    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public String getMail() {
        return mail;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(name, user.name) &&
                Objects.equals(password, user.password) &&
                Objects.equals(mail, user.mail);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, password, mail);
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", password='" + password + '\'' +
                ", mail='" + mail + '\'' +
                '}';
    }

    /**
     * Статический метод для создания Builder
     */
    public static UserBuilder builder() {
        return new UserBuilder();
    }
}