package app.services.mail;


public interface MailSender {

    void sendMail(
            String recipient,
            String subject,
            String body
    );
}
