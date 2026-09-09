package app.controllers;

import app.controllers.generic.BaseController;
import app.daos.UserDAO;
import app.dtos.UserDTO;
import app.entities.User;
import app.enums.Notifications;
import app.enums.Role;
import app.mappers.UserMapper;
import app.security.SecurityService;
import app.server.Setup;
import app.services.EmailService;
import app.services.PasswordService;
import app.services.TokenGenerator;
import app.services.UserService;
import app.utils.ErrorHandler;
import io.javalin.apibuilder.EndpointGroup;
import io.javalin.http.Context;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import static io.javalin.apibuilder.ApiBuilder.*;

public class UserController extends BaseController<User, UserDTO> {

    private final UserDAO userDAO = new UserDAO(Setup.em);
    private final UserMapper userMapper = new UserMapper();
    private final SecurityService securityService = new SecurityService();
    private final UserService userService = new UserService();
    private final TokenGenerator tokenGenerator = new TokenGenerator();
    private final EmailService emailService = new EmailService();

    // ________________________________________________________

    public UserController() {
        super(User.class, new UserMapper());
    }

    // ________________________________________________________

    public static EndpointGroup registerRoutes() {
        UserController controller = new UserController();
        return ()->{

            post("users/auth/register", controller::registerUser, Role.ANYONE);
            post("users/auth/login", controller::login, Role.ANYONE);
            get("users/auth/confirm-email", controller::confirmMail);

            get("/users", controller::getAll, Role.USER);
            get("/user/{id}", controller::getByID, Role.USER);
        };
    }

    // ________________________________________________________

    @Override
    protected List<User> getAllEntities() {
        return userDAO.getAll();
    }

    // ________________________________________________________

    @Override
    protected User getEntityById(UUID id) {
        return userDAO.getById(id);
    }

    // ________________________________________________________

    private void registerUser(Context ctx) {
        Map<String, String> body = ErrorHandler.tryBodyMap(ctx, Notifications.BODY_EMPTY.getDisplayName());

        String firstname = ErrorHandler.tryString(body.get("first_name"), Notifications.REGISTER_NO_FIRSTNAME.getDisplayName());
        String lastname = ErrorHandler.tryString(body.get("last_name"), Notifications.REGISTER_NO_LASTNAME.getDisplayName());
        String email = ErrorHandler.tryString(body.get("email"), Notifications.REGISTER_NO_EMAIL.getDisplayName());
        String password = ErrorHandler.tryString(body.get("password"), Notifications.REGISTER_NO_PASSWORD.getDisplayName());
        String password_repeat = ErrorHandler.tryString(body.get("repeat_password"), Notifications.REGISTER_NO_PASSWORD_REPEAT.getDisplayName());

        if(!password.equals(password_repeat)){
            ctx.status(400).json(Notifications.REGISTER_PASSWORD_MISMATCH.getDisplayName());
            return;
        }

        Role role = Role.USER;

        if (userDAO.existByColumn(email, "username") || !email.contains("@")) {
            String message = messageService.buildMessage(Notifications.USERNAME_EXISTS, email);
            ctx.status(400).json(message);
            return;
        }

        String token = tokenGenerator.generateToken();

        User user = ErrorHandler.tryEntity(
                userDAO.create(User.builder()
                        .firstname(firstname)
                        .lastname(lastname)
                        .roles(Set.of(role))
                        .email(email)
                        .password(PasswordService.hashHelper(password))
                        .emailConfirmationToken(token)
                        .emailConfirmationExpiresAt(LocalDateTime.now())
                        .build()),
                messageService.buildMessage(Notifications.USERNAME_EXISTS, email)
        );

        emailService.sendConfirmationEmail(user);

        UserDTO dto = userMapper.toDTO(user);

        String jwtToken = securityService.createToken(dto);

        String message = messageService.buildMessage(Notifications.REGISTER_SUCCESS, user.getFirstname());

        respond(ctx, 201, message, Map.of(
                "token", jwtToken,
                "data", dto
        ));
    }

    // ________________________________________________________

    private void confirmMail(Context ctx){

        String token = ctx.queryParam("token");

        if (token == null || token.isBlank()) {
            String message = Notifications.TOKEN_MISSING.getDisplayName();
            respond(ctx, 400, message, null);
            return;
        }

        boolean confirmed = userService.confirmEmail(token);

        if (!confirmed) {
            String message = Notifications.LINK_EXPIRED.getDisplayName();
            respond(ctx, 400, message, null);
            return;
        }

        String message = Notifications.EMAIL_CONFIRMED.getDisplayName();
        respond(ctx, 200, message, null);
    }

    // ________________________________________________________

    private void login(Context ctx) {
        Map<String, String> body = ErrorHandler.tryBodyMap(ctx, Notifications.BODY_EMPTY.getDisplayName());
        User user = ErrorHandler.tryEntity(
                userDAO.getByUsername(body.get("username")),
                Notifications.WRONG_CREDENTIALS.getDisplayName()
        );

        if (!PasswordService.passwordEquals(body.get("password"), user.getPassword())) {
            respond(ctx, 401, Notifications.WRONG_CREDENTIALS.getDisplayName(), null);
            return;
        }

        UserDTO dto = userMapper.toDTO(user);

        String token = securityService.createToken(dto);

        String message = messageService.buildMessage(
                Notifications.LOGGED_IN,
                user.getFirstname()
        );

        respond(ctx, 200, message, Map.of(
                "token", token,
                "data", dto
        ));
    }

}
