package ru.teamscore.isotest;
import ru.teamscore.iso.Finder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.jupiter.api.DisplayName;

import java.time.LocalDate;
import java.time.DayOfWeek;

import static org.junit.jupiter.api.Assertions.*;

class FinderTest {

    private Finder finder;

    @BeforeEach
    void setUp() {
        finder = new Finder();
    }

    @Test
    @DisplayName("Получение понедельника для стандартной недели")
    void getMonday_standardWeek_returnsCorrectMonday() {
        // 2024-W01 (ISO неделя 1 2024 года)
        LocalDate result = finder.getMonday(2024, 1);

        assertEquals(LocalDate.of(2024, 1, 1), result);
        assertEquals(DayOfWeek.MONDAY, result.getDayOfWeek());
    }

    @Test
    @DisplayName("Получение понедельника для недели, которая пересекает границу года")
    void getMonday_weekCrossingYearBoundary_returnsCorrectMonday() {
        // 2023-W01 (ISO неделя 1 2023 года начинается 2 января 2023)
        LocalDate result = finder.getMonday(2023, 1);

        assertEquals(LocalDate.of(2023, 1, 2), result);
        assertEquals(DayOfWeek.MONDAY, result.getDayOfWeek());
    }

    @ParameterizedTest
    @DisplayName("Проверка корректности понедельника для различных недель")
    @CsvSource({
            "2024, 1, 2024-01-01",
            "2024, 18, 2024-04-29",
            "2024, 52, 2024-12-23",
            "2023, 52, 2023-12-25"
    })
    void getMonday_variousWeeks_returnsCorrectDate(int year, int week, String expectedDate) {
        LocalDate expected = LocalDate.parse(expectedDate);
        LocalDate result = finder.getMonday(year, week);

        assertEquals(expected, result);
        assertEquals(DayOfWeek.MONDAY, result.getDayOfWeek());
    }

    @Test
    @DisplayName("Получение понедельника для 53-й недели в году с 53 неделями")
    void getMonday_53rdWeekInLongYear_returnsCorrectMonday() {
        // 2020 год имеет 53 ISO недели
        LocalDate result = finder.getMonday(2020, 53);

        assertEquals(LocalDate.of(2020, 12, 28), result);
        assertEquals(DayOfWeek.MONDAY, result.getDayOfWeek());
    }

    @Test
    @DisplayName("Получение воскресенья для недели")
    void getSunday_week_returnsCorrectSunday() {
        LocalDate monday = finder.getMonday(2024, 10);
        LocalDate sunday = finder.getSunday(2024, 10);

        assertEquals(monday.plusDays(6), sunday);
        assertEquals(DayOfWeek.SUNDAY, sunday.getDayOfWeek());
    }

    @Test
    @DisplayName("Попытка получить несуществующую неделю бросает исключение")
    void getMonday_nonExistentWeek_throwsException() {
        // 2024 год имеет 52 ISO недели (не 53)
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> finder.getMonday(2024, 53)
        );

        assertTrue(exception.getMessage().contains("Invalid ISO week: 53 for year 2024"));
    }

    @ParameterizedTest
    @DisplayName("Попытка получить неделю с некорректным номером бросает исключение")
    @ValueSource(ints = {0, 54, -1, 100})
    void getMonday_invalidWeekNumber_throwsException(int invalidWeek) {
        assertThrows(IllegalArgumentException.class,
                () -> finder.getMonday(2024, invalidWeek)
        );
    }

    @Test
    @DisplayName("Получение воскресенья для недели, которая пересекает границу года")
    void getSunday_weekCrossingYearBoundary_returnsCorrectSunday() {
        // 2023-W52 заканчивается 31 декабря 2023
        LocalDate sunday = finder.getSunday(2023, 52);

        assertEquals(LocalDate.of(2023, 12, 31), sunday);
        assertEquals(DayOfWeek.SUNDAY, sunday.getDayOfWeek());
    }
}