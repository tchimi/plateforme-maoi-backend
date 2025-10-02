package maoi.platforme.exception;

public class BearerTokenExpiredException extends RuntimeException {
    public BearerTokenExpiredException(String message) {
        super(message);
    }
}
