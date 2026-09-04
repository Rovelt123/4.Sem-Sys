package app.enums;


import lombok.Getter;

@Getter
public enum Notifications {
    GET_ALL_EMPTY("No data was fetched because %s was empty!"),
    MUST_BE_INT("Input must be a number! You entered: %s"),
    GET_BY_ID("You fetched %s with ID: %s"),
    GET_ALL("You fetched %s %ss");

    // ________________________________________________________

    private final String displayName;

    // ________________________________________________________

    Notifications(String displayName) {
        this.displayName = displayName;
    }
}
