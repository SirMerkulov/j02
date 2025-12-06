package org.example.alreadyExistsException;
import java.util.*;
public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        List<String> enteredValues = new ArrayList<>();
        int counter = 1;

        System.out.println("Введите значения (для выхода введите 'exit'):");

        while (true) {
            System.out.print("Введите значение #" + counter + ": ");
            String input = scanner.nextLine().trim();

            // Проверка на выход
            if (input.equalsIgnoreCase("exit")) {
                System.out.println("Программа завершена.");
                break;
            }

            try {
                // Проверка на дубликат
                if (enteredValues.contains(input)) {
                    int firstPosition = enteredValues.indexOf(input) + 1; // +1 для пользовательского номера
                    throw new AlreadyExistsException(input, firstPosition);
                }

                // Если дубликата нет, добавляем значение
                enteredValues.add(input);
                System.out.println("Значение '" + input + "' успешно добавлено.");
                counter++;

            } catch (AlreadyExistsException e) {
                System.out.println("Ошибка: " + e.getMessage());
                System.out.println("Попробуйте ввести другое значение.");
            }
        }

        scanner.close();

        // Вывод всех введенных значений
        System.out.println("\nВсе уникальные введенные значения:");
        for (int i = 0; i < enteredValues.size(); i++) {
            System.out.println((i + 1) + ". " + enteredValues.get(i));
        }
    }
}