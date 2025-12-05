package org.example.passwordvalidator;
import java.util.ArrayList;
import java.util.List;

/**
 * Проверка пароля на сложность.
 * Пароль должен отвечать следующим требованиям:
 * - не менее 8 символов в длину
 * - содержит строчные, заглавные буквы и цифры
 * - не должен совпадать с именем пользователя
 * - не должен содержать пробельных символов, табуляции и кавычек
 */
public class PasswordValidator {

    /**
     * Класс для хранения результата валидации пароля
     */
    public static class ValidationResult {
        private final boolean isValid;
        private final List<String> errors;

        public ValidationResult(boolean isValid, List<String> errors) {
            this.isValid = isValid;
            this.errors = errors;
        }

        public boolean isValid() {
            return isValid;
        }

        public List<String> getErrors() {
            return new ArrayList<>(errors); // Возвращаем копию для безопасности
        }

        @Override
        public String toString() {
            if (isValid) {
                return "Password is valid";
            } else {
                return "Password is invalid. Errors: " + String.join(", ", errors);
            }
        }
    }

    /**
     * Проверка валидности пароля (сохранен для обратной совместимости)
     * @param password пароль
     * @param userName имя пользователя
     * @return возвращает true, если пароль отвечает всем требованиям
     */
    public static boolean isValidPassword(String password, String userName) {
        return validatePassword(password, userName).isValid();
    }

    /**
     * Проверка валидности пароля с детальной информацией об ошибках
     * @param password пароль
     * @param userName имя пользователя
     * @return объект ValidationResult с результатом проверки
     */
    public static ValidationResult validatePassword(String password, String userName) {
        List<String> errors = new ArrayList<>();

        if (password == null || password.length() < 8) {
            errors.add("Password must be at least 8 characters long");
        }

        if (password != null && !hasDigits(password)) {
            errors.add("Password must contain at least one digit");
        }

        if (password != null && !hasLowercase(password)) {
            errors.add("Password must contain at least one lowercase letter");
        }

        if (password != null && !hasUppercase(password)) {
            errors.add("Password must contain at least one uppercase letter");
        }

        if (password != null && userName != null && password.equals(userName)) {
            errors.add("Password must not match the username");
        }
        
        if (password != null && hasSpacesOrQuotes(password)) {
            errors.add("Password must not contain spaces, tabs, or quotes");
        }

        boolean isValid = errors.isEmpty();
        return new ValidationResult(isValid, errors);
    }

    private static boolean hasDigits(String text) {
        for (char symbol : text.toCharArray()) {
            if (Character.isDigit(symbol)) {
                return true;
            }
        }
        return false;
    }

    private static boolean hasLowercase(String text) {
        for (char symbol : text.toCharArray()) {
            if (Character.isLowerCase(symbol)) {
                return true;
            }
        }
        return false;
    }

    private static boolean hasUppercase(String text) {
        for (char symbol : text.toCharArray()) {
            if (Character.isUpperCase(symbol)) {
                return true;
            }
        }
        return false;
    }

    private static boolean hasSpacesOrQuotes(String text) {
        for (char symbol : text.toCharArray()) {
            if (Character.isSpaceChar(symbol)
                    || symbol == '\t' || symbol == '"' || symbol == '\'') {
                return true;
            }
        }
        return false;
    }
}