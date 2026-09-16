package app.enums;

import lombok.Getter;

@Getter
public enum Categories {

    UNCATEGORIZED("Uncategorized"),
    CATERING("Catering"),
    DRINKS("Alcohol & soft drinks"),
    SNACKS("Snacks");

    // ________________________________________________________

    private final String displayName;

    // ________________________________________________________

    Categories(String displayName) {
        this.displayName = displayName;
    }
}
