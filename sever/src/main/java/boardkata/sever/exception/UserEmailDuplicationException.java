package boardkata.sever.exception;

public class UserEmailDuplicationException extends RuntimeException {

    public UserEmailDuplicationException(String message) {
        super(message);
    }
}
