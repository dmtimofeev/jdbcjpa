package ru.sportmaster.demo;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;
import ru.sportmaster.demo.model.User;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

public class HibernateDemo {

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
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<User> allUsersCriteriaQuery = criteriaBuilder.createQuery(User.class);
        Root<User> userRoot = allUsersCriteriaQuery.from(User.class);
        allUsersCriteriaQuery.select(userRoot);
        Query<User> allUsersQuery = session.createQuery(allUsersCriteriaQuery);

        // Начало транзакции
        Transaction transaction = session.beginTransaction();

        // Выборка данных из таблицы
        users = allUsersQuery.getResultList();
        printUserTableResultSet(users);

        // 3.1 Вставка новой записи
        System.out.println("1. Вставка новой записи");
        User newUser = new User();
        newUser.setId(4);
        newUser.setName("Вася Пупкин");
        newUser.setDateBirth(Timestamp.valueOf("2012-12-21 00:00:00"));
        session.save(newUser);
        // Выборка данных из таблицы
        users = allUsersQuery.getResultList();
        printUserTableResultSet(users);
        // 3.2 Изменение записи
        System.out.println("2. Изменение записи");
        User editUser = session.get(User.class, 4);
        editUser.setName("Поросенок Петр");
        session.update(editUser);
        // Выборка данных из таблицы
        users = allUsersQuery.getResultList();
        printUserTableResultSet(users);
        // 3.3 Удаление записи
        System.out.println("3. Удаление записи");
        User deleteUser = session.get(User.class, 4);
        session.delete(deleteUser);
        // Выборка данных из таблицы
        users = allUsersQuery.getResultList();
        printUserTableResultSet(users);

        // Коммит транзакции
        transaction.commit();

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
