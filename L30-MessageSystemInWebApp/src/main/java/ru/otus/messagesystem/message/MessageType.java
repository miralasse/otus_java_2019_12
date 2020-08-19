package ru.otus.messagesystem.message;

public enum MessageType {
    USER_DATA("UserData"),
    GET_ALL_USERS("getAllUsers"),
    CREATE_USER("createUser");

    private final String name;

    MessageType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
