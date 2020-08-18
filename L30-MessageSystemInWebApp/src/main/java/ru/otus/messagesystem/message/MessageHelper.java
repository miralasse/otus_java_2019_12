package ru.otus.messagesystem.message;

import lombok.experimental.UtilityClass;

@UtilityClass
public class MessageHelper {

    public static <T> T getPayload(Message msg) {
        return (T) Serializers.deserialize(msg.getPayload());
    }

    public static byte[] serializeMessage(Message msg) {
        return Serializers.serialize(msg);
    }

    public static Message deSerializeMessage(byte[] bytes) {
        return (Message) Serializers.deserialize(bytes);
    }
}
