package ru.otus.db.dao;

import ru.otus.db.sessionmanager.SessionManager;
import ru.otus.model.User;

import java.util.List;

public interface UserDao {

    long insertOrUpdate(User user);

    List<User> findAllUsers();

    SessionManager getSessionManager();
}