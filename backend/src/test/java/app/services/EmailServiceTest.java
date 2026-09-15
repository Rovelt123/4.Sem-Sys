package app.services;

import app.entities.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class EmailServiceTest {

    private final List<SentMail> sentMails = new ArrayList<>();
    private EmailService emailService;

    @BeforeEach
    void setUp() {
        emailService = new EmailService(
                (recipient, subject, body) -> sentMails.add(new SentMail(recipient, subject, body)),
                "https://frontend.example.test"
        );
    }

    @Test
    void sendConfirmationEmail() {
        User user = new User();
        user.setEmail("user@example.test");
        user.setEmailConfirmationToken("test-confirmation-token-123");

        emailService.sendConfirmationEmail(user);

        assertEquals(1, sentMails.size());
        SentMail mail = sentMails.getFirst();
        assertEquals(user.getEmail(), mail.recipient());
        assertEquals("Confirm your email", mail.subject());
        assertTrue(mail.body().contains(
                "https://frontend.example.test/confirm-email?token=test-confirmation-token-123"));
    }

    @Test
    void sendForgotPasswordEmail() {
        emailService.sendForgotPasswordEmail("user@example.test", "test-token-123");

        assertEquals(1, sentMails.size());
        SentMail mail = sentMails.getFirst();
        assertEquals("user@example.test", mail.recipient());
        assertEquals("Reset your password", mail.subject());
        assertTrue(mail.body().contains(
                "https://frontend.example.test/reset-password?token=test-token-123"));
    }

    private record SentMail(String recipient, String subject, String body) { }
}
