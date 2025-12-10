package ru.teamscore.monthinfo;
import java.time.*;
import java.time.format.TextStyle;
import java.util.Locale;

public class MonthInfo {
    private LocalDateTime dateTime;

    public MonthInfo(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public MonthInfo() {
        this.dateTime = LocalDateTime.now();
    }

    public String getFullMonthName() {
        return dateTime.getMonth().getDisplayName(
                TextStyle.FULL,
                new Locale("ru")
        );
    }

    public int getMonthNumber() {
        return dateTime.getMonthValue();
    }

    // День недели первого числа месяца
    public String getFirstDayOfMonthWeekday() {
        LocalDate firstDay = LocalDate.of(
                dateTime.getYear(),
                dateTime.getMonth(),
                1
        );
        return firstDay.getDayOfWeek()
                .getDisplayName(TextStyle.SHORT, new Locale("ru"))
                .toLowerCase();
    }

    public LocalDate getLastDayOfMonth() {
        return LocalDate.of(
                dateTime.getYear(),
                dateTime.getMonth(),
                dateTime.getMonth().length(dateTime.toLocalDate().isLeapYear())
        );
    }

    public int getDaysInMonth() {
        return dateTime.getMonth().length(dateTime.toLocalDate().isLeapYear());
    }

    public String getQuarterWithYear() {
        int year = dateTime.getYear();
        int month = dateTime.getMonthValue();
        int quarter = (month - 1) / 3 + 1;
        return year + " Q" + quarter;
    }

    // Дополнительный метод для удобного вывода информации
    public void printAllInfo() {
        System.out.println("Полное название месяца: " + getFullMonthName());
        System.out.println("Номер месяца: " + getMonthNumber());
        System.out.println("День недели первого числа: " + getFirstDayOfMonthWeekday());
        System.out.println("Последний день месяца: " + getLastDayOfMonth());
        System.out.println("Количество дней в месяце: " + getDaysInMonth());
        System.out.println("Квартал: " + getQuarterWithYear());
    }
    
}

