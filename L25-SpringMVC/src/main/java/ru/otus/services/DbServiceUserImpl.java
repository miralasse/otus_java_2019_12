package ru.otus.services;

import static java.util.Collections.emptyList;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.otus.dao.UserDao;
import ru.otus.model.User;
import ru.otus.sessionmanager.SessionManager;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DbServiceUserImpl implements DBServiceUser {
    private static final Logger logger = LoggerFactory.getLogger(DbServiceUserImpl.class);

    private final UserDao userDao;

    @Override
    public long saveUser(User user) {
        try (SessionManager sessionManager = userDao.getSessionManager()) {
            sessionManager.beginSession();
            try {
                long userId = userDao.insertOrUpdate(user);
                sessionManager.commitSession();

                logger.info("created user: {}", userId);
                return userId;
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
                sessionManager.rollbackSession();
                throw new DbServiceException(e);
            }
        }
    }

    @Override
    public Optional<User> findById(long id) {
        try (SessionManager sessionManager = userDao.getSessionManager()) {
            sessionManager.beginSession();
            try {
                Optional<User> userOptional = userDao.findById(id);

                logger.info("user: {}", userOptional.orElse(null));
                return userOptional;
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
                sessionManager.rollbackSession();
            }
            return Optional.empty();
        }
    }

    @Override
    public List<User> findAllUsers() {
        try (SessionManager sessionManager = userDao.getSessionManager()) {
            sessionManager.beginSession();
            try {
                List<User> allUsers = userDao.findAllUsers();

                logger.info("allUsers: {}", allUsers);
                return allUsers;
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
                sessionManager.rollbackSession();
            }
            return emptyList();
        }
    }

    @Override
    public Optional<User> findByLogin(String login) {
        try (SessionManager sessionManager = userDao.getSessionManager()) {
            sessionManager.beginSession();
            try {
                Optional<User> userOptional = userDao.findByLogin(login);

                logger.info("user: {}", userOptional.orElse(null));
                return userOptional;
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
                sessionManager.rollbackSession();
            }
            return Optional.empty();
        }
    }
}
