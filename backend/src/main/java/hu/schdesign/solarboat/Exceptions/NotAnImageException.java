package hu.schdesign.solarboat.Exceptions;

public class NotAnImageException extends RuntimeException{

        public NotAnImageException(String message) {
            super(message);
        }

        public NotAnImageException(String message, Throwable cause) {
            super(message, cause);
        }

    }
