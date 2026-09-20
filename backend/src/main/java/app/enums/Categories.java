package app.enums;

import lombok.Getter;

@Getter
public enum Categories {

    UNCATEGORIZED("Uncategorized"),
    VENUE("Venue"),
    GUESTS("Guests"),
    FOOD_AND_DRINK("Food & drinks"),
    CEREMONY("Ceremony"),
    ATTIRE("Attire"),
    FLOWERS_AND_DECOR("Flowers & decor"),
    ENTERTAINMENT("Entertainment"),
    PAPERWORK("Paperwork");

    // ________________________________________________________

    private final String displayName;

    // ________________________________________________________

    Categories(String displayName) {
        this.displayName = displayName;
    }
}
