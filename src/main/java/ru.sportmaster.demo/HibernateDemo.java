package ru.sportmaster.demo;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import ru.sportmaster.demo.model.User;

import java.util.Date;
import java.util.List;

public class HibernateDemo {

    private static final String SELECT_ALL_USERS_QUERY = "SELECT * FROM USER";

    public static void main(String[] args) throws Exception {

        // 1. Создаем соединение с БД
        SessionFactory sessionFactory = new Configuration()
                // Получаем конфигурацию из файла "hibernate.cfg.xml"
                .configure()
                .buildSessionFactory();
        if (sessionFactory != null && sessionFactory.isOpen()) {
            System.out.println("Соединение с БД успешно установлено.\n");
        } else {
            throw new Exception("Невозможно установить соединение с БД.");
        }

        // 2. Открываем сессию работы с БД
        Session session = sessionFactory.openSession();
        if (session != null && session.isOpen()) {
            System.out.println("Сессия работы с БД успешно открыта.\n");
        } else {
            throw new Exception("Невозможно открыть сессию работы с БД.");
        }

        // 3. Выполняем запросы к БД
        // Проверка корректности настройки маппинга - делаем выборку из таблицы
        List<User> users = session.createNativeQuery(SELECT_ALL_USERS_QUERY, User.class).list();
        printUserTableResultSet(users);

        // 4. Закрываем сессию
        session.close();
        if (!session.isOpen()) {
            System.out.println("Сессия работы с БД успешно закрыта.\n");
        } else {
            throw new Exception("Невозможно закрыть сессию работы с БД.");
        }

        // 5. Закрываем соединение с БД
        sessionFactory.close();
        if (sessionFactory.isClosed()) {
            System.out.println("Соединение с БД успешно закрыто.\n");
        } else {
            throw new Exception("Невозможно закрыть соединение с БД.");
        }
    }

    /**
     * Вывод содержимого таблицы USER
     */
    private static void printUserTableResultSet(List<User> users) {

        System.out.println("Выборка из таблицы USER");

        users.forEach(user -> {

            int id = user.getId();
            String name = user.getName();
            Date dateBirth = user.getDateBirth();

            System.out.println(String.format("ID = %d, NAME = \"%s\", DATE_BIRTH = %s", id, name, dateBirth));
        });

        System.out.println(String.format("Всего строк: %d\n", users.size()));
    }
}
