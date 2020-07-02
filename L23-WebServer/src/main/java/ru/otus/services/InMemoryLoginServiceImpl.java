package ru.otus.services;

import org.eclipse.jetty.security.AbstractLoginService;
import org.eclipse.jetty.util.security.Password;
import ru.otus.model.User;

import java.util.Optional;

public class InMemoryLoginServiceImpl extends AbstractLoginService {

    private final DBServiceUser userService;

    public InMemoryLoginServiceImpl(DBServiceUser userService) {
        this.userService = userService;
    }


    @Override
    protected String[] loadRoleInfo(UserPrincipal userPrincipal) {
        return new String[]{"user"};
    }

    @Override
    protected UserPrincipal loadUserInfo(String login) {
        System.out.println(String.format("InMemoryLoginService#loadUserInfo(%s)", login));
        Optional<User> dbUser = userService.findByLogin(login);
        return dbUser.map(u -> new UserPrincipal(u.getLogin(), new Password(u.getPassword()))).orElse(null);
    }
}
