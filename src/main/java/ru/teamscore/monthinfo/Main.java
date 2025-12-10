package ru.teamscore.monthinfo;

import java.time.LocalDateTime;

public class Main {
    public static void main(String[] args) {
        // Создание объекта с текущей датой
        MonthInfo currentMonth = new MonthInfo();
        System.out.println("=== Информация о текущем месяце ===");
        currentMonth.printAllInfo();

        System.out.println("\n=== Информация с конкретной датой ===");

        // Создание объекта с конкретной датой
        LocalDateTime specificDate = LocalDateTime.of(2023, 3, 15, 10, 30);
        MonthInfo march2023 = new MonthInfo(specificDate);
        march2023.printAllInfo();
    }
}