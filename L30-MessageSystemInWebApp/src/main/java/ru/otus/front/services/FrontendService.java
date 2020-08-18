package ru.otus.front.services;

import ru.otus.dto.UserData;
import ru.otus.messagesystem.client.MessageCallback;

public interface FrontendService {

    void getAllUsers(MessageCallback<UserData> dataConsumer);

    void saveUser(String userJson, MessageCallback<UserData> dataConsumer);
}

