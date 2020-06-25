package ru.otus;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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

    private SessionManagerJdbc sessionManager;

    @BeforeEach
    public void setUp() throws Exception {
        var dataSource = new DataSourceH2();
        createUserTable(dataSource);
        sessionManager = new SessionManagerJdbc(dataSource);

    }

    private void createUserTable(DataSource dataSource) throws SQLException {
        try (var connection = dataSource.getConnection();
             var pst = connection.prepareStatement("create table user(id long auto_increment, name varchar(50), age integer)")) {
            pst.executeUpdate();
        }
        System.out.println("user table created");
    }

    @Test
    public void testUserService() {
        DbExecutorImpl<User> dbExecutor = new DbExecutorImpl<>();
        var userDao = new UserDaoWithMapperImpl(sessionManager, dbExecutor);

        //use UserService without caching
        var dbServiceUser = new DbServiceUserImpl(userDao, false);

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

}
