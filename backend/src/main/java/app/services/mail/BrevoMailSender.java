package app.services.mail;

import app.utils.Utils;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.List;
import java.util.Map;

public class BrevoMailSender implements MailSender {

    private final String apiKey = System.getenv("BREVO_API_KEY");
    private final String emailSender = Utils.getPropertyValue("email.sender", "config.properties");

    private final HttpClient httpClient = HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(10)).build();
    private final ObjectMapper objectMapper = new ObjectMapper();

    // ________________________________________________________

    @Override
    public void sendMail(String recipient, String subject, String body) {

        if (apiKey == null || apiKey.isEmpty() ||apiKey.isBlank()) {

            throw new IllegalStateException("Error The BREVO_API_KEY is not set.");
        }

        Map<String, Object> emailData = Map.of("sender", Map.of("email", emailSender),
                "to", List.of(Map.of("email", recipient)),
                "subject", subject, "textContent", body);

        try {
            String jsonBody = objectMapper.writeValueAsString(emailData);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://api.brevo.com/v3/smtp/email"))
                    .timeout(Duration.ofSeconds(20))
                    .header("accept", "application/json")
                    .header("api-key", apiKey)
                    .header("content-type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() < 200 || response.statusCode() >= 300) {

                throw new RuntimeException(
                        "Error: Could not send the email. Brevo status: "
                                + response.statusCode()
                                + " : "
                                + response.body()
                );
            }

        } catch (IOException e) {
            throw new RuntimeException("Error occurred while sending the email : ", e);

        } catch (InterruptedException e) {

            Thread.currentThread().interrupt();

            throw new RuntimeException("Error email sending was interrupted : ", e);
        }
    }
}