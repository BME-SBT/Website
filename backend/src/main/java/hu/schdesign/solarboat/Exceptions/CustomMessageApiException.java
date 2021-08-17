package hu.schdesign.solarboat.Exceptions;

public class CustomMessageApiException extends RuntimeException {
    public CustomMessageApiException(String message) {
        super(message);
    }

    public CustomMessageApiException(String message, Throwable cause) {
        super(message, cause);
    }
}
