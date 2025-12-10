package ru.teamscore.iso;
import java.time.LocalDate;
import java.time.temporal.IsoFields;

public class Finder {

    /**
     * Возвращает дату понедельника указанной ISO-недели.
     *
     * @param year  год по ISO (может отличаться от календарного, если неделя из следующего года)
     * @param week  номер недели по ISO (1–53)
     * @return дата понедельника
     * @throws IllegalArgumentException если неделя не существует в данном году по ISO
     */
    public LocalDate getMonday(int year, int week) {
        // Проверяем корректность номера недели для данного года
        validateWeekNumber(year, week);

        try {
            // Используем WeekFields.ISO, но проще через LocalDate + IsoFields
            return LocalDate.of(year, 1, 4)  // гарантированно находится в 1-й ISO-неделе
                    .with(IsoFields.WEEK_OF_WEEK_BASED_YEAR, week)
                    .with(java.time.DayOfWeek.MONDAY);
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid ISO week: " + week + " for year " + year, e);
        }
    }

    /**
     * Проверяет, что номер недели корректен для данного года по ISO.
     */
    private void validateWeekNumber(int year, int week) {
        if (week < 1 || week > 53) {
            throw new IllegalArgumentException("Week number must be between 1 and 53, got: " + week);
        }

        // Определяем максимальное количество недель в данном году по ISO
        int maxWeeksInYear = getMaxWeeksInYear(year);

        if (week > maxWeeksInYear) {
            throw new IllegalArgumentException("Invalid ISO week: " + week + " for year " + year +
                    " (maximum is " + maxWeeksInYear + ")");
        }
    }

    /**
     * Возвращает максимальное количество недель в году по стандарту ISO 8601.
     * Год имеет 53 недели, если:
     * 1. Он начинается в четверг (обычный год) ИЛИ
     * 2. Он начинается в среду и является високосным
     */
    private int getMaxWeeksInYear(int year) {
        // 4 января всегда находится в 1-й неделе года
        LocalDate date = LocalDate.of(year, 1, 4);

        // День недели 1 января
        java.time.DayOfWeek firstDayOfYear = LocalDate.of(year, 1, 1).getDayOfWeek();

        // Проверяем условия для 53 недель
        boolean isLeapYear = java.time.Year.of(year).isLeap();

        if (firstDayOfYear == java.time.DayOfWeek.THURSDAY ||
                (firstDayOfYear == java.time.DayOfWeek.WEDNESDAY && isLeapYear)) {
            return 53;
        } else {
            return 52;
        }
    }

    /**
     * Возвращает дату воскресенья указанной ISO-недели.
     *
     * @param year  год по ISO
     * @param week  номер недели по ISO
     * @return дата воскресенья
     */
    public LocalDate getSunday(int year, int week) {
        return getMonday(year, week).plusDays(6);
    }
}