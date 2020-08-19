package ru.otus.messagesystem.message;

import lombok.experimental.UtilityClass;
import ru.otus.messagesystem.client.CallbackId;
import ru.otus.messagesystem.client.ResultDataType;

import java.util.UUID;

@UtilityClass
public class MessageBuilder {
    private static final Message VOID_MESSAGE =
            new Message(new MessageId(UUID.randomUUID().toString()), null, null,
                    null, "voidTechnicalMessage", new byte[1], null);

    public static Message getVoidMessage() {
        return VOID_MESSAGE;
    }

    public static <T extends ResultDataType> Message buildMessage(String from, String to, MessageId sourceMessageId,
                                                                  T data, MessageType msgType) {
        return buildMessage(from, to, sourceMessageId, data, msgType, null);
    }

    public static <T extends ResultDataType> Message buildReplyMessage(Message message, T data, MessageType msgType) {
        return buildMessage(message.getTo(), message.getFrom(), message.getId(), data,
                msgType, message.getCallbackId());
    }

    private static <T extends ResultDataType> Message buildMessage(String from, String to, MessageId sourceMessageId,
                                                                   T data, MessageType msgType, CallbackId callbackId) {
        String id = UUID.randomUUID().toString();
        return new Message(new MessageId(id), from, to, sourceMessageId, msgType.getName(),
                Serializers.serialize(data), callbackId == null ? new CallbackId(id) : callbackId);
    }
}
