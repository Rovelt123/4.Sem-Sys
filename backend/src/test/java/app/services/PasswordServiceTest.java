package app.services;

import app.exceptions.ApiException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PasswordServiceTest {

    @Test
    void validPasswordShouldNotThrowApiException() {
        assertDoesNotThrow(() ->
                PasswordService.passwordValidation("Password1!")
        );
    }

    @Test
    void passwordTooShortShouldThrowApiException() {
        assertThrows(
                ApiException.class,
                () -> PasswordService.passwordValidation("Ab1!")
        );
    }

    @Test
    void passwordWithoutUppercaseShouldThrowApiException() {
        assertThrows(
                ApiException.class,
                () -> PasswordService.passwordValidation("password1!")
        );
    }

    @Test
    void passwordWithoutLowercaseShouldThrowApiException() {
        assertThrows(
                ApiException.class,
                () -> PasswordService.passwordValidation("PASSWORD1!")
        );
    }

    @Test
    void passwordWithoutSpecialCharacterShouldThrowApiException() {
        assertThrows(
                ApiException.class,
                () -> PasswordService.passwordValidation("Password123")
        );
    }
}
