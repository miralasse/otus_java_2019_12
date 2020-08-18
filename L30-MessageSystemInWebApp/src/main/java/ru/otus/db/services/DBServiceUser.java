package ru.otus.db.services;

import ru.otus.model.User;

import java.util.List;

public interface DBServiceUser {

    long saveUser(User user);

    List<User> findAllUsers();
}
