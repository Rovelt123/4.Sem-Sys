package app.services;

import app.daos.UserDAO;
import app.entities.User;
import app.server.Setup;

import java.time.LocalDateTime;

public class UserService {

    private final UserDAO userDAO = new UserDAO(Setup.em);

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
}
