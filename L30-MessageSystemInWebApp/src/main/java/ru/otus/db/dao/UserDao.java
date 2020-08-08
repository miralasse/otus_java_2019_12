package ru.otus.db.dao;

import ru.otus.db.sessionmanager.SessionManager;
import ru.otus.model.User;

import java.util.List;
import java.util.Optional;

public interface UserDao {

    Optional<User> findById(long id);

    Optional<User> findRandomUser();

    Optional<User> findByLogin(String login);

    long insertUser(User user);

    void updateUser(User user);

    long insertOrUpdate(User user);

    List<User> findAllUsers();

    SessionManager getSessionManager();
}