package app.services;

import app.entities.User;
import app.services.mail.BrevoMailSender;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

/*
At the moment we can only test the API calls not the SMTP calls,
therefore the tests only include for BrevoMailSender
*/

class EmailServiceTest {

    private EmailService emailService;

    @BeforeEach
    void setUp() {

        emailService = new EmailService(new BrevoMailSender());
    }

    @Test
    void sendConfirmationEmail() {

        User user = new User();
        user.setEmail("olivermjmj@outlook.dk");
        user.setEmailConfirmationToken("test-confirmation-token-123");

        assertDoesNotThrow(() ->
                emailService.sendConfirmationEmail(user)
        );
    }

    @Test
    void sendForgotPasswordEmail() {

        String recipient = "olivermjmj@outlook.dk";
        String token = "test-token-123";

        assertDoesNotThrow(() ->
                emailService.sendForgotPasswordEmail(recipient, token)
        );
    }
}