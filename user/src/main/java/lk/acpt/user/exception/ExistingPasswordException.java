package lk.acpt.user.exception;

public class ExistingPasswordException extends RuntimeException {
    public ExistingPasswordException(String message) {
        super(message);
    }
}
