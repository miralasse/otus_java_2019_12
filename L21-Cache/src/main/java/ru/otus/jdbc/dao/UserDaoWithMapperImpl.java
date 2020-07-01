package ru.otus.jdbc.dao;


import ru.otus.core.dao.UserDao;
import ru.otus.core.model.User;
import ru.otus.core.sessionmanager.SessionManager;
import ru.otus.jdbc.DbExecutorImpl;
import ru.otus.jdbc.mapper.JdbcMapperImpl;
import ru.otus.jdbc.mapper.ReflectionHelper;
import ru.otus.jdbc.sessionmanager.SessionManagerJdbc;

import java.util.Optional;

/**
 * UserDaoWithMapperImpl.
 *
 * @author Evgeniya_Yanchenko
 */
public class UserDaoWithMapperImpl implements UserDao {

    private final SessionManagerJdbc sessionManager;
    private final JdbcMapperImpl<User> jdbcMapper;

    public UserDaoWithMapperImpl(SessionManagerJdbc sessionManager, DbExecutorImpl<User> dbExecutor) {
        this.sessionManager = sessionManager;
        this.jdbcMapper = new JdbcMapperImpl<>(sessionManager, dbExecutor, new ReflectionHelper<>(User.class));
    }

    @Override
    public Optional<User> findById(long id) {
        return Optional.ofNullable(jdbcMapper.findById(id, User.class));
    }

    @Override
    public long insertUser(User user) {
        jdbcMapper.insert(user);
        return user.getId();
    }

    @Override
    public void updateUser(User user) {
        jdbcMapper.update(user);
    }

    @Override
    public void insertOrUpdate(User user) {
        jdbcMapper.insertOrUpdate(user);
    }

    @Override
    public SessionManager getSessionManager() {
        return sessionManager;
    }
}
