package ru.otus.db.services;

import static java.util.Collections.emptyList;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.otus.db.dao.UserDao;
import ru.otus.db.sessionmanager.SessionManager;
import ru.otus.model.User;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class DbServiceUserImpl implements DBServiceUser {

    private final UserDao userDao;

    @Override
    public long saveUser(User user) {
        try (SessionManager sessionManager = userDao.getSessionManager()) {
            sessionManager.beginSession();
            try {
                long userId = userDao.insertOrUpdate(user);
                sessionManager.commitSession();

                log.info("created user: {}", userId);
                return userId;
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                sessionManager.rollbackSession();
                throw new DbServiceException(e);
            }
        }
    }

    @Override
    public List<User> findAllUsers() {
        try (SessionManager sessionManager = userDao.getSessionManager()) {
            sessionManager.beginSession();
            try {
                List<User> allUsers = userDao.findAllUsers();

                log.info("allUsers: {}", allUsers);
                return allUsers;
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                sessionManager.rollbackSession();
            }
            return emptyList();
        }
    }

}
