package ru.teamscore.exception;

import java.util.*;

/**
 * Класс для отслеживания ввода значений и предотвращения дубликатов.
 * В случае попытки добавить уже существующее значение,
 * выбрасывает {@link AlreadyExistsException}.
 */
public class ValueHistory {
    private final Map<String, Integer> firstOccurrences = new LinkedHashMap<>();
    private int counter = 1;

    /**
     * Добавляет значение в историю.
     *
     * @param value значение для добавления
     * @throws AlreadyExistsException если значение уже было добавлено ранее
     */
    public void add(String value) throws AlreadyExistsException {
        if (value == null) {
            throw new IllegalArgumentException("Значение не может быть null");
        }
        String key = value.trim();
        if (firstOccurrences.containsKey(key)) {
            throw new AlreadyExistsException(key, firstOccurrences.get(key));
        }
        firstOccurrences.put(key, counter);
        counter++;
    }

    /**
     * Возвращает все добавленные значения в порядке ввода.
     */
    public List<String> getValues() {
        return new ArrayList<>(firstOccurrences.keySet());
    }

    /**
     * Возвращает позицию первого вхождения значения (нумерация с 1).
     * Возвращает -1, если значение не найдено.
     */
    public int getFirstPosition(String value) {
        return firstOccurrences.getOrDefault(value.trim(), -1);
    }
}