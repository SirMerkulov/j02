package passwordvalidator;

import org.example.passwordvalidator.PasswordValidator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PasswordValidatorTest {
    private void testPassword(String password, String username, boolean result) {
        // Act
        boolean validationResult = PasswordValidator.isValidPassword(password, username);

        // Assert
        assertEquals(result, validationResult);
    }

    @ParameterizedTest
    @DisplayName("Valid password is accepted")
    @ValueSource(strings =
            {"Test6789", "1R2g3T4k5Y", "0123456789aA", "_TestUser74"})
    public void validPassword(String password) {
        testPassword(password, "TestUser", true);
    }

    @ParameterizedTest
    @DisplayName("Password is less 8 symbols")
    @ValueSource(strings =
            {"abcdefg", "_ _ _ ", "1234", "1", ""})
    public void tooShortPassword(String password) {
        testPassword(password, "TestUser", false);
    }

    @ParameterizedTest
    @DisplayName("Password has no digits")
    @ValueSource(strings =
            {"asdfghjkl", "__TgTg__", "AbCdEfGhIjKlMn"})
    public void noDigitsPassword(String password) {
        testPassword(password, "TestUser", false);
    }

    @ParameterizedTest
    @DisplayName("Password has no lower case letters")
    @ValueSource(strings =
            {"QWERTYU2", "01234ABCD", "P@$$W0RD"})
    public void noLowerCasePassword(String password) {
        testPassword(password, "TestUser", false);
    }

    @ParameterizedTest
    @DisplayName("Password has no upper case letters")
    @ValueSource(strings =
            {"qwerty1234", ")(*&56nm", "password5"})
    public void noUpperCasePassword(String password) {
        testPassword(password, "TestUser", false);
    }

    @Test
    @DisplayName("Password equals username")
    public void equalsUsernamePassword() {
        String username = "TestUser1";
        testPassword(username, username, false);
    }

    @ParameterizedTest
    @DisplayName("Password has spaces or quotes")
    @ValueSource(strings =
            {"qwerty 1234", "_&*^\tBg79", "\"34jdfgER", " \t\r\n\"Rg6*"})
    public void hasSpacesQuotesPassword(String password) {
        testPassword(password, "TestUser", false);
    }

    // new tests

    @Test
    @DisplayName("Test null password")
    public void testNullPassword() {
        PasswordValidator.ValidationResult result =
                PasswordValidator.validatePassword(null, "TestUser");
        assertFalse(result.isValid());
        assertTrue(result.getErrors().contains("Password must be at least 8 characters long"));
    }

    @Test
    @DisplayName("Test null username")
    public void testNullUsername() {
        PasswordValidator.ValidationResult result =
                PasswordValidator.validatePassword("ValidPass1", null);
        assertTrue(result.isValid());
        assertTrue(result.getErrors().isEmpty());
    }

    @Test
    @DisplayName("Test multiple errors in password")
    public void testMultipleErrors() {
        // Пароль "test" - короткий, нет цифр, нет заглавных букв
        PasswordValidator.ValidationResult result =
                PasswordValidator.validatePassword("test", "TestUser");

        assertFalse(result.isValid());
        List<String> errors = result.getErrors();

        // Проверяем, что есть несколько ошибок
        assertTrue(errors.size() >= 3);
        assertTrue(errors.contains("Password must be at least 8 characters long"));
        assertTrue(errors.contains("Password must contain at least one digit"));
        assertTrue(errors.contains("Password must contain at least one uppercase letter"));
    }

    @Test
    @DisplayName("Test password with all errors")
    public void testPasswordWithAllErrors() {
        // Короткий пароль без цифр, без заглавных букв, с пробелом, равный имени пользователя
        String password = "user ";
        String username = "user ";

        PasswordValidator.ValidationResult result =
                PasswordValidator.validatePassword(password, username);

        assertFalse(result.isValid());
        List<String> errors = result.getErrors();

        // Проверяем наличие ошибок (не обязательно все, так как проверка может остановиться на первом)
        assertFalse(errors.isEmpty());
    }

    @Test
    @DisplayName("Test ValidationResult toString method")
    public void testValidationResultToString() {
        // Валидный пароль
        PasswordValidator.ValidationResult validResult =
                PasswordValidator.validatePassword("ValidPass1", "User");
        assertTrue(validResult.toString().contains("Password is valid"));

        // Невалидный пароль
        PasswordValidator.ValidationResult invalidResult =
                PasswordValidator.validatePassword("short", "User");
        assertTrue(invalidResult.toString().contains("Password is invalid"));
        assertTrue(invalidResult.toString().contains("Errors:"));
    }

    @Test
    @DisplayName("Test password with single quotes")
    public void testPasswordWithSingleQuotes() {
        PasswordValidator.ValidationResult result =
                PasswordValidator.validatePassword("Pass'word1", "User");
        assertFalse(result.isValid());
        assertTrue(result.getErrors().contains("Password must not contain spaces, tabs, or quotes"));
    }

    @Test
    @DisplayName("Test backward compatibility")
    public void testBackwardCompatibility() {
        // Проверяем, что старый метод работает корректно
        assertTrue(PasswordValidator.isValidPassword("ValidPass1", "User"));
        assertFalse(PasswordValidator.isValidPassword("short", "User"));

        // Проверяем, что результаты обоих методов совпадают
        String[] testPasswords = {"ValidPass1", "short", "NODIGITS", "noupper1", "User1", "Pass word1"};

        for (String password : testPasswords) {
            boolean oldResult = PasswordValidator.isValidPassword(password, "User");
            boolean newResult = PasswordValidator.validatePassword(password, "User").isValid();
            assertEquals(oldResult, newResult,
                    "Results should match for password: " + password);
        }
    }
}