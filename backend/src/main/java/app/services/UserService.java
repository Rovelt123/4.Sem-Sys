package app.services;

import app.daos.UserDAO;
import app.entities.User;
import app.enums.Notifications;
import app.exceptions.ApiException;
import app.server.Setup;
import app.services.mail.BrevoMailSender;
import app.utils.ErrorHandler;

import java.time.LocalDateTime;
import java.util.UUID;

public class UserService {

    private final UserDAO userDAO;
    private final TokenGenerator tokenGenerator;

    public UserService() {
        this(new UserDAO(Setup.em));
    }

    public UserService(UserDAO userDAO) {
        this(userDAO, new TokenGenerator());
    }

    private final EmailService emailService = new EmailService(new BrevoMailSender());

    public UserService(UserDAO userDAO, TokenGenerator tokenGenerator) {
        this.userDAO = userDAO;
        this.tokenGenerator = tokenGenerator;
    }

    // ________________________________________________________

    public boolean confirmEmail(String token) {

        User user = userDAO
                .findByEmailConfirmationToken(token);

        if (user == null) {
            return false;
        }

        if (user.getEmailConfirmationExpiresAt() == null ||
                user.getEmailConfirmationExpiresAt()
                        .isBefore(LocalDateTime.now())) {

            return false;
        }

        user.setEmailConfirmed(true);

        user.setEmailConfirmationToken(null);
        user.setEmailConfirmationExpiresAt(null);

        userDAO.update(user);

        return true;
    }

    // ________________________________________________________

    public void changePassword(UUID userId, String currentPassword, String newPassword, String repeatNewPassword) {
        User user = ErrorHandler.tryEntity(
                userDAO.getById(userId),
                Notifications.WRONG_CREDENTIALS.getDisplayName()
        );

        String current = ErrorHandler.tryString(
                currentPassword,
                Notifications.PASSWORD_CURRENT_MISSING.getDisplayName()
        );
        String password = ErrorHandler.tryString(
                newPassword,
                Notifications.PASSWORD_NEW_MISSING.getDisplayName()
        );
        String repeatedPassword = ErrorHandler.tryString(
                repeatNewPassword,
                Notifications.PASSWORD_REPEAT_MISSING.getDisplayName()
        );

        if (!password.equals(repeatedPassword)) {
            throw new ApiException(400, Notifications.REGISTER_PASSWORD_MISMATCH.getDisplayName());
        }

        if (!PasswordService.passwordEquals(current, user.getPassword())) {
            throw new ApiException(401, Notifications.PASSWORD_CURRENT_WRONG.getDisplayName());
        }

        if (PasswordService.passwordEquals(password, user.getPassword())) {
            throw new ApiException(400, Notifications.PASSWORD_UNCHANGED.getDisplayName());
        }

        user.setPassword(PasswordService.hashHelper(password));
        userDAO.update(user);
    }

    // ________________________________________________________

    public void requestForgotPassword(String email) {
        String requestedEmail = ErrorHandler.tryString(
                email,
                Notifications.REGISTER_NO_EMAIL.getDisplayName()
        );

        User user = userDAO.getByEmail(requestedEmail);

        if (user == null) {
            return;
        }

        String token = tokenGenerator.generateToken();

        user.setPasswordResetToken(token);
        user.setPasswordResetExpiresAt(LocalDateTime.now().plusHours(1));
        userDAO.update(user);

        emailService.sendForgotPasswordEmail(user.getEmail(), token);
    }

    // ________________________________________________________

    public void forgotPassword(String token, String newPassword, String repeatNewPassword) {
        String resetToken = ErrorHandler.tryString(
                token,
                Notifications.TOKEN_MISSING.getDisplayName()
        );
        String password = ErrorHandler.tryString(
                newPassword,
                Notifications.PASSWORD_NEW_MISSING.getDisplayName()
        );
        String repeatedPassword = ErrorHandler.tryString(
                repeatNewPassword,
                Notifications.PASSWORD_REPEAT_MISSING.getDisplayName()
        );

        if (!password.equals(repeatedPassword)) {
            throw new ApiException(400, Notifications.REGISTER_PASSWORD_MISMATCH.getDisplayName());
        }

        User user = userDAO.findByPasswordResetToken(resetToken);

        if (user == null ||
                user.getPasswordResetExpiresAt() == null ||
                user.getPasswordResetExpiresAt().isBefore(LocalDateTime.now())) {

            throw new ApiException(400, Notifications.LINK_EXPIRED.getDisplayName());
        }

        if (PasswordService.passwordEquals(password, user.getPassword())) {
            throw new ApiException(400, Notifications.PASSWORD_UNCHANGED.getDisplayName());
        }

        user.setPassword(PasswordService.hashHelper(password));
        user.setPasswordResetToken(null);
        user.setPasswordResetExpiresAt(null);

        userDAO.update(user);
    }

    // ________________________________________________________

    public void resendConfirmationEmail(String email) {

        String requestedEmail = ErrorHandler.tryString(
                email,
                Notifications.REGISTER_NO_EMAIL.getDisplayName()
        );

        User user = userDAO.getByEmail(requestedEmail);

        if (user == null || user.isEmailConfirmed()) {
            return;
        }

        user.setEmailConfirmationToken(tokenGenerator.generateToken());
        user.setEmailConfirmationExpiresAt(LocalDateTime.now().plusHours(24));

        userDAO.update(user);

        emailService.sendConfirmationEmail(user);
    }
}
