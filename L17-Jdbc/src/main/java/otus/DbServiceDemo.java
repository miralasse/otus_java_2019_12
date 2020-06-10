package otus;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import otus.core.model.Account;
import otus.core.model.User;
import otus.core.service.DbServiceAccountImpl;
import otus.core.service.DbServiceUserImpl;
import otus.h2.DataSourceH2;
import otus.jdbc.DbExecutorImpl;
import otus.jdbc.dao.AccountDaoWithMapperImpl;
import otus.jdbc.dao.UserDaoWithMapperImpl;
import otus.jdbc.sessionmanager.SessionManagerJdbc;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Optional;

/**
 * @author sergey
 * created on 03.02.19.
 */
public class DbServiceDemo {
    private static final Logger logger = LoggerFactory.getLogger(DbServiceDemo.class);

    public static void main(String[] args) throws Exception {
        var dataSource = new DataSourceH2();
        var demo = new DbServiceDemo();

        demo.createUserTable(dataSource);
        demo.createAccountTable(dataSource);

        var sessionManager = new SessionManagerJdbc(dataSource);
        DbExecutorImpl<User> dbExecutor = new DbExecutorImpl<>();
        var userDao = new UserDaoWithMapperImpl(sessionManager, dbExecutor);

        var dbServiceUser = new DbServiceUserImpl(userDao);
        var id = dbServiceUser.saveUser(new User(0, "dbServiceUser", 25));
        Optional<User> user = dbServiceUser.getUser(id);

        user.ifPresentOrElse(
                crUser -> logger.info("created user, name:{}", crUser.getName()),
                () -> logger.info("user was not created")
        );

        DbExecutorImpl<Account> dbAccountExecutor = new DbExecutorImpl<>();
        var accountDao = new AccountDaoWithMapperImpl(sessionManager, dbAccountExecutor);

        var dbServiceAccount = new DbServiceAccountImpl(accountDao);
        var accountNo = dbServiceAccount.saveAccount(new Account(1, "dbServiceAccount", new BigDecimal("1000.00")));
        Optional<Account> account = dbServiceAccount.getAccount(accountNo);

        account.ifPresentOrElse(
                crAccount -> logger.info("created account, type:{}, rest:{}", crAccount.getType(), crAccount.getRest()),
                () -> logger.info("account was not created")
        );
    }

    private void createUserTable(DataSource dataSource) throws SQLException {
        try (var connection = dataSource.getConnection();
             var pst = connection.prepareStatement("create table user(id long auto_increment, name varchar(50), age integer)")) {
            pst.executeUpdate();
        }
        System.out.println("user table created");
    }

    private void createAccountTable(DataSource dataSource) throws SQLException {
        try (var connection = dataSource.getConnection();
             var pst = connection.prepareStatement("create table account(no bigint(20) NOT NULL auto_increment, type varchar(255), rest number)")) {
            pst.executeUpdate();
        }
        System.out.println("account table created");
    }
}
