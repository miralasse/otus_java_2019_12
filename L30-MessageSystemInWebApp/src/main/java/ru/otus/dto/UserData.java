package ru.otus.dto;

import lombok.Data;
import ru.otus.messagesystem.client.ResultDataType;

@Data
public class UserData extends ResultDataType {
    private final String data;
}
