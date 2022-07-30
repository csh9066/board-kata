package boardkata.sever.application;

import boardkata.sever.domain.board.Board;
import boardkata.sever.domain.board.BoardRepository;
import boardkata.sever.domain.comment.Comment;
import boardkata.sever.domain.comment.CommentRepository;
import boardkata.sever.dto.comment.CommentCreateDto;
import boardkata.sever.dto.comment.CommentUpdateDto;
import boardkata.sever.exception.AccessDeniedException;
import boardkata.sever.exception.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static boardkata.sever.Fixtures.aBoard;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
class CommentServiceTest {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private BoardRepository boardRepository;

    private CommentService commentService;

    private Board boardSubject() {
        return boardRepository.save(aBoard());
    }

    private Comment commentSubject() {
        Comment comment = Comment.builder()
                .content("title")
                .authorId(1L)
                .boardId(1L)
                .build();

        return commentRepository.save(comment);
    }

    @BeforeEach
    void setUp() {
        commentService = new CommentService(commentRepository, boardRepository);
    }

    @Nested
    @DisplayName("createComment 메소드는")
    class Describe_createComment {

        @Nested
        @DisplayName("올바른 데이터가 주어지면")
        class Context_when_right_arg {
            Long authorId;
            Long boardId;
            CommentCreateDto commentCreateDto;

            @BeforeEach
            void setUp() {
                authorId = 1L;

                boardId = boardSubject().getId();

                commentCreateDto = new CommentCreateDto("content", boardId);
            }

            @Test
            @DisplayName("생성된 boardId를 조회하면 생성된 걸 확인할 수 있다.")
            void it_saved_comment() {
                Long id = commentService.createComment(authorId, commentCreateDto);
                Comment comment = commentRepository.findById(id).get();

                assertThat(comment.getId()).isEqualTo(id);
                assertThat(comment.getContent()).isEqualTo("content");
            }
        }

        @Nested
        @DisplayName("board가 존재하지 않으면")
        class Context_when_not_exists_board {
            Long notExistsBoardId;
            Long authorId;
            CommentCreateDto commentCreateDto;

            @BeforeEach
            void setUp() {
                authorId = 1L;

                notExistsBoardId = 100L;

                commentCreateDto = new CommentCreateDto("content", notExistsBoardId);
            }

            @Test
            @DisplayName("ResourceNotFoundException 예외를 던진다.")
            void it_saved_comment() {
                assertThatThrownBy(() -> commentService
                        .createComment(authorId, commentCreateDto)
                )
                        .isInstanceOf(ResourceNotFoundException.class);
            }
        }
    }

    @Nested
    @DisplayName("updateComment 메소드는")
    class Describe_updateComment {

        @Nested
        @DisplayName("올바른 인자들이 주어지면")
        class Context_with_right_arg {
            Long commentId;
            Long userId;
            CommentUpdateDto commentUpdateDto;

            @BeforeEach
            void setUp() {
                userId = 1L;

                Comment comment = commentSubject();

                commentId = comment.getId();

                commentUpdateDto = new CommentUpdateDto("updatedTitle");
            }

            @Test
            @DisplayName("호출 후 조회하면 Comment가 변경된걸 확인할 수 있다.")
            void it_updates_comment() {
                commentService.updateComment(commentId, userId, commentUpdateDto);

                Comment comment = commentRepository.findById(commentId).get();

                assertThat(comment.getContent()).isEqualTo("updatedTitle");
            }
        }

        @Nested
        @DisplayName("comment가 존재하지 않으면")
        class Context_when_comment_not_exists {
            Long notExistsCommentId;
            Long userId;
            CommentUpdateDto commentUpdateDto;

            @BeforeEach
            void setUp() {
                notExistsCommentId = 100L;

                userId = 1L;

                commentUpdateDto = new CommentUpdateDto("updatedTitle");
            }

            @Test
            @DisplayName("ResourceNotFoundException 예외를 던진다.")
            void it_throws_ResourceNotFoundException() {
                assertThatThrownBy(() -> commentService
                        .updateComment(notExistsCommentId, userId, commentUpdateDto)
                )
                    .isInstanceOf(ResourceNotFoundException.class);
            }
        }

        @Nested
        @DisplayName("변경할 권한이 없으면")
        class Context_when_not_access {
            Long commentId;
            Long notAccessUserId;
            CommentUpdateDto commentUpdateDto;

            @BeforeEach
            void setUp() {
                commentId = commentSubject().getId();

                notAccessUserId = 100L;

                commentUpdateDto = new CommentUpdateDto("updatedTitle");
            }

            @Test
            @DisplayName("AccessDeniedException 예외를 던집니다.")
            void it_throws_AccessDeniedException() {
                assertThatThrownBy(
                        () -> commentService
                                .updateComment(commentId, notAccessUserId, commentUpdateDto)
                )
                        .isInstanceOf(AccessDeniedException.class);
            }
        }
    }

    @Nested
    @DisplayName("deleteComment 메소드는")
    class Describe_deleteComment {

        @Nested
        @DisplayName("올바른 인자들이 주어지면")
        class Context_with_right_arg {
            Long commentId;
            Long userId;

            @BeforeEach
            void setUp() {
                userId = 1L;

                Comment comment = commentSubject();

                commentId = comment.getId();
            }

            @Test
            @DisplayName("호출 후 조회하면 Comment가 삭제된걸 확인할 수 있다.")
            void it_deletes_comment() {
                commentService.deleteComment(commentId, userId);

                boolean empty = commentRepository.findById(commentId).isEmpty();

                assertThat(empty).isTrue();
            }
        }

        @Nested
        @DisplayName("comment가 존재하지 않으면")
        class Context_when_comment_not_exists {
            Long notExistsCommentId;
            Long userId;

            @BeforeEach
            void setUp() {
                notExistsCommentId = 100L;

                userId = 1L;
            }

            @Test
            @DisplayName("ResourceNotFoundException 예외를 던진다.")
            void it_throws_ResourceNotFoundException() {
                assertThatThrownBy(() -> commentService
                        .deleteComment(notExistsCommentId, userId)
                )
                        .isInstanceOf(ResourceNotFoundException.class);
            }
        }

        @Nested
        @DisplayName("변경할 권한이 없으면")
        class Context_when_not_access {
            Long commentId;
            Long notAccessUserId;

            @BeforeEach
            void setUp() {
                commentId = commentSubject().getId();

                notAccessUserId = 100L;
            }

            @Test
            @DisplayName("AccessDeniedException 예외를 던집니다.")
            void it_throws_AccessDeniedException() {
                assertThatThrownBy(
                        () -> commentService
                                .deleteComment(commentId, notAccessUserId)
                )
                        .isInstanceOf(AccessDeniedException.class);
            }
        }
    }
}
