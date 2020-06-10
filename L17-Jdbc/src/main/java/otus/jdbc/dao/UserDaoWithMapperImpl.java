package otus.jdbc.dao;

import otus.core.dao.UserDao;
import otus.core.model.User;
import otus.core.sessionmanager.SessionManager;
import otus.jdbc.DbExecutorImpl;
import otus.jdbc.mapper.JdbcMapperImpl;
import otus.jdbc.mapper.ReflectionHelper;
import otus.jdbc.sessionmanager.SessionManagerJdbc;

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
