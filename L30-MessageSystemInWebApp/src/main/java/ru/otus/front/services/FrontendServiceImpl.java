package ru.otus.front.services;

import lombok.RequiredArgsConstructor;
import ru.otus.dto.UserData;
import ru.otus.messagesystem.client.MessageCallback;
import ru.otus.messagesystem.client.MsClient;
import ru.otus.messagesystem.message.Message;
import ru.otus.messagesystem.message.MessageType;

@RequiredArgsConstructor
public class FrontendServiceImpl implements FrontendService {

    private final MsClient msClient;
    private final String databaseServiceClientName;

    @Override
    public void getAllUsers(MessageCallback<UserData> dataConsumer) {
        Message outMsg = msClient.produceMessage(databaseServiceClientName, null,
                MessageType.GET_ALL_USERS, dataConsumer);
        msClient.sendMessage(outMsg);
    }

    @Override
    public void saveUser(String userJson, MessageCallback<UserData> dataConsumer) {
        Message outMsg = msClient.produceMessage(databaseServiceClientName, new UserData(userJson),
                MessageType.CREATE_USER, dataConsumer);
        msClient.sendMessage(outMsg);
    }
}
