package model;

import service.validation.UserValidator;

/**
 * Класс-билдер для создания объектов User с валидацией полей.
 * Реализует паттерн Builder для безопасного создания объектов.
 */
public class UserBuilder {

    private String name;
    private String password;
    private String mail;

    /**
     * Устанавливает имя пользователя
     * @param name имя пользователя
     * @return текущий экземпляр билдера
     */
    public UserBuilder name(String name) {
        this.name = name;
        return this;
    }

    /**
     * Устанавливает пароль пользователя
     * @param password пароль
     * @return текущий экземпляр билдера
     */
    public UserBuilder password(String password) {
        this.password = password;
        return this;
    }

    /**
     * Устанавливает email пользователя
     * @param mail email адрес
     * @return текущий экземпляр билдера
     */
    public UserBuilder mail(String mail) {
        this.mail = mail;
        return this;
    }

    /**
     * Создаёт и валидирует объект User.
     * @throws IllegalArgumentException если валидация не пройдена
     * @return готовый объект User
     */
    public User build() {
        validateName();
        validatePassword();
        validateMail();

        return new User(name, password, mail);
    }

    /**
     * Валидация имени пользователя
     */
    private void validateName() {
        if (!UserValidator.isValidName(name)) {
            throw new IllegalArgumentException(
                    UserValidator.getNameErrorMessage(name)
            );
        }
    }

    /**
     * Валидация пароля
     */
    private void validatePassword() {
        if (!UserValidator.isValidPassword(password)) {
            throw new IllegalArgumentException(
                    UserValidator.getPasswordErrorMessage(password)
            );
        }
    }

    /**
     * Валидация email
     */
    private void validateMail() {
        if (!UserValidator.isValidMail(mail)) {
            throw new IllegalArgumentException(
                    UserValidator.getMailErrorMessage(mail)
            );
        }
    }
}