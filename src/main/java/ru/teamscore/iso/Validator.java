package ru.teamscore.iso;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
public class Validator {

    private final Finder finder = new Finder();
    private static final DateTimeFormatter ISO_DATE = DateTimeFormatter.ISO_LOCAL_DATE;

    /**
     * Проверяет корректность года и номера недели по ISO.
     * Год должен быть в разумных пределах (скажем, от 1 до 9999).
     * Неделя — от 1 до 53, и должна существовать в данном ISO-году.
     *
     * @throws IllegalArgumentException при некорректных входных данных
     */
    public void validate(int year, int week) {
        if (year < 1 || year > 9999) {
            throw new IllegalArgumentException("Year must be between 1 and 9999");
        }
        if (week < 1 || week > 53) {
            throw new IllegalArgumentException("ISO week must be between 1 and 53");
        }

        // Проверим, что такая неделя существует: попытаемся вычислить понедельник
        try {
            finder.getMonday(year, week);
        } catch (Exception e) {
            throw new IllegalArgumentException("Week " + week + " does not exist in ISO year " + year, e);
        }
    }

    /**
     * Возвращает строку вида "YYYY-MM-DD – YYYY-MM-DD"
     * — диапазон дат (Пн – Вс) указанной ISO-недели.
     */
    public String getWeekRangeString(int year, int week) {
        validate(year, week);
        LocalDate monday = finder.getMonday(year, week);
        LocalDate sunday = finder.getSunday(year, week);
        return ISO_DATE.format(monday) + " – " + ISO_DATE.format(sunday);
    }
}