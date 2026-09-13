package app.services;

import app.entities.User;
import app.services.mail.MailSender;
import app.utils.Utils;

public class EmailService {

    private final String frontendUrl = Utils.getPropertyValue("url", "config.properties");
    private final MailSender mailSender;

    // ________________________________________________________

    public EmailService(MailSender mailSender) {

        this.mailSender = mailSender;
    }

    // ________________________________________________________

    public void sendConfirmationEmail(User user) {

        String confirmationUrl = frontendUrl+ "/confirm-email?token=" + user.getEmailConfirmationToken();

        String subject = "Bekræft din email";

        String body = """
                Hej,

                Tak for din registrering.

                Bekræft din email ved at klikke på linket:

                %s

                Linket udløber efter 24 timer.
                """.formatted(confirmationUrl);

        mailSender.sendMail(
                user.getEmail(),
                subject,
                body
        );
    }

    // ________________________________________________________

    public void sendForgotPasswordEmail(String email, String token) {

        //TODO make a solution such that the email will carry the JWTToken.
    }
}