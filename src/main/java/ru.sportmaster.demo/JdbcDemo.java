package ru.sportmaster.demo;

import java.sql.*;

public class JdbcDemo {

    /**
     * Параметры соединения с БД
     */
    private static final String DB_URL = "jdbc:h2:mem:demodb";
    private static final String DB_USER = "sa";
    private static final String DB_PASS = "";

    /**
     * Запросы к таблице USER
     */
    private static final String CREATE_USER_TABLE =
            "CREATE TABLE USER " +
                    "(ID INTEGER NOT NULL, " +
                    "NAME VARCHAR(255), " +
                    "DATE_BIRTH DATE, " +
                    "PRIMARY KEY (ID))";
    private static final String SELECT_ALL_USERS_QUERY = "SELECT * FROM USER";
    private static final String INSERT_USER_QUERY = "INSERT INTO USER VALUES(?, ?, ?)";
    private static final String UPDATE_USER_QUERY = "UPDATE USER SET NAME = ?, DATE_BIRTH = ? WHERE ID = ?";
    private static final String DELETE_USER_QUERY = "DELETE FROM USER WHERE NAME LIKE ?";

    /**
     * Данные для множественной вставки в таблицу USER
     */
    private static final int[] BATCH_INSERT_IDS = {2, 3};
    private static final String[] BATCH_INSERT_NAMES = {"Абырвалг", "Darth Vader"};
    private static final Date[] BATCH_INSERT_BIRTH_DATES = {Date.valueOf("1983-05-24"), Date.valueOf("2017-11-30")};
    private static final int BATCH_INSERT_ROW_COUNT = 2;

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
        PreparedStatement preparedStatement;
        ResultSet resultSet;
        int recordCount;

        // 2.1 Создаем таблицу
        System.out.println("2.1 Создание таблицы.\n");
        // Выполняем SQL-запрос
        statement = connection.createStatement();
        statement.execute(CREATE_USER_TABLE);
        // Освобождаем ресурсы БД, связанные с выполненным запросом
        statement.close();
        // Проверяем, что таблица создалась
        DatabaseMetaData databaseMetaData = connection.getMetaData();
        resultSet =
                databaseMetaData.getTables(null, null, "USER", null);
        if (resultSet.next()) {
            System.out.println("Таблица USER успешно создана.\n");
        } else {
            resultSet.close();
            throw new Exception("Невозможно создать таблицу USER.");
        }
        resultSet.close();

        // 2.2 Делаем выборку из таблицы
        System.out.println("2.2 Выборка из таблицы.\n");
        statement = connection.createStatement();
        resultSet = statement.executeQuery(SELECT_ALL_USERS_QUERY);
        printUserTableResultSet(resultSet);
        resultSet.close();
        statement.close();

        // 2.3 Делаем вставку в таблицу
        System.out.println("2.3 Вставка записей в таблицу.\n");
        // Формируем параметризованный SQL-запрос
        preparedStatement = connection.prepareStatement(INSERT_USER_QUERY);
        // Задаем значения параметров
        preparedStatement.setInt(1, 1);
        preparedStatement.setString(2, "Вагон Героев");
        preparedStatement.setDate(3, Date.valueOf("1987-01-27"));
        // Выполняем сформированный запрос
        preparedStatement.execute();
        // Проверяем, сколько строк было добавлено
        recordCount = preparedStatement.getUpdateCount();
        if (recordCount == 1) {
            System.out.println("Добавлена 1 строка.\n");
        } else {
            preparedStatement.close();
            throw new Exception("Невозможно добавить строку в таблицу USER.");
        }
        preparedStatement.close();
        // Делаем множественную вставку
        preparedStatement = connection.prepareStatement(INSERT_USER_QUERY);
        for (int i = 0; i < BATCH_INSERT_ROW_COUNT; i++) {
            preparedStatement.setInt(1, BATCH_INSERT_IDS[i]);
            preparedStatement.setString(2, BATCH_INSERT_NAMES[i]);
            preparedStatement.setDate(3, BATCH_INSERT_BIRTH_DATES[i]);
            preparedStatement.addBatch();
        }
        int[] batchRecordCount = preparedStatement.executeBatch();
        // Проверяем, сколько строк было добавлено
        recordCount = 0;
        for (int oneRecordCount : batchRecordCount) {
            if (oneRecordCount >= 0) {
                recordCount += oneRecordCount;
            }
        }
        if (recordCount == BATCH_INSERT_ROW_COUNT) {
            System.out.println(String.format("Добавлено %d строк(и).\n", BATCH_INSERT_ROW_COUNT));
        } else {
            preparedStatement.close();
            throw new Exception("Невозможно добавить строки в таблицу USER.");
        }
        preparedStatement.close();
        // Проверяем факт вставки - делаем выборку из таблицы
        statement = connection.createStatement();
        resultSet = statement.executeQuery(SELECT_ALL_USERS_QUERY);
        printUserTableResultSet(resultSet);
        resultSet.close();
        statement.close();

        // 2.4 Обновляем запись в таблице
        System.out.println("2.4 Обновление записей в таблице.\n");
        // Формируем и выполняем параметризованный SQL-запрос
        preparedStatement = connection.prepareStatement(UPDATE_USER_QUERY);
        preparedStatement.setString(1, "Вася Пупкин");
        preparedStatement.setDate(2, Date.valueOf("1997-02-14"));
        preparedStatement.setInt(3, 2);
        preparedStatement.execute();
        // Проверяем, сколько строк было обновлено
        recordCount = preparedStatement.getUpdateCount();
        if (recordCount == 1) {
            System.out.println("Обновлена 1 строка.\n");
        } else {
            preparedStatement.close();
            throw new Exception("Невозможно обновить строку в таблице USER.");
        }
        preparedStatement.close();
        // Проверяем факт вставки - делаем выборку из таблицы
        statement = connection.createStatement();
        resultSet = statement.executeQuery(SELECT_ALL_USERS_QUERY);
        printUserTableResultSet(resultSet);
        resultSet.close();
        statement.close();

        // 2.5 Удаляем записи
        System.out.println("2.5 Удаление записей из таблицы.\n");
        // Формируем и выполняем параметризованный SQL-запрос
        preparedStatement = connection.prepareStatement(DELETE_USER_QUERY);
        // Задаем значения параметров
        preparedStatement.setString(1, "Ва%");
        // Выполняем сформированный запрос
        preparedStatement.execute();
        // Проверяем, сколько строк было удалено
        recordCount = preparedStatement.getUpdateCount();
        if (recordCount == 2) {
            System.out.println("Удалено 2 строки.\n");
        } else {
            preparedStatement.close();
            throw new Exception("Невозможно удалить строки из таблицы USER.");
        }
        preparedStatement.close();
        // Проверяем факт удаления - делаем выборку из таблицы
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