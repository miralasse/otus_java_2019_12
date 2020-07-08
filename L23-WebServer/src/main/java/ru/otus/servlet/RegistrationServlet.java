package ru.otus.servlet;

import ru.otus.model.User;
import ru.otus.services.DBServiceUser;
import ru.otus.services.TemplateProcessor;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;

public class RegistrationServlet extends HttpServlet {

    private static final String USER_NAME_PARAMETER = "name";
    private static final String USER_LOGIN_PARAMETER = "login";
    private static final String USER_PASSWORD_PARAMETER = "password";

    private static final String REGISTRATION_PAGE_TEMPLATE = "registration.html";
    private static final String REDIRECT_URL = "/users";


    private final TemplateProcessor templateProcessor;
    private final DBServiceUser userService;

    public RegistrationServlet(TemplateProcessor templateProcessor, DBServiceUser userService) {
        this.userService = userService;
        this.templateProcessor = templateProcessor;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        response.getWriter().println(templateProcessor.getPage(REGISTRATION_PAGE_TEMPLATE, Collections.emptyMap()));
    }


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

        String userName = request.getParameter(USER_NAME_PARAMETER);
        String userLogin = request.getParameter(USER_LOGIN_PARAMETER);
        String userPassword = request.getParameter(USER_PASSWORD_PARAMETER);

        User newUser = new User(userName, userLogin, userPassword);
        userService.saveUser(newUser);
        response.sendRedirect(REDIRECT_URL);
    }

}
