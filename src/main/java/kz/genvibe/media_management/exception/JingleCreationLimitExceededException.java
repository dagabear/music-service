package kz.genvibe.media_management.exception;

public class JingleCreationLimitExceededException extends RuntimeException {
    public JingleCreationLimitExceededException(String message) {
        super(message);
    }
}
