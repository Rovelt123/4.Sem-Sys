package app.enums;


import lombok.Getter;

@Getter
public enum Notifications {

    // LOGIN
    LOGGED_IN("Welcome back %s!"),
    WRONG_CREDENTIALS("You entered the wrong credentials!"),
    PASSWORD_CHANGED("Password was changed successfully"),
    PASSWORD_RESET_REQUESTED("If the email exists, a password reset link has been sent"),
    PASSWORD_RESET_SUCCESS("Password has been reset successfully"),
    PASSWORD_CURRENT_MISSING("You must enter your current password"),
    PASSWORD_CURRENT_WRONG("Current password is wrong"),
    PASSWORD_NEW_MISSING("You must enter a new password"),
    PASSWORD_REPEAT_MISSING("You must repeat your new password"),
    PASSWORD_UNCHANGED("New password must be different from current password"),

    // VALIDATE PASSWORD
    PASSWORD_LENGTH_INVALID("The password must be between 8 and 30 characters"),
    PASSWORD_LOWERCASE_MISSING("The password must contain a lowercase letter"),
    PASSWORD_UPPERCASE_MISSING("The password must contain an uppercase letter"),
    PASSWORD_SPECIAL_CHAR_MISSING("The password must contain a special character"),


    // REGISTER
    EMAIL_EXISTS("Email: %s already exists! Choose another email"),
    REGISTER_SUCCESS("Welcome to Wedding planner %s! We hope you will enjoy the site"),
    REGISTER_NO_EMAIL("You must enter a valid email"),
    REGISTER_NO_PASSWORD("You must enter a valid password"),
    REGISTER_NO_PASSWORD_REPEAT("You must verify your password"),
    REGISTER_PASSWORD_MISMATCH("Password confirmation does not match"),
    REGISTER_NO_FIRSTNAME("You must enter your first name"),
    REGISTER_NO_LASTNAME("You must enter your last name"),
    TOKEN_MISSING("Token is missing"),
    LINK_EXPIRED("Link is expired or invalid"),
    EMAIL_CONFIRMED("Email has been confirmed"),


    // GENERICS
    GET_ALL_EMPTY("No data was fetched because %s was empty!"),
    GET_BY_ID("You fetched %s with ID: %s"),
    GET_ALL("You fetched %s %ss"),
    BODY_EMPTY("Body is empty or invalid!")

    ;

    // ________________________________________________________

    private final String displayName;

    // ________________________________________________________

    Notifications(String displayName) {
        this.displayName = displayName;
    }
}
