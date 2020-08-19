package ru.otus.db.hibernate.dao;


import static java.util.Collections.emptyList;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;
import ru.otus.db.dao.UserDao;
import ru.otus.db.dao.UserDaoException;
import ru.otus.db.hibernate.sessionmanager.DatabaseSessionHibernate;
import ru.otus.db.hibernate.sessionmanager.SessionManagerHibernate;
import ru.otus.db.sessionmanager.SessionManager;
import ru.otus.model.User;

import java.util.List;

@Slf4j
@Repository
@RequiredArgsConstructor
public class UserDaoHibernate implements UserDao {

    private final SessionManagerHibernate sessionManager;

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
            log.error(e.getMessage(), e);
            throw new UserDaoException(e);
        }
    }

    @Override
    public List<User> findAllUsers() {
        Session session = sessionManager.getCurrentSession().getHibernateSession();
        try {
            return session.createQuery("SELECT u FROM User u", User.class).getResultList();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return emptyList();
    }


    @Override
    public SessionManager getSessionManager() {
        return sessionManager;
    }
}
