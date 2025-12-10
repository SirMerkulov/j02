package ru.teamscore.exception;

import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ValueHistory history = new ValueHistory();

        System.out.println("Введите значения (для выхода введите 'exit'):");

        while (true) {
            System.out.print("Введите значение #" + (history.getValues().size() + 1) + ": ");
            String input = scanner.nextLine();

            if (input == null) {
                System.out.println("Ввод прерван.");
                break;
            }

            input = input.trim();
            if (input.equalsIgnoreCase("exit")) {
                System.out.println("Программа завершена.");
                break;
            }

            try {
                history.add(input);
                System.out.println("Значение '" + input + "' успешно добавлено.");
            } catch (AlreadyExistsException e) {
                System.out.println("Ошибка: " + e.getMessage());
                System.out.println("Попробуйте ввести другое значение.");
            } catch (IllegalArgumentException e) {
                System.out.println("Некорректный ввод: " + e.getMessage());
            }
        }

        scanner.close();

        // Вывод всех введённых значений
        System.out.println("\nВсе уникальные введённые значения:");
        List<String> values = history.getValues();
        for (int i = 0; i < values.size(); i++) {
            System.out.println((i + 1) + ". " + values.get(i));
        }
    }
}