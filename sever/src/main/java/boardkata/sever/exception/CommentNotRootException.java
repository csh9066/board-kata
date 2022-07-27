package boardkata.sever.exception;

public class CommentNotRootException extends RuntimeException {
    public CommentNotRootException(Long commentId) {
        super(commentId + "는 Root Comment가 아닙니다. Root Comment 에만 Reply를 작성할 수 있습니다.");
    }
}
