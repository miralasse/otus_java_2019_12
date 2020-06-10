package otus.jdbc.dao;

import otus.core.dao.AccountDao;
import otus.core.model.Account;
import otus.core.sessionmanager.SessionManager;
import otus.jdbc.DbExecutorImpl;
import otus.jdbc.mapper.JdbcMapperImpl;
import otus.jdbc.mapper.ReflectionHelper;
import otus.jdbc.sessionmanager.SessionManagerJdbc;

import java.util.Optional;

/**
 * AccountDaoWithMapperImpl.
 *
 * @author Evgeniya_Yanchenko
 */
public class AccountDaoWithMapperImpl implements AccountDao {

    private final SessionManagerJdbc sessionManager;
    private final JdbcMapperImpl<Account> jdbcMapper;

    public AccountDaoWithMapperImpl(SessionManagerJdbc sessionManager, DbExecutorImpl<Account> dbExecutor) {
        this.sessionManager = sessionManager;
        this.jdbcMapper = new JdbcMapperImpl<>(sessionManager, dbExecutor, new ReflectionHelper<>(Account.class));
    }

    @Override
    public Optional<Account> findById(long id) {
        return Optional.ofNullable(jdbcMapper.findById(id, Account.class));
    }

    @Override
    public long insertAccount(Account account) {
        jdbcMapper.insert(account);
        return account.getNo();
    }

    @Override
    public void updateAccount(Account account) {
        jdbcMapper.update(account);
    }

    @Override
    public void insertOrUpdate(Account account) {
        jdbcMapper.insertOrUpdate(account);
    }

    @Override
    public SessionManager getSessionManager() {
        return sessionManager;
    }
}
