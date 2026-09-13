package app.enums;


import lombok.Getter;

@Getter
public enum Notifications {

    // LOGIN
    LOGGED_IN("Welcome back %s!"),
    WRONG_CREDENTIALS("You entered the wrong credentials!"),


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
