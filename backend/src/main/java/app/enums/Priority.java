package app.enums;

import lombok.Getter;

@Getter
public enum Priority {


    LOW("Low priority"),
    MEDIUM("Medium priority"),
    HIGH("High priority");

    // ________________________________________________________

    private final String displayName;

    // ________________________________________________________

    Priority(String displayName) {
        this.displayName = displayName;
    }
}
