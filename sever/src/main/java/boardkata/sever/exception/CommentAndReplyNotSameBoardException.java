package boardkata.sever.exception;

public class CommentAndReplyNotSameBoardException extends RuntimeException {
    public CommentAndReplyNotSameBoardException(String message) {
        super("서로 다른 Board 입니다. 같은 Board 에서만 Reply를 작성할 수 있습니다.");
    }
}
