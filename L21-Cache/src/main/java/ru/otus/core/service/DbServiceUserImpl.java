package ru.otus.core.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.cachehw.HwCache;
import ru.otus.core.dao.UserDao;
import ru.otus.core.model.User;

import java.util.Optional;

public class DbServiceUserImpl implements DBServiceUser {
    private static final Logger logger = LoggerFactory.getLogger(DbServiceUserImpl.class);

    private final UserDao userDao;
    private final HwCache<String, User> cache;
    private boolean cacheEnabled;

    public DbServiceUserImpl(UserDao userDao, boolean cacheEnabled, HwCache<String, User> cache) {
        this.userDao = userDao;
        this.cacheEnabled = cacheEnabled;
        this.cache = cache;
    }

    @Override
    public long saveUser(User user) {
        try (var sessionManager = userDao.getSessionManager()) {
            sessionManager.beginSession();
            try {
                var userId = userDao.insertUser(user);
                sessionManager.commitSession();

                logger.info("created user: {}", userId);
                if (cacheEnabled) {
                    cache.put(String.valueOf(userId), user);
                }
                return userId;
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
                sessionManager.rollbackSession();
                throw new DbServiceException(e);
            }
        }
    }

    @Override
    public Optional<User> getUser(long userId) {
        if (cacheEnabled) {
            Optional<User> userFromCache = Optional.ofNullable(cache.get(String.valueOf(userId)));
            if (userFromCache.isPresent()) {
                logger.info("user: {} was found in cache", userFromCache.orElse(null));
                return userFromCache;
            }
        }
        try (var sessionManager = userDao.getSessionManager()) {
            sessionManager.beginSession();
            try {
                Optional<User> userOptional = userDao.findById(userId);
                logger.info("user: {} was received from DB", userOptional.orElse(null));
                if (cacheEnabled) {
                    userOptional.ifPresent(user -> cache.put(String.valueOf(userId), user));
                }
                return userOptional;
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
                sessionManager.rollbackSession();
            }
            return Optional.empty();
        }
    }

    public void enableCaching(boolean enabled) {
        this.cacheEnabled = enabled;
    }
}
