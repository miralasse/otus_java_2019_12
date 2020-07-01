package ru.otus;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.otus.cachehw.MyCache;
import ru.otus.core.dao.UserDao;
import ru.otus.core.model.User;
import ru.otus.core.service.DbServiceUserImpl;
import ru.otus.h2.DataSourceH2;
import ru.otus.jdbc.DbExecutorImpl;
import ru.otus.jdbc.dao.UserDaoWithMapperImpl;
import ru.otus.jdbc.sessionmanager.SessionManagerJdbc;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class CacheTest {

    private DataSource dataSource;
    private UserDao userDao;

    @BeforeEach
    public void setUp() throws Exception {
        dataSource = new DataSourceH2();
        createUserTable();
        SessionManagerJdbc sessionManager = new SessionManagerJdbc(dataSource);
        DbExecutorImpl<User> dbExecutor = new DbExecutorImpl<>();
        userDao = new UserDaoWithMapperImpl(sessionManager, dbExecutor);
    }

    private void createUserTable() throws SQLException {
        try (var connection = dataSource.getConnection();
             var pst = connection.prepareStatement("create table user(id long auto_increment, name varchar(50), age integer)")) {
            pst.executeUpdate();
        }
        System.out.println("user table created");
    }

    @AfterEach
    public void cleanUp() throws SQLException {
        try (var connection = dataSource.getConnection();
             var pst = connection.prepareStatement("drop table user")) {
            pst.executeUpdate();
        }
        System.out.println("user table removed");
    }

    @Test
    public void testCacheWhenSaving() {
        //use UserService without caching
        var dbServiceUser = new DbServiceUserImpl(userDao, false, new MyCache<>());

        List<Long> userIds = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            userIds.add(dbServiceUser.saveUser(new User(i, "User#" + i, 25)));
        }
        long start = System.currentTimeMillis();
        userIds.forEach(dbServiceUser::getUser);
        long spentWithoutCache = System.currentTimeMillis() - start;
        System.out.println("Без кэша: " + spentWithoutCache + " мс");

        //enable cache
        userIds.clear();
        dbServiceUser.enableCaching(true);

        userIds = new ArrayList<>();
        for (int i = 100; i < 110; i++) {
            userIds.add(dbServiceUser.saveUser(new User(i, "User#" + i, 25)));
        }

        long startWithCache = System.currentTimeMillis();
        userIds.forEach(dbServiceUser::getUser);
        long spentOnReceivingWithCache = System.currentTimeMillis() - startWithCache;
        System.out.println("С кэшем: " + spentOnReceivingWithCache + " мс");

        assertThat(spentOnReceivingWithCache).isLessThan(spentWithoutCache);
    }

    @Test
    public void testCacheWhenGettingTheSameUser() {
        //use UserService without caching
        var dbServiceUser = new DbServiceUserImpl(userDao, false, new MyCache<>());

        List<Long> userIds = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            userIds.add(dbServiceUser.saveUser(new User(i, "User#" + i, 25)));
        }
        long start = System.currentTimeMillis();

        //getting the same user 10 times
        for (int i = 0; i < 10; i++) {
            dbServiceUser.getUser(userIds.get(0));
        }
        long spentWithoutCache = System.currentTimeMillis() - start;
        System.out.println("Без кэша: " + spentWithoutCache + " мс");

        //enable cache
        userIds.clear();
        userIds = new ArrayList<>();
        for (int i = 100; i < 110; i++) {
            userIds.add(dbServiceUser.saveUser(new User(i, "User#" + i, 25)));
        }
        dbServiceUser.enableCaching(true);
        long startWithCache = System.currentTimeMillis();

        //getting the same user 10 times with cache
        for (int i = 0; i < 10; i++) {
            dbServiceUser.getUser(userIds.get(0));
        }
        long spentOnReceivingWithCache = System.currentTimeMillis() - startWithCache;
        System.out.println("С кэшем: " + spentOnReceivingWithCache + " мс");

        assertThat(spentOnReceivingWithCache).isLessThan(spentWithoutCache);
    }

}
