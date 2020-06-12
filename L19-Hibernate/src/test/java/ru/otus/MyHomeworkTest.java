package ru.otus;

import static java.util.stream.Collectors.toSet;
import static org.assertj.core.api.Assertions.assertThat;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.otus.core.dao.UserDao;
import ru.otus.core.model.AddressDataSet;
import ru.otus.core.model.PhoneDataSet;
import ru.otus.core.model.User;
import ru.otus.core.service.DBServiceUser;
import ru.otus.core.service.DbServiceUserImpl;
import ru.otus.hibernate.HibernateUtils;
import ru.otus.hibernate.dao.UserDaoHibernate;
import ru.otus.hibernate.sessionmanager.SessionManagerHibernate;

import java.util.Optional;
import java.util.Random;
import java.util.Set;
import java.util.stream.Stream;

@DisplayName("Демо работы ДЗ")
public class MyHomeworkTest {

    private static final String HIBERNATE_CFG_XML_FILE_RESOURCE = "hibernate-test.cfg.xml";
    private SessionFactory sessionFactory;
    private DBServiceUser dbServiceUser;

    @BeforeEach
    public void setUp() {
        sessionFactory = HibernateUtils.buildSessionFactory(HIBERNATE_CFG_XML_FILE_RESOURCE,
                User.class, AddressDataSet.class, PhoneDataSet.class);
        SessionManagerHibernate sessionManager = new SessionManagerHibernate(sessionFactory);
        UserDao userDao = new UserDaoHibernate(sessionManager);
        dbServiceUser = new DbServiceUserImpl(userDao);
    }

    @AfterEach
    void tearDown() {
        sessionFactory.close();
    }

    @Test
    @DisplayName(" корректно сохранять пользователя с вложенными объектами")
    void shouldSaveUserCorrectly() {
        User savedUser = buildDefaultUser();
        long id = dbServiceUser.saveUser(savedUser);

        try (Session session = sessionFactory.openSession()) {
            User loadedUser = session.find(User.class, id);

            System.out.println(savedUser);
            System.out.println(loadedUser);
            assertThat(loadedUser).isEqualToComparingFieldByField(savedUser);
        }
    }

    @Test
    @DisplayName(" корректно читать пользователя с вложенными объектами")
    void shouldLoadUserCorrectly() {
        User savedUser = buildDefaultUser();
        long id = dbServiceUser.saveUser(savedUser);

        Optional<User> mayBeUser = dbServiceUser.getUser(id);

        System.out.println(savedUser);
        mayBeUser.ifPresent(System.out::println);
        assertThat(mayBeUser).isPresent().get().isEqualToComparingFieldByField(savedUser);
    }

    private User buildDefaultUser() {
        User user = new User("Вася");
        Set<PhoneDataSet> phones = Stream.generate(new Random()::nextInt)
                .limit(3)
                .map(i -> new PhoneDataSet(String.valueOf(i), user))
                .collect(toSet());
        user.setPhones(phones);
        AddressDataSet address = new AddressDataSet("Street");
        user.setAddress(address);
        return user;
    }


}
