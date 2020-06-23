package otus.core.dao;

import otus.core.model.Account;
import otus.core.sessionmanager.SessionManager;

import java.util.Optional;

public interface AccountDao {
    Optional<Account> findById(long id);

    long insertAccount(Account account);

    void updateAccount(Account account);

    void insertOrUpdate(Account account);

    SessionManager getSessionManager();
}
