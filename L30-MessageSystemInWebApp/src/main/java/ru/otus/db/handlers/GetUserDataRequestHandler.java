package ru.otus.db.handlers;

import ru.otus.db.services.DBServiceUser;
import ru.otus.dto.UserData;
import ru.otus.messagesystem.RequestHandler;
import ru.otus.messagesystem.message.Message;
import ru.otus.messagesystem.message.MessageBuilder;
import ru.otus.messagesystem.message.MessageHelper;

import java.util.Optional;


public class GetUserDataRequestHandler implements RequestHandler<UserData> {
    private final DBServiceUser dbServiceUser;

    public GetUserDataRequestHandler(DBServiceUser dbServiceUser) {
        this.dbServiceUser = dbServiceUser;
    }

    @Override
    public Optional<Message> handle(Message msg) {
        UserData userData = MessageHelper.getPayload(msg);
        UserData data = new UserData(userData.getUserId(), dbServiceUser.getUserData(userData.getUserId()));
        return Optional.of(MessageBuilder.buildReplyMessage(msg, data));
    }
}
