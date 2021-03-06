package ru.otus.hibernate.dao;


import org.hibernate.Session;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.dao.UserDao;
import ru.otus.dao.UserDaoException;
import ru.otus.hibernate.sessionmanager.DatabaseSessionHibernate;
import ru.otus.hibernate.sessionmanager.SessionManagerHibernate;
import ru.otus.model.User;
import ru.otus.sessionmanager.SessionManager;

import java.util.List;
import java.util.Optional;
import java.util.Random;

import static java.util.Collections.emptyList;

public class UserDaoHibernate implements UserDao {
    private static final Logger logger = LoggerFactory.getLogger(UserDaoHibernate.class);

    private final SessionManagerHibernate sessionManager;

    public UserDaoHibernate(SessionManagerHibernate sessionManager) {
        this.sessionManager = sessionManager;
    }


    @Override
    public Optional<User> findById(long id) {
        DatabaseSessionHibernate currentSession = sessionManager.getCurrentSession();
        try {
            return Optional.ofNullable(currentSession.getHibernateSession().find(User.class, id));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return Optional.empty();
    }

    @Override
    public Optional<User> findRandomUser() {
        Random r = new Random();
        List<User> allUsers = findAllUsers();
        return allUsers.stream().skip(r.nextInt(allUsers.size() - 1)).findFirst();
    }

    @Override
    public Optional<User> findByLogin(String login) {
        Session session = sessionManager.getCurrentSession().getHibernateSession();
        try {
            Query<User> query = session.createQuery("SELECT u FROM User u where login=:login", User.class);
            query.setParameter("login", login);
            return Optional.ofNullable(query.uniqueResult());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return Optional.empty();
    }

    @Override
    public long insertUser(User user) {
        DatabaseSessionHibernate currentSession = sessionManager.getCurrentSession();
        try {
            Session hibernateSession = currentSession.getHibernateSession();
            hibernateSession.persist(user);
            hibernateSession.flush();
            return user.getId();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new UserDaoException(e);
        }
    }

    @Override
    public void updateUser(User user) {
        DatabaseSessionHibernate currentSession = sessionManager.getCurrentSession();
        try {
            Session hibernateSession = currentSession.getHibernateSession();
            hibernateSession.merge(user);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new UserDaoException(e);
        }
    }

    @Override
    public long insertOrUpdate(User user) {
        DatabaseSessionHibernate currentSession = sessionManager.getCurrentSession();
        try {
            Session hibernateSession = currentSession.getHibernateSession();
            if (user.getId() != null) {
                hibernateSession.merge(user);
            } else {
                hibernateSession.persist(user);
                hibernateSession.flush();
            }
            return user.getId();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new UserDaoException(e);
        }
    }

    @Override
    public List<User> findAllUsers() {
        Session session = sessionManager.getCurrentSession().getHibernateSession();
        try {
            return session.createQuery("SELECT u FROM User u", User.class).getResultList();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return emptyList();
    }


    @Override
    public SessionManager getSessionManager() {
        return sessionManager;
    }
}
