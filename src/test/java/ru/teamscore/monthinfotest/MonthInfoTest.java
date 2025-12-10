package ru.teamscore.monthinfotest;

import ru.teamscore.monthinfo.MonthInfo;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import java.time.Month;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("Тестирование класса MonthInfo")
class MonthInfoTest {

    @Nested
    @DisplayName("Полное название месяца")
    class FullMonthNameTests {

        @Test
        @DisplayName("Январь должен возвращать 'января'")
        void januaryReturnsCorrectName() {
            MonthInfo info = new MonthInfo(LocalDateTime.of(2023, Month.JANUARY, 15, 10, 30));
            assertEquals("января", info.getFullMonthName());
        }

        @Test
        @DisplayName("Декабрь должен возвращать 'декабря'")
        void decemberReturnsCorrectName() {
            MonthInfo info = new MonthInfo(LocalDateTime.of(2023, Month.DECEMBER, 25, 14, 0));
            assertEquals("декабря", info.getFullMonthName());
        }
    }

    @Nested
    @DisplayName("Номер месяца")
    class MonthNumberTests {

        @Test
        @DisplayName("Март должен возвращать 3")
        void marchReturnsNumber3() {
            MonthInfo info = new MonthInfo(LocalDateTime.of(2023, Month.MARCH, 10, 9, 0));
            assertEquals(3, info.getMonthNumber());
        }

        @Test
        @DisplayName("Октябрь должен возвращать 10")
        void octoberReturnsNumber10() {
            MonthInfo info = new MonthInfo(LocalDateTime.of(2023, Month.OCTOBER, 5, 11, 45));
            assertEquals(10, info.getMonthNumber());
        }
    }

    @Nested
    @DisplayName("День недели первого числа")
    class FirstDayOfMonthWeekdayTests {

        @Test
        @DisplayName("1 января 2023 (воскресенье) должен возвращать 'вс'")
        void january2023FirstDayIsSunday() {
            MonthInfo info = new MonthInfo(LocalDateTime.of(2023, Month.JANUARY, 15, 0, 0));
            assertEquals("вс", info.getFirstDayOfMonthWeekday());
        }

        @Test
        @DisplayName("1 февраля 2023 (среда) должен возвращать 'ср'")
        void february2023FirstDayIsWednesday() {
            MonthInfo info = new MonthInfo(LocalDateTime.of(2023, Month.FEBRUARY, 10, 0, 0));
            assertEquals("ср", info.getFirstDayOfMonthWeekday());
        }

        @Test
        @DisplayName("1 марта 2024 (пятница) должен возвращать 'пт'")
        void march2024FirstDayIsFriday() {
            MonthInfo info = new MonthInfo(LocalDateTime.of(2024, Month.MARCH, 1, 0, 0));
            assertEquals("пт", info.getFirstDayOfMonthWeekday());
        }
    }

    @Nested
    @DisplayName("Последний день месяца")
    class LastDayOfMonthTests {

        @Test
        @DisplayName("Январь 2023 должен заканчиваться 31 января")
        void january2023EndsOn31() {
            MonthInfo info = new MonthInfo(LocalDateTime.of(2023, Month.JANUARY, 15, 0, 0));
            assertEquals(java.time.LocalDate.of(2023, Month.JANUARY, 31), info.getLastDayOfMonth());
        }

        @Test
        @DisplayName("Февраль 2023 (невисокосный) должен заканчиваться 28 февраля")
        void february2023NonLeapYearEndsOn28() {
            MonthInfo info = new MonthInfo(LocalDateTime.of(2023, Month.FEBRUARY, 1, 0, 0));
            assertEquals(java.time.LocalDate.of(2023, Month.FEBRUARY, 28), info.getLastDayOfMonth());
        }

        @Test
        @DisplayName("Февраль 2024 (високосный) должен заканчиваться 29 февраля")
        void february2024LeapYearEndsOn29() {
            MonthInfo info = new MonthInfo(LocalDateTime.of(2024, Month.FEBRUARY, 1, 0, 0));
            assertEquals(java.time.LocalDate.of(2024, Month.FEBRUARY, 29), info.getLastDayOfMonth());
        }

        @Test
        @DisplayName("Апрель 2023 должен заканчиваться 30 апреля")
        void april2023EndsOn30() {
            MonthInfo info = new MonthInfo(LocalDateTime.of(2023, Month.APRIL, 15, 0, 0));
            assertEquals(java.time.LocalDate.of(2023, Month.APRIL, 30), info.getLastDayOfMonth());
        }
    }

    @Nested
    @DisplayName("Количество дней в месяце")
    class DaysInMonthTests {

        @Test
        @DisplayName("Январь 2023 должен содержать 31 день")
        void january2023Has31Days() {
            MonthInfo info = new MonthInfo(LocalDateTime.of(2023, Month.JANUARY, 1, 0, 0));
            assertEquals(31, info.getDaysInMonth());
        }

        @Test
        @DisplayName("Февраль 2023 (невисокосный) должен содержать 28 дней")
        void february2023NonLeapYearHas28Days() {
            MonthInfo info = new MonthInfo(LocalDateTime.of(2023, Month.FEBRUARY, 1, 0, 0));
            assertEquals(28, info.getDaysInMonth());
        }

        @Test
        @DisplayName("Февраль 2024 (високосный) должен содержать 29 дней")
        void february2024LeapYearHas29Days() {
            MonthInfo info = new MonthInfo(LocalDateTime.of(2024, Month.FEBRUARY, 1, 0, 0));
            assertEquals(29, info.getDaysInMonth());
        }

        @Test
        @DisplayName("Ноябрь 2023 должен содержать 30 дней")
        void november2023Has30Days() {
            MonthInfo info = new MonthInfo(LocalDateTime.of(2023, Month.NOVEMBER, 15, 0, 0));
            assertEquals(30, info.getDaysInMonth());
        }
    }

    @Nested
    @DisplayName("Квартал с годом")
    class QuarterWithYearTests {

        @Test
        @DisplayName("Январь 2023 должен быть в 2023 Q1")
        void january2023InQ1() {
            MonthInfo info = new MonthInfo(LocalDateTime.of(2023, Month.JANUARY, 1, 0, 0));
            assertEquals("2023 Q1", info.getQuarterWithYear());
        }

        @Test
        @DisplayName("Март 2023 должен быть в 2023 Q1")
        void march2023InQ1() {
            MonthInfo info = new MonthInfo(LocalDateTime.of(2023, Month.MARCH, 31, 0, 0));
            assertEquals("2023 Q1", info.getQuarterWithYear());
        }

        @Test
        @DisplayName("Апрель 2023 должен быть в 2023 Q2")
        void april2023InQ2() {
            MonthInfo info = new MonthInfo(LocalDateTime.of(2023, Month.APRIL, 1, 0, 0));
            assertEquals("2023 Q2", info.getQuarterWithYear());
        }

        @Test
        @DisplayName("Декабрь 2024 должен быть в 2024 Q4")
        void december2024InQ4() {
            MonthInfo info = new MonthInfo(LocalDateTime.of(2024, Month.DECEMBER, 31, 23, 59));
            assertEquals("2024 Q4", info.getQuarterWithYear());
        }


    }
}