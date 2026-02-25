package model;

import Client.model.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit-тесты для класса User.
 */
@DisplayName("Тесты класса User")
class UserTest {

    private static final String VALID_NAME = "Иван Иванов";
    private static final String VALID_PASSWORD = "secure123";
    private static final String VALID_MAIL = "ivan@example.com";

    @Test
    @DisplayName("Успешное создание пользователя через Builder")
    void testCreateUserSuccessfully() {
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
    @DisplayName("Геттеры возвращают корректные значения")
    void testGettersReturnCorrectValues() {
        User user = User.builder()
                .name("Петр Петров")
                .password("password456")
                .mail("petr@test.com")
                .build();

        assertEquals("Петр Петров", user.getName());
        assertEquals("password456", user.getPassword());
        assertEquals("petr@test.com", user.getMail());
    }

    @Test
    @DisplayName("toString возвращает корректную строку")
    void testToStringReturnsCorrectString() {
        User user = User.builder()
                .name("Анна")
                .password("pass789")
                .mail("anna@test.com")
                .build();

        String expected = "User{name='Анна', password='pass789', mail='anna@test.com'}";
        assertEquals(expected, user.toString());
    }

    @Test
    @DisplayName("equals возвращает true для одинаковых пользователей")
    void testEqualsReturnsTrueForSameUsers() {
        User user1 = User.builder()
                .name("Сидор")
                .password("sidor123")
                .mail("sidor@test.com")
                .build();

        User user2 = User.builder()
                .name("Сидор")
                .password("sidor123")
                .mail("sidor@test.com")
                .build();

        assertEquals(user1, user2);
    }

    @Test
    @DisplayName("equals возвращает false для разных пользователей")
    void testEqualsReturnsFalseForDifferentUsers() {
        User user1 = User.builder()
                .name("Сидор")
                .password("sidor123")
                .mail("sidor@test.com")
                .build();

        User user2 = User.builder()
                .name("Анна")
                .password("anna123")
                .mail("anna@test.com")
                .build();

        assertNotEquals(user1, user2);
    }

    @Test
    @DisplayName("equals возвращает false при сравнении с null")
    void testEqualsReturnsFalseWhenComparedWithNull() {
        User user = User.builder()
                .name("Тест")
                .password("test123")
                .mail("test@test.com")
                .build();

        assertNotEquals(user, null);
    }

    @Test
    @DisplayName("equals возвращает false при сравнении с объектом другого класса")
    void testEqualsReturnsFalseWhenComparedWithDifferentClass() {
        User user = User.builder()
                .name("Тест")
                .password("test123")
                .mail("test@test.com")
                .build();

        assertNotEquals(user, "строка");
        assertNotEquals(user, 123);
    }

    @Test
    @DisplayName("hashCode возвращает одинаковое значение для одинаковых пользователей")
    void testHashCodeReturnsSameValueForSameUsers() {
        User user1 = User.builder()
                .name("Ольга")
                .password("olga123")
                .mail("olga@test.com")
                .build();

        User user2 = User.builder()
                .name("Ольга")
                .password("olga123")
                .mail("olga@test.com")
                .build();

        assertEquals(user1.hashCode(), user2.hashCode());
    }

    @Test
    @DisplayName("hashCode возвращает разное значение для разных пользователей")
    void testHashCodeReturnsDifferentValueForDifferentUsers() {
        User user1 = User.builder()
                .name("Ольга")
                .password("olga123")
                .mail("olga@test.com")
                .build();

        User user2 = User.builder()
                .name("Мария")
                .password("maria123")
                .mail("maria@test.com")
                .build();

        assertNotEquals(user1.hashCode(), user2.hashCode());
    }

    @Test
    @DisplayName("equals возвращает true при сравнении объекта с самим собой")
    void testEqualsReturnsTrueWhenComparedWithItself() {
        User user = User.builder()
                .name("Тест")
                .password("test123")
                .mail("test@test.com")
                .build();

        assertEquals(user, user);
    }

    @ParameterizedTest
    @DisplayName("equals возвращает false при различии хотя бы в одном поле")
    @MethodSource("provideDifferentUsers")
    void testEqualsReturnsFalseWhenFieldsDiffer(User user1, User user2) {
        assertNotEquals(user1, user2);
    }

    private static Stream<Object[]> provideDifferentUsers() {
        User baseUser = User.builder()
                .name("Базовый")
                .password("base123")
                .mail("base@test.com")
                .build();

        return Stream.of(
                new Object[]{
                        baseUser,
                        User.builder().name("Другой").password("base123").mail("base@test.com").build()
                },
                new Object[]{
                        baseUser,
                        User.builder().name("Базовый").password("other123").mail("base@test.com").build()
                },
                new Object[]{
                        baseUser,
                        User.builder().name("Базовый").password("base123").mail("other@test.com").build()
                }
        );
    }
}