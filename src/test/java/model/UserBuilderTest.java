package model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit-тесты для класса UserBuilder.
 */
@DisplayName("Тесты класса UserBuilder")
class UserBuilderTest {

    private static final String VALID_NAME = "Иван Иванов";
    private static final String VALID_PASSWORD = "secure123";
    private static final String VALID_MAIL = "ivan@example.com";

    @Test
    @DisplayName("Builder создаёт корректный объект User")
    void testBuilderCreatesValidUser() {
        User user = User.builder()
                .name(VALID_NAME)
                .password(VALID_PASSWORD)
                .mail(VALID_MAIL)
                .build();

        assertNotNull(user);
        assertEquals(VALID_NAME, user.getName());
        assertEquals(VALID_PASSWORD, user.getPassword());
        assertEquals(VALID_MAIL, user.getMail());
    }

    @Test
    @DisplayName("Builder поддерживает fluent interface")
    void testBuilderSupportsFluentInterface() {
        UserBuilder builder = User.builder();

        assertSame(builder, builder.name(VALID_NAME));
        assertSame(builder, builder.password(VALID_PASSWORD));
        assertSame(builder, builder.mail(VALID_MAIL));
    }

    @Test
    @DisplayName("Builder создаёт пользователя с минимально валидными данными")
    void testBuilderCreatesUserWithMinimalValidData() {
        User user = User.builder()
                .name("АБ")  // Минимальная длина имени
                .password("123456")  // Минимальная длина пароля
                .mail("a@b.cd")  // Минимально валидный email
                .build();

        assertNotNull(user);
        assertEquals("АБ", user.getName());
        assertEquals("123456", user.getPassword());
        assertEquals("a@b.cd", user.getMail());
    }

    @Test
    @DisplayName("Builder создаёт пользователя с максимально валидными данными")
    void testBuilderCreatesUserWithMaximalValidData() {
        String maxName = "А".repeat(50);  // Максимальная длина имени
        String maxPassword = "1".repeat(100);  // Максимальная длина пароля
        String maxMail = "a".repeat(40) + "@" + "b".repeat(40) + ".cd";  // Максимально валидный email

        User user = User.builder()
                .name(maxName)
                .password(maxPassword)
                .mail(maxMail)
                .build();

        assertNotNull(user);
        assertEquals(maxName, user.getName());
        assertEquals(maxPassword, user.getPassword());
        assertEquals(maxMail, user.getMail());
    }

    // ========== Тесты валидации имени ==========

    @ParameterizedTest
    @DisplayName("Builder выбрасывает исключение при null имени")
    @NullSource
    void testBuilderThrowsExceptionWhenNameIsNull(String name) {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> User.builder()
                        .name(name)
                        .password(VALID_PASSWORD)
                        .mail(VALID_MAIL)
                        .build()
        );

        assertTrue(exception.getMessage().contains("Имя пользователя не может быть пустым"));
    }

    @ParameterizedTest
    @DisplayName("Builder выбрасывает исключение при пустом имени")
    @EmptySource
    void testBuilderThrowsExceptionWhenNameIsEmpty(String name) {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> User.builder()
                        .name(name)
                        .password(VALID_PASSWORD)
                        .mail(VALID_MAIL)
                        .build()
        );

        assertTrue(exception.getMessage().contains("Имя пользователя не может быть пустым"));
    }

    @Test
    @DisplayName("Builder выбрасывает исключение при имени из пробелов")
    void testBuilderThrowsExceptionWhenNameIsWhitespace() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> User.builder()
                        .name("   ")
                        .password(VALID_PASSWORD)
                        .mail(VALID_MAIL)
                        .build()
        );

        assertTrue(exception.getMessage().contains("Имя пользователя не может быть пустым"));
    }

    @ParameterizedTest
    @DisplayName("Builder выбрасывает исключение при слишком коротком имени")
    @ValueSource(strings = {"А", "Б"})
    void testBuilderThrowsExceptionWhenNameIsTooShort(String name) {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> User.builder()
                        .name(name)
                        .password(VALID_PASSWORD)
                        .mail(VALID_MAIL)
                        .build()
        );

        assertTrue(exception.getMessage().contains("минимум 2 символа"));
    }

    @Test
    @DisplayName("Builder выбрасывает исключение при слишком длинном имени")
    void testBuilderThrowsExceptionWhenNameIsTooLong() {
        String tooLongName = "А".repeat(51);

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> User.builder()
                        .name(tooLongName)
                        .password(VALID_PASSWORD)
                        .mail(VALID_MAIL)
                        .build()
        );

        assertTrue(exception.getMessage().contains("не должно превышать 50 символов"));
    }

    // ========== Тесты валидации пароля ==========

    @ParameterizedTest
    @DisplayName("Builder выбрасывает исключение при null пароле")
    @NullSource
    void testBuilderThrowsExceptionWhenPasswordIsNull(String password) {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> User.builder()
                        .name(VALID_NAME)
                        .password(password)
                        .mail(VALID_MAIL)
                        .build()
        );

        assertTrue(exception.getMessage().contains("Пароль не может быть пустым"));
    }

    @ParameterizedTest
    @DisplayName("Builder выбрасывает исключение при пустом пароле")
    @EmptySource
    void testBuilderThrowsExceptionWhenPasswordIsEmpty(String password) {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> User.builder()
                        .name(VALID_NAME)
                        .password(password)
                        .mail(VALID_MAIL)
                        .build()
        );

        assertTrue(exception.getMessage().contains("Пароль не может быть пустым"));
    }

    @Test
    @DisplayName("Builder выбрасывает исключение при пароле из пробелов")
    void testBuilderThrowsExceptionWhenPasswordIsWhitespace() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> User.builder()
                        .name(VALID_NAME)
                        .password("   ")
                        .mail(VALID_MAIL)
                        .build()
        );

        assertTrue(exception.getMessage().contains("Пароль не может быть пустым"));
    }

    @ParameterizedTest
    @DisplayName("Builder выбрасывает исключение при слишком коротком пароле")
    @ValueSource(strings = {"12345", "abcde", "1"})
    void testBuilderThrowsExceptionWhenPasswordIsTooShort(String password) {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> User.builder()
                        .name(VALID_NAME)
                        .password(password)
                        .mail(VALID_MAIL)
                        .build()
        );

        assertTrue(exception.getMessage().contains("минимум 6 символов"));
    }

    @Test
    @DisplayName("Builder выбрасывает исключение при слишком длинном пароле")
    void testBuilderThrowsExceptionWhenPasswordIsTooLong() {
        String tooLongPassword = "1".repeat(101);

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> User.builder()
                        .name(VALID_NAME)
                        .password(tooLongPassword)
                        .mail(VALID_MAIL)
                        .build()
        );

        assertTrue(exception.getMessage().contains("не должен превышать 100 символов"));
    }

    // ========== Тесты валидации email ==========

    @ParameterizedTest
    @DisplayName("Builder выбрасывает исключение при null email")
    @NullSource
    void testBuilderThrowsExceptionWhenMailIsNull(String mail) {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> User.builder()
                        .name(VALID_NAME)
                        .password(VALID_PASSWORD)
                        .mail(mail)
                        .build()
        );

        assertTrue(exception.getMessage().contains("Email не может быть пустым"));
    }

    @ParameterizedTest
    @DisplayName("Builder выбрасывает исключение при пустом email")
    @EmptySource
    void testBuilderThrowsExceptionWhenMailIsEmpty(String mail) {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> User.builder()
                        .name(VALID_NAME)
                        .password(VALID_PASSWORD)
                        .mail(mail)
                        .build()
        );

        assertTrue(exception.getMessage().contains("Email не может быть пустым"));
    }

    @Test
    @DisplayName("Builder выбрасывает исключение при email из пробелов")
    void testBuilderThrowsExceptionWhenMailIsWhitespace() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> User.builder()
                        .name(VALID_NAME)
                        .password(VALID_PASSWORD)
                        .mail("   ")
                        .build()
        );

        assertTrue(exception.getMessage().contains("Email не может быть пустым"));
    }

    @ParameterizedTest
    @DisplayName("Builder выбрасывает исключение при невалидном формате email")
    @CsvSource({
            "invalid-email",
            "invalid@",
            "@invalid.com",
            "invalid@com",
            "invalid..email@test.com",
            "invalid@email",
            "invalid@email.",
            "invalid@.com"
    })
    void testBuilderThrowsExceptionWhenMailHasInvalidFormat(String mail) {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> User.builder()
                        .name(VALID_NAME)
                        .password(VALID_PASSWORD)
                        .mail(mail)
                        .build()
        );

        assertTrue(exception.getMessage().contains("Email имеет некорректный формат"));
    }

    @Test
    @DisplayName("Builder выбрасывает исключение при слишком длинном email")
    void testBuilderThrowsExceptionWhenMailIsTooLong() {
        String tooLongMail = "a".repeat(50) + "@" + "b".repeat(50) + ".com";  // > 100 символов

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> User.builder()
                        .name(VALID_NAME)
                        .password(VALID_PASSWORD)
                        .mail(tooLongMail)
                        .build()
        );

        assertTrue(exception.getMessage().contains("не должен превышать 100 символов"));
    }

    // ========== Тесты граничных случаев ==========

    @ParameterizedTest
    @DisplayName("Builder принимает валидные email с различными форматами")
    @CsvSource({
            "test@example.com",
            "test.user@example.com",
            "test+user@example.com",
            "test_user@example.com",
            "test-user@example.com",
            "test@sub.example.com",
            "test123@example.com",
            "TEST@EXAMPLE.COM"
    })
    void testBuilderAcceptsValidEmailFormats(String mail) {
        User user = User.builder()
                .name(VALID_NAME)
                .password(VALID_PASSWORD)
                .mail(mail)
                .build();

        assertEquals(mail, user.getMail());
    }

    @Test
    @DisplayName("Builder может быть переиспользован для создания нескольких пользователей")
    void testBuilderCanBeReused() {
        UserBuilder builder = User.builder();

        User user1 = builder
                .name("Пользователь 1")
                .password("pass123")
                .mail("user1@test.com")
                .build();

        User user2 = builder
                .name("Пользователь 2")
                .password("pass456")
                .mail("user2@test.com")
                .build();

        assertNotEquals(user1, user2);
        assertEquals("Пользователь 1", user1.getName());
        assertEquals("Пользователь 2", user2.getName());
    }
}