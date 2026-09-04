package app.exceptions;

import lombok.Getter;

/**
 * Purpose: To handle No authorized exceptions in the API
 * Author: Thomas Hartmann
 */
@Getter
public class NotAuthorizedException extends Exception {
    private final int statusCode;

    public NotAuthorizedException(int statusCode, String message) {
        super(message);
        this.statusCode = statusCode;
    }

}
