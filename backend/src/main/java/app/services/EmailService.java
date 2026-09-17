package app.services;

import app.entities.User;
import app.services.mail.MailSender;
import app.utils.Utils;

public class EmailService {

    private final String frontendUrl;
    private final MailSender mailSender;

    // ________________________________________________________

    public EmailService(MailSender mailSender) {

        this(mailSender, Utils.getPropertyValue("FRONTEND", "config.properties"));
    }

    // ________________________________________________________

    public EmailService(MailSender mailSender, String frontendUrl) {

        this.mailSender = mailSender;
        this.frontendUrl = frontendUrl;
    }

    // ________________________________________________________

    public void sendConfirmationEmail(User user) {

        String confirmationUrl = frontendUrl+ "/confirm-email?token=" + user.getEmailConfirmationToken();

        String subject = "Confirm your email";

        String body = """
                Hi,

                Thank you for registering.

                Confirm your email by clicking the link below:

                %s

                The link expires after 24 hours.
                """.formatted(confirmationUrl);

        mailSender.sendMail(
                user.getEmail(),
                subject,
                body
        );
    }

    // ________________________________________________________

    public void sendForgotPasswordEmail(String email, String token) {

        String resetUrl = frontendUrl + "/reset-password?token=" + token;

        String subject = "Reset your password";

        String body = """
                Hi,

                We received a request to reset your password.

                Click the link below to choose a new one:

                %s

                If you did not ask for this, you can ignore this email.
                """.formatted(resetUrl);

        mailSender.sendMail(email, subject, body);
    }
}
