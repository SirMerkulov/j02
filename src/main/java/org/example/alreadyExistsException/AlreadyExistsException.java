package org.example.alreadyExistsException;

import java.util.*;

/**
        * Пользовательское исключение, возникающее при обнаружении дублирующегося значения
 * в последовательности или коллекции.
        */


class AlreadyExistsException extends Exception {
    private String value;
    private int position;

    /**
     * Создаёт экземпляр исключения с указанием дублирующегося значения
     * и позиции его первого появления.
     *
     * @param value    дублирующееся значение
     * @param position позиция (нумерация с 1), на которой значение было впервые обнаружено
     */
    public AlreadyExistsException(String value, int position) {
        super("Дублирующееся значение: '" + value + "' (первый раз было введено на позиции " + position + ")");
        this.value = value;
        this.position = position;
    }

    public String getValue() {
        return value;
    }

    public int getPosition() {
        return position;
    }
}



