package app.services.mail;

import app.utils.Utils;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Map;

public class BrevoMailSender implements MailSender {

    private final String apiKey = System.getenv("BREVO_API_KEY");
    private final String emailSender = Utils.getPropertyValue("email.sender", "config.properties");

    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final ObjectMapper objectMapper = new ObjectMapper();

    // ________________________________________________________

    @Override
    public void sendMail(
            String recipient,
            String subject,
            String body
    ) {

        Map<String, Object> emailData = Map.of("sender", Map.of("email", emailSender),
                "to", List.of(Map.of("email", recipient)),
                "subject", subject, "textContent", body);

        try {
            String jsonBody = objectMapper.writeValueAsString(emailData);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://api.brevo.com/v3/smtp/email"))
                    .header("accept", "application/json")
                    .header("api-key", apiKey)
                    .header("content-type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                    .build();

            HttpResponse<String> response = httpClient.send(
                    request,
                    HttpResponse.BodyHandlers.ofString()
            );

            if (response.statusCode() < 200 || response.statusCode() >= 300) {

                throw new RuntimeException(
                        "Kunne ikke sende email. Brevo svarede med status: "
                                + response.statusCode()
                                + " : "
                                + response.body()
                );
            }

        } catch (IOException e) {
            throw new RuntimeException("Der opstod en fejl under afsendelse af email", e);

        } catch (InterruptedException e) {

            Thread.currentThread().interrupt();

            throw new RuntimeException("Email afsendelsen blev afbrudt", e);
        }
    }
}