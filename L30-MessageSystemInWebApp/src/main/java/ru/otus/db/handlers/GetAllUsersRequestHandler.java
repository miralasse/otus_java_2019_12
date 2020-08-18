package ru.otus.db.handlers;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import ru.otus.db.services.DBServiceUser;
import ru.otus.dto.UserData;
import ru.otus.messagesystem.RequestHandler;
import ru.otus.messagesystem.message.Message;
import ru.otus.messagesystem.message.MessageBuilder;
import ru.otus.messagesystem.message.MessageType;
import ru.otus.model.User;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class GetAllUsersRequestHandler implements RequestHandler<UserData> {
    private final DBServiceUser dbServiceUser;
    private final ObjectMapper objectMapper;

    @SneakyThrows
    @Override
    public Optional<Message> handle(Message msg) {
        List<User> allUsers = dbServiceUser.findAllUsers();
        UserData data = new UserData(objectMapper.writeValueAsString(allUsers));
        return Optional.of(MessageBuilder.buildReplyMessage(msg, data, MessageType.GET_ALL_USERS));
    }
}
