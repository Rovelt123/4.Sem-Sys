package app.services;

import app.entities.User;
import app.utils.Utils;

public class EmailService {

    private final String frontendUrl = Utils.getPropertyValue("URL", "config.properties");

    // ________________________________________________________

    public EmailService(){};

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

        sendEmail(
                user.getEmail(),
                subject,
                body
        );
    }

    // ________________________________________________________

    private void sendEmail(
            String recipient,
            String subject,
            String body
    ) {
        //TODO: Sæt vores SMTP mail system op
    }
}