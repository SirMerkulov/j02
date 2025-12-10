package ru.teamscore.isotest;
import ru.teamscore.iso.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class ValidatorTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        validator = new Validator();
    }

    @Test
    @DisplayName("Валидация корректных года и недели проходит успешно")
    void validate_validYearAndWeek_doesNotThrow() {
        assertDoesNotThrow(() -> validator.validate(2024, 52));
        assertDoesNotThrow(() -> validator.validate(2020, 53)); // 2020 имеет 53 недели
        assertDoesNotThrow(() -> validator.validate(1, 1));
        assertDoesNotThrow(() -> validator.validate(9999, 1));
    }

    @Test
    @DisplayName("Валидация года меньше 1 бросает исключение")
    void validate_yearLessThanOne_throwsException() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> validator.validate(0, 1)
        );

        assertTrue(exception.getMessage().contains("Year must be between 1 and 9999"));
    }

    @Test
    @DisplayName("Валидация года больше 9999 бросает исключение")
    void validate_yearGreaterThan9999_throwsException() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> validator.validate(10000, 1)
        );

        assertTrue(exception.getMessage().contains("Year must be between 1 and 9999"));
    }

    @ParameterizedTest
    @DisplayName("Валидация некорректного номера недели бросает исключение")
    @ValueSource(ints = {0, -1, 54, 100})
    void validate_invalidWeekNumber_throwsException(int invalidWeek) {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> validator.validate(2024, invalidWeek)
        );

        assertTrue(exception.getMessage().contains("ISO week must be between 1 and 53"));
    }

    @Test
    @DisplayName("Валидация несуществующей недели в году бросает исключение")
    void validate_nonExistentWeekInYear_throwsException() {
        // 2024 год имеет 52 ISO недели, не 53
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> validator.validate(2024, 53)
        );

        assertTrue(exception.getMessage().contains("Week 53 does not exist in ISO year 2024"));
    }

    @ParameterizedTest
    @DisplayName("Получение строки диапазона для различных недель")
    @CsvSource({
            "2024, 1, '2024-01-01 – 2024-01-07'",
            "2024, 18, '2024-04-29 – 2024-05-05'",
            "2024, 52, '2024-12-23 – 2024-12-29'",
            "2020, 53, '2020-12-28 – 2021-01-03'",
            "2023, 1, '2023-01-02 – 2023-01-08'"
    })
    void getWeekRangeString_variousWeeks_returnsCorrectRange(int year, int week, String expected) {
        String result = validator.getWeekRangeString(year, week);
        assertEquals(expected, result);
    }

    @Test
    @DisplayName("Получение строки диапазона вызывает валидацию")
    void getWeekRangeString_callsValidation() {
        // Проверим, что метод валидирует входные данные
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> validator.getWeekRangeString(2024, 53)
        );

        assertTrue(exception.getMessage().contains("does not exist"));
    }

    @Test
    @DisplayName("Проверка, что диапазон содержит правильные дни недели")
    void getWeekRangeString_rangeHasCorrectDaysOfWeek() {
        String range = validator.getWeekRangeString(2024, 10);
        String[] dates = range.split(" – ");

        LocalDate monday = LocalDate.parse(dates[0]);
        LocalDate sunday = LocalDate.parse(dates[1]);

        assertEquals(java.time.DayOfWeek.MONDAY, monday.getDayOfWeek());
        assertEquals(java.time.DayOfWeek.SUNDAY, sunday.getDayOfWeek());
        assertEquals(6, monday.until(sunday).getDays());
    }
}