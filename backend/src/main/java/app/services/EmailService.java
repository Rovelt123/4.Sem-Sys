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

        String resetUrl = frontendUrl + "/reset-password?token=" + token;

        String subject = "Nulstil din adgangskode";

        String body = """
                Hej,
                
                Vi har modtaget en anmodning om at nulstille din adgangskode.
                
                Klik på linket for at vælge en ny adgangskode:
                
                %s
                
                Hvis du ikke har bedt om det, så kan du ignorere det mailen.
                """.formatted(resetUrl);

        mailSender.sendMail(email, subject, body);
    }
}
