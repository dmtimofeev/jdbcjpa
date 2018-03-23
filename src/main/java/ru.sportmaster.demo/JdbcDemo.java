package ru.sportmaster.demo;

import java.sql.*;

public class JdbcDemo {

    /**
     * Параметры соединения с БД
     */
    private static final String DB_URL = "jdbc:h2:mem:demodb;INIT=runscript from './db/init.sql'";
    private static final String DB_USER = "sa";
    private static final String DB_PASS = "";

    /**
     * Запросы к таблице USER
     */
    private static final String SELECT_ALL_USERS_QUERY = "SELECT * FROM USER";

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

        Statement statement;
        ResultSet resultSet;

        // Делаем выборку из таблицы
        statement = connection.createStatement();
        resultSet = statement.executeQuery(SELECT_ALL_USERS_QUERY);
        printUserTableResultSet(resultSet);
        resultSet.close();
        statement.close();

        // 3. Закрываем соединение с БД
        System.out.println("3. Закрытие соединения с БД.\n");
        connection.close();
        if (connection.isClosed()) {
            System.out.println("Соединение с БД успешно закрыто.\n");
        } else {
            throw new Exception("Невозможно закрыть соединение с БД.");
        }
    }

    /**
     * Вывод содержимого таблицы USER
     */
    private static void printUserTableResultSet(ResultSet resultSet) throws SQLException {

        int rowCount = 0;

        System.out.println("Выборка из таблицы USER");

        while (resultSet.next()) {

            int id = resultSet.getInt("ID");
            String name = resultSet.getString("NAME");
            Date dateBirth = resultSet.getDate("DATE_BIRTH");

            System.out.println(String.format("ID = %d, NAME = \"%s\", DATE_BIRTH = %s", id, name, dateBirth));

            rowCount++;
        }

        System.out.println(String.format("Всего строк: %d\n", rowCount));
    }
}