package boardkata.sever.application;

import boardkata.sever.domain.board.BoardRepository;
import boardkata.sever.domain.comment.Comment;
import boardkata.sever.domain.comment.CommentRepository;
import boardkata.sever.dto.comment.CommentCommandDto;
import boardkata.sever.exception.AccessDeniedException;
import boardkata.sever.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@RequiredArgsConstructor
@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final BoardRepository boardRepository;

    public Long createComment(Long boardId, Long authorId, CommentCommandDto commentCommandDto) {
        if (!boardRepository.existsById(boardId)) {
            throw new ResourceNotFoundException("board", "id", boardId);
        }

        Comment comment = Comment.builder()
                .boardId(boardId)
                .authorId(authorId)
                .content(commentCommandDto.getContent())
                .build();

        commentRepository.save(comment);

        return comment.getId();
    }

    public void updateComment(Long commentId, Long userId, CommentCommandDto commentCommandDto) {
        Comment comment = findComment(commentId);

        if (!comment.isAuthor(userId)) {
            throw new AccessDeniedException("접근 권한이 없습니다.");
        }

        comment.update(commentCommandDto.getContent());
    }

    public void deleteComment(Long commentId, Long userId) {
        Comment comment = findComment(commentId);

        if (!comment.isAuthor(userId)) {
            throw new AccessDeniedException("접근 권한이 없습니다.");
        }

        commentRepository.delete(comment);
    }

    private Comment findComment(Long commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(() -> new ResourceNotFoundException("comment", "id", commentId));
    }
}
