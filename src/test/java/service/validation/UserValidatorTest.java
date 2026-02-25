package service.validation;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit-тесты для класса UserValidator.
 */
@DisplayName("Тесты класса UserValidator")
class UserValidatorTest {

    // ========== Тесты валидации имени ==========

    @Test
    @DisplayName("isValidName возвращает true для валидного имени")
    void testIsValidNameReturnsTrueForValidName() {
        assertTrue(UserValidator.isValidName("Иван Иванов"));
        assertTrue(UserValidator.isValidName("АБ"));
        assertTrue(UserValidator.isValidName("А".repeat(50)));
    }

    @ParameterizedTest
    @DisplayName("isValidName возвращает false для null имени")
    @NullSource
    void testIsValidNameReturnsFalseForNullName(String name) {
        assertFalse(UserValidator.isValidName(name));
    }

    @ParameterizedTest
    @DisplayName("isValidName возвращает false для пустого имени")
    @EmptySource
    void testIsValidNameReturnsFalseForEmptyName(String name) {
        assertFalse(UserValidator.isValidName(name));
    }

    @Test
    @DisplayName("isValidName возвращает false для имени из пробелов")
    void testIsValidNameReturnsFalseForWhitespaceName() {
        assertFalse(UserValidator.isValidName("   "));
        assertFalse(UserValidator.isValidName("\t"));
        assertFalse(UserValidator.isValidName("\n"));
    }

    @ParameterizedTest
    @DisplayName("isValidName возвращает false для слишком короткого имени")
    @ValueSource(strings = {"А", "Б", "В"})
    void testIsValidNameReturnsFalseForTooShortName(String name) {
        assertFalse(UserValidator.isValidName(name));
    }

    @Test
    @DisplayName("isValidName возвращает false для слишком длинного имени")
    void testIsValidNameReturnsFalseForTooLongName() {
        assertFalse(UserValidator.isValidName("А".repeat(51)));
        assertFalse(UserValidator.isValidName("А".repeat(100)));
    }

    // ========== Тесты сообщений об ошибках для имени ==========

    @Test
    @DisplayName("getNameErrorMessage возвращает корректное сообщение для null имени")
    void testGetNameErrorMessageForNullName() {
        String message = UserValidator.getNameErrorMessage(null);
        assertEquals("Имя пользователя не может быть пустым", message);
    }

    @Test
    @DisplayName("getNameErrorMessage возвращает корректное сообщение для пустого имени")
    void testGetNameErrorMessageForEmptyName() {
        String message = UserValidator.getNameErrorMessage("");
        assertEquals("Имя пользователя не может быть пустым", message);
    }

    @Test
    @DisplayName("getNameErrorMessage возвращает корректное сообщение для слишком короткого имени")
    void testGetNameErrorMessageForTooShortName() {
        String message = UserValidator.getNameErrorMessage("А");
        assertEquals("Имя пользователя должно содержать минимум 2 символа", message);
    }

    @Test
    @DisplayName("getNameErrorMessage возвращает корректное сообщение для слишком длинного имени")
    void testGetNameErrorMessageForTooLongName() {
        String message = UserValidator.getNameErrorMessage("А".repeat(51));
        assertEquals("Имя пользователя не должно превышать 50 символов", message);
    }

    // ========== Тесты валидации пароля ==========

    @Test
    @DisplayName("isValidPassword возвращает true для валидного пароля")
    void testIsValidPasswordReturnsTrueForValidPassword() {
        assertTrue(UserValidator.isValidPassword("123456"));
        assertTrue(UserValidator.isValidPassword("password"));
        assertTrue(UserValidator.isValidPassword("1".repeat(100)));
    }

    @ParameterizedTest
    @DisplayName("isValidPassword возвращает false для null пароля")
    @NullSource
    void testIsValidPasswordReturnsFalseForNullPassword(String password) {
        assertFalse(UserValidator.isValidPassword(password));
    }

    @ParameterizedTest
    @DisplayName("isValidPassword возвращает false для пустого пароля")
    @EmptySource
    void testIsValidPasswordReturnsFalseForEmptyPassword(String password) {
        assertFalse(UserValidator.isValidPassword(password));
    }

    @Test
    @DisplayName("isValidPassword возвращает false для пароля из пробелов")
    void testIsValidPasswordReturnsFalseForWhitespacePassword() {
        assertFalse(UserValidator.isValidPassword("   "));
        assertFalse(UserValidator.isValidPassword("\t"));
    }

    @ParameterizedTest
    @DisplayName("isValidPassword возвращает false для слишком короткого пароля")
    @ValueSource(strings = {"12345", "abcde", "1", "a"})
    void testIsValidPasswordReturnsFalseForTooShortPassword(String password) {
        assertFalse(UserValidator.isValidPassword(password));
    }

    @Test
    @DisplayName("isValidPassword возвращает false для слишком длинного пароля")
    void testIsValidPasswordReturnsFalseForTooLongPassword() {
        assertFalse(UserValidator.isValidPassword("1".repeat(101)));
        assertFalse(UserValidator.isValidPassword("1".repeat(200)));
    }

    // ========== Тесты сообщений об ошибках для пароля ==========

    @Test
    @DisplayName("getPasswordErrorMessage возвращает корректное сообщение для null пароля")
    void testGetPasswordErrorMessageForNullPassword() {
        String message = UserValidator.getPasswordErrorMessage(null);
        assertEquals("Пароль не может быть пустым", message);
    }

    @Test
    @DisplayName("getPasswordErrorMessage возвращает корректное сообщение для пустого пароля")
    void testGetPasswordErrorMessageForEmptyPassword() {
        String message = UserValidator.getPasswordErrorMessage("");
        assertEquals("Пароль не может быть пустым", message);
    }

    @Test
    @DisplayName("getPasswordErrorMessage возвращает корректное сообщение для слишком короткого пароля")
    void testGetPasswordErrorMessageForTooShortPassword() {
        String message = UserValidator.getPasswordErrorMessage("12345");
        assertEquals("Пароль должен содержать минимум 6 символов", message);
    }

    @Test
    @DisplayName("getPasswordErrorMessage возвращает корректное сообщение для слишком длинного пароля")
    void testGetPasswordErrorMessageForTooLongPassword() {
        String message = UserValidator.getPasswordErrorMessage("1".repeat(101));
        assertEquals("Пароль не должен превышать 100 символов", message);
    }

    // ========== Тесты валидации email ==========

    @Test
    @DisplayName("isValidMail возвращает true для валидного email")
    void testIsValidMailReturnsTrueForValidMail() {
        assertTrue(UserValidator.isValidMail("test@example.com"));
        assertTrue(UserValidator.isValidMail("test.user@example.com"));
        assertTrue(UserValidator.isValidMail("test+user@example.com"));
        assertTrue(UserValidator.isValidMail("a@b.cd"));
    }

    @ParameterizedTest
    @DisplayName("isValidMail возвращает false для null email")
    @NullSource
    void testIsValidMailReturnsFalseForNullMail(String mail) {
        assertFalse(UserValidator.isValidMail(mail));
    }

    @ParameterizedTest
    @DisplayName("isValidMail возвращает false для пустого email")
    @EmptySource
    void testIsValidMailReturnsFalseForEmptyMail(String mail) {
        assertFalse(UserValidator.isValidMail(mail));
    }

    @Test
    @DisplayName("isValidMail возвращает false для email из пробелов")
    void testIsValidMailReturnsFalseForWhitespaceMail() {
        assertFalse(UserValidator.isValidMail("   "));
        assertFalse(UserValidator.isValidMail("\t"));
    }

    @ParameterizedTest
    @DisplayName("isValidMail возвращает false для невалидного формата email")
    @CsvSource({
            "invalid-email",
            "invalid@",
            "@invalid.com",
            "invalid@com",
            "invalid..email@test.com",
            "invalid@email",
            "invalid@email.",
            "invalid@.com",
            "invalid@@test.com",
            "invalid@test..com"
    })
    void testIsValidMailReturnsFalseForInvalidMailFormat(String mail) {
        assertFalse(UserValidator.isValidMail(mail));
    }

    @Test
    @DisplayName("isValidMail возвращает false для слишком длинного email")
    void testIsValidMailReturnsFalseForTooLongMail() {
        String tooLongMail = "a".repeat(50) + "@" + "b".repeat(50) + ".com";
        assertFalse(UserValidator.isValidMail(tooLongMail));
    }

    // ========== Тесты сообщений об ошибках для email ==========

    @Test
    @DisplayName("getMailErrorMessage возвращает корректное сообщение для null email")
    void testGetMailErrorMessageForNullMail() {
        String message = UserValidator.getMailErrorMessage(null);
        assertEquals("Email не может быть пустым", message);
    }

    @Test
    @DisplayName("getMailErrorMessage возвращает корректное сообщение для пустого email")
    void testGetMailErrorMessageForEmptyMail() {
        String message = UserValidator.getMailErrorMessage("");
        assertEquals("Email не может быть пустым", message);
    }

    @Test
    @DisplayName("getMailErrorMessage возвращает корректное сообщение для невалидного формата email")
    void testGetMailErrorMessageForInvalidMailFormat() {
        String message = UserValidator.getMailErrorMessage("invalid-email");
        assertTrue(message.contains("Email имеет некорректный формат"));
        assertTrue(message.contains("invalid-email"));
    }

    @Test
    @DisplayName("getMailErrorMessage возвращает корректное сообщение для слишком длинного email")
    void testGetMailErrorMessageForTooLongMail() {
        String tooLongMail = "a".repeat(50) + "@" + "b".repeat(50) + ".com";
        String message = UserValidator.getMailErrorMessage(tooLongMail);
        assertEquals("Email не должен превышать 100 символов", message);
    }

    // ========== Тесты комплексной валидации ==========

    @Test
    @DisplayName("isValidUser возвращает true для валидных данных")
    void testIsValidUserReturnsTrueForValidData() {
        assertTrue(UserValidator.isValidUser("Иван", "password123", "ivan@test.com"));
    }

    @Test
    @DisplayName("isValidUser возвращает false при невалидном имени")
    void testIsValidUserReturnsFalseForInvalidName() {
        assertFalse(UserValidator.isValidUser("А", "password123", "ivan@test.com"));
    }

    @Test
    @DisplayName("isValidUser возвращает false при невалидном пароле")
    void testIsValidUserReturnsFalseForInvalidPassword() {
        assertFalse(UserValidator.isValidUser("Иван", "12345", "ivan@test.com"));
    }

    @Test
    @DisplayName("isValidUser возвращает false при невалидном email")
    void testIsValidUserReturnsFalseForInvalidMail() {
        assertFalse(UserValidator.isValidUser("Иван", "password123", "invalid-email"));
    }

    @Test
    @DisplayName("isValidUser возвращает false при всех невалидных полях")
    void testIsValidUserReturnsFalseForAllInvalidFields() {
        assertFalse(UserValidator.isValidUser(null, null, null));
        assertFalse(UserValidator.isValidUser("", "", ""));
        assertFalse(UserValidator.isValidUser("А", "1", "invalid"));
    }

    // ========== Тесты граничных случаев ==========

    @ParameterizedTest
    @DisplayName("isValidMail принимает различные валидные форматы email")
    @CsvSource({
            "test@example.com",
            "test.user@example.com",
            "test+user@example.com",
            "test_user@example.com",
            "test-user@example.com",
            "test@sub.example.com",
            "test123@example.com",
            "TEST@EXAMPLE.COM",
            "a@b.cd",
            "test.email+tag+sorting@example.com"
    })
    void testIsValidMailAcceptsVariousValidFormats(String mail) {
        assertTrue(UserValidator.isValidMail(mail));
    }

    @Test
    @DisplayName("Валидатор корректно обрабатывает минимально валидные данные")
    void testValidatorHandlesMinimalValidData() {
        assertTrue(UserValidator.isValidName("АБ"));
        assertTrue(UserValidator.isValidPassword("123456"));
        assertTrue(UserValidator.isValidMail("a@b.cd"));
        assertTrue(UserValidator.isValidUser("АБ", "123456", "a@b.cd"));
    }

    @Test
    @DisplayName("Валидатор корректно обрабатывает максимально валидные данные")
    void testValidatorHandlesMaximalValidData() {
        String maxName = "А".repeat(50);
        String maxPassword = "1".repeat(100);
        String maxMail = "a".repeat(40) + "@" + "b".repeat(40) + ".cd";

        assertTrue(UserValidator.isValidName(maxName));
        assertTrue(UserValidator.isValidPassword(maxPassword));
        assertTrue(UserValidator.isValidMail(maxMail));
        assertTrue(UserValidator.isValidUser(maxName, maxPassword, maxMail));
    }
}