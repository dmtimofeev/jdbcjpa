package ru.sportmaster.demo;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.criterion.Restrictions;
import ru.sportmaster.demo.model.User;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.Date;
import java.util.List;

public class HibernateDemo {

    private static final String SELECT_USERS_SQL_QUERY = "SELECT * FROM USER WHERE ID < 3";

    private static final String SELECT_USERS_HQL_QUERY = "select u from USER u where u.id < 3";

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
        List<User> users;
        // 3.1 Запросы на SQL
        System.out.println("1. Запрос на SQL");
        users = session
                .createNativeQuery(SELECT_USERS_SQL_QUERY, User.class)
                .list();
        printUserTableResultSet(users);
        // 3.2 Запросы на HQL
        System.out.println("2. Запрос на HQL");
        users = session
                .createQuery(SELECT_USERS_HQL_QUERY, User.class)
                .list();
        printUserTableResultSet(users);
        // 3.3 Запросы на HQL
        System.out.println("3. Запрос с использованием Criteria");
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<User> criteriaQuery = criteriaBuilder.createQuery(User.class);
        Root<User> root = criteriaQuery.from(User.class);
        criteriaQuery.select(root);
        criteriaQuery.where(criteriaBuilder.lessThan(root.get("id"), 3));
        users = session.createQuery(criteriaQuery).getResultList();
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
