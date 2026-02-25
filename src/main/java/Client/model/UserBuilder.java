package Client.model;

import Client.service.validation.UserValidator;

public class UserBuilder {
    private String name;
    private String password;
    private String mail;

    public UserBuilder name(String name) { this.name = name; return this; }
    public UserBuilder password(String password) { this.password = password; return this; }
    public UserBuilder mail(String mail) { this.mail = mail; return this; }

    public User build() {
        validateName();
        validatePassword();
        validateMail();
        return new User(name, password, mail);
    }

    private void validateName() {
        if (!UserValidator.isValidName(name)) {
            throw new IllegalArgumentException(UserValidator.getNameErrorMessage(name));
        }
    }

    private void validatePassword() {
        if (!UserValidator.isValidPassword(password)) {
            throw new IllegalArgumentException(UserValidator.getPasswordErrorMessage(password));
        }
    }

    private void validateMail() {
        if (!UserValidator.isValidMail(mail)) {
            throw new IllegalArgumentException(UserValidator.getMailErrorMessage(mail));
        }
    }
}