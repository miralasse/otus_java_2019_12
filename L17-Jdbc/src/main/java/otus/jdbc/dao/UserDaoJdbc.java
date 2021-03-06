package otus.jdbc.dao;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import otus.core.dao.UserDao;
import otus.core.dao.UserDaoException;
import otus.core.model.User;
import otus.core.sessionmanager.SessionManager;
import otus.jdbc.DbExecutorImpl;
import otus.jdbc.sessionmanager.SessionManagerJdbc;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collections;
import java.util.Optional;

public class UserDaoJdbc implements UserDao {
    private static final Logger logger = LoggerFactory.getLogger(UserDaoJdbc.class);

    private final SessionManagerJdbc sessionManager;
    private final DbExecutorImpl<User> dbExecutor;

    public UserDaoJdbc(SessionManagerJdbc sessionManager, DbExecutorImpl<User> dbExecutor) {
        this.sessionManager = sessionManager;
        this.dbExecutor = dbExecutor;
    }

    @Override
    public Optional<User> findById(long id) {
        try {
            return dbExecutor.executeSelect(getConnection(), "select id, name, age from user where id  = ?",
                    id, rs -> {
                        try {
                            if (rs.next()) {
                                return new User(rs.getLong("id"), rs.getString("name"), rs.getInt("age"));
                            }
                        } catch (SQLException e) {
                            logger.error(e.getMessage(), e);
                        }
                        return null;
                    });
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return Optional.empty();
    }

    @Override
    public long insertUser(User user) {
        try {
            return dbExecutor.executeInsert(getConnection(), "insert into user(name) values (?)",
                    Collections.singletonList(user.getName()));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new UserDaoException(e);
        }
    }

    @Override
    public void updateUser(User user) {

    }

    @Override
    public void insertOrUpdate(User user) {

    }

    @Override
    public SessionManager getSessionManager() {
        return sessionManager;
    }

    private Connection getConnection() {
        return sessionManager.getCurrentSession().getConnection();
    }
}
