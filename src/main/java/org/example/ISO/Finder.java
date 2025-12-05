package org.example.ISO;

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