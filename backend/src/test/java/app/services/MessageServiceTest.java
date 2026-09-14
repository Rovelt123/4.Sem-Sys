package app.services;

import app.enums.Notifications;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MessageServiceTest {

    private final MessageService messageService = new MessageService();

    @Test
    void buildMessageShouldFormatNotificationWithArguments() {
        String message = messageService.buildMessage(Notifications.GET_BY_ID, "user", "123");

        assertEquals("You fetched user with ID: 12r3", message);
    }

    @Test
    void buildMessageShouldFormatEmptyGetAllMessage() {
        String message = messageService.buildMessage(Notifications.GET_ALL_EMPTY, "user");

        assertEquals("No data was fetched because user was empty!", message);
    }
}
