package ru.otus.front.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import ru.otus.front.services.FrontendService;

@Slf4j
@Controller
@RequiredArgsConstructor
public class UserController {

    private final FrontendService frontendService;
    private final SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/getAllUsers")
    @SendTo("/topic/allUsers")
    public void getAllUsers() {
        frontendService.getAllUsers(data ->
        {
            log.info("Get all users: {}", data.getData());
            messagingTemplate.convertAndSend("/topic/allUsers", data.getData());
        });
    }

    @MessageMapping("/createUser")
    @SendTo("/topic/user")
    public void createUser(String userJson) {
        frontendService.saveUser(userJson, data ->
        {
            log.info("created user with id = {}", data.getData());
        });
    }
}
