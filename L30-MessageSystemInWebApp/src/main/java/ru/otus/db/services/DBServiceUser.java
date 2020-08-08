package ru.otus.db.services;

import ru.otus.model.User;

import java.util.List;
import java.util.Optional;

public interface DBServiceUser {

    long saveUser(User user);

    Optional<User> findById(long id);

    List<User> findAllUsers();

    Optional<User> findByLogin(String login);

    String getUserData(long id);
}
