package ru.otus.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.view.RedirectView;
import ru.otus.model.User;
import ru.otus.services.DBServiceUser;

import java.util.List;


@Controller
@RequiredArgsConstructor
public class UserController {

    private final DBServiceUser userService;

    @GetMapping({"/", "/users"})
    public String userListView(Model model) {
        List<User> users = userService.findAllUsers();
        model.addAttribute("users", users);
        model.addAttribute("newUser", new User());
        return "users.html";
    }

    @PostMapping("/users/save")
    public RedirectView userSave(@ModelAttribute User user) {
        userService.saveUser(user);
        return new RedirectView("/users", true);
    }

    @GetMapping("/api/user/{id}")
    @ResponseBody
    public User getUserJson(@PathVariable Long id) {
        return userService.findById(id).orElse(null);
    }
}
