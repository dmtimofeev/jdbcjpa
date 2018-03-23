package ru.sportmaster.demo;

import java.sql.Connection;
import java.sql.DriverManager;

public class JdbcDemo {

    /**
     * Параметры соединения с БД
     */
    private static final String DB_URL = "jdbc:h2:mem:demodb";
    private static final String DB_USER = "sa";
    private static final String DB_PASS = "";

    public static void main(String[] args) throws Exception {

        // 1. Создаем соединение с БД
        System.out.println("1. Установка соединения с БД.\n");
        Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
        if (connection != null && !connection.isClosed()) {
            System.out.println("Соединение с БД успешно установлено.\n");
        } else {
            throw new Exception("Невозможно установить соединение с БД.");
        }

        // 2. Выполняем запросы к БД
        System.out.println("2. Выполнение запросов к БД.\n");

        //TODO: создание таблицы, а также добавление, изменение, удаление и выборка записей таблицы

        // 3. Закрываем соединение с БД
        System.out.println("3. Закрытие соединения с БД.\n");
        connection.close();
        if (connection.isClosed()) {
            System.out.println("Соединение с БД успешно закрыто.\n");
        } else {
            throw new Exception("Невозможно закрыть соединение с БД.");
        }
    }
}
