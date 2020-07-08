package ru.otus.dao;

import ru.otus.model.User;
import ru.otus.sessionmanager.SessionManager;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

import static java.lang.String.format;

public class InMemoryUserDao implements UserDao {

    private final AtomicLong userIdCount;
    private final Map<Long, User> users;

    public InMemoryUserDao() {
        users = new TreeMap<>();
        users.put(1L, new User(1L, "Крис Гир", "user1", "11111"));
        users.put(2L, new User(2L, "Ая Кэш", "user2", "11111"));
        users.put(3L, new User(3L, "Десмин Боргес", "user3", "11111"));
        users.put(4L, new User(4L, "Кетер Донохью", "user4", "11111"));
        users.put(5L, new User(5L, "Стивен Шнайдер", "user5", "11111"));
        users.put(6L, new User(6L, "Джанет Вэрни", "user6", "11111"));
        users.put(7L, new User(7L, "Брэндон Смит", "user7", "11111"));
        userIdCount = new AtomicLong(7L);
    }

    @Override
    public Optional<User> findById(long id) {
        return Optional.ofNullable(users.get(id));
    }

    @Override
    public Optional<User> findRandomUser() {
        Random r = new Random();
        return users.values().stream().skip(r.nextInt(users.size() - 1)).findFirst();
    }

    @Override
    public Optional<User> findByLogin(String login) {
        return users.values().stream().filter(v -> v.getLogin().equals(login)).findFirst();
    }

    @Override
    public long insertUser(User user) {
        long id = userIdCount.incrementAndGet();
        user.setId(id);
        users.put(id, user);
        return id;
    }

    @Override
    public void updateUser(User user) {
        Long id = user.getId();
        if (users.containsKey(id)) {
            users.put(id, user);
        } else {
            throw new IllegalArgumentException(format("User with id=%d does not exist", id));
        }
    }

    @Override
    public long insertOrUpdate(User user) {
        Long id = user.getId();
        if (id != null) {
            updateUser(user);
            return id;
        } else {
            return insertUser(user);
        }
    }

    @Override
    public List<User> findAllUsers() {
        return new ArrayList<>(users.values());
    }

    @Override
    public SessionManager getSessionManager() {
        return null;
    }
}
