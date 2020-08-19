package ru.otus.db.handlers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import ru.otus.db.services.DBServiceUser;
import ru.otus.dto.UserData;
import ru.otus.messagesystem.RequestHandler;
import ru.otus.messagesystem.message.Message;
import ru.otus.messagesystem.message.MessageBuilder;
import ru.otus.messagesystem.message.MessageHelper;
import ru.otus.messagesystem.message.MessageType;
import ru.otus.model.User;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class SaveUserRequestHandler implements RequestHandler<UserData> {
    private final DBServiceUser dbServiceUser;
    private final ObjectMapper objectMapper;

    @SneakyThrows
    @Override
    public Optional<Message> handle(Message msg) {
        UserData userData = MessageHelper.getPayload(msg);
        User user = objectMapper.readValue(userData.getData(), new TypeReference<User>() {
        });
        long id = dbServiceUser.saveUser(user);
        UserData data = new UserData(objectMapper.writeValueAsString(id));
        return Optional.of(MessageBuilder.buildReplyMessage(msg, data, MessageType.CREATE_USER));
    }
}
