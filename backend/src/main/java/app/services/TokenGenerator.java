package app.services;

import java.security.SecureRandom;
import java.util.Base64;

public class TokenGenerator {

    private final SecureRandom secureRandom = new SecureRandom();

    // ________________________________________________________

    public String generateToken() {
        byte[] bytes = new byte[32];
        secureRandom.nextBytes(bytes);

        return Base64.getUrlEncoder()
                .withoutPadding()
                .encodeToString(bytes);
    }

}