package boardkata.sever.domain.comment;

import boardkata.sever.exception.CommentNotRootException;
import boardkata.sever.exception.CommentAndReplyNotSameBoardException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CommentTest {

    private Comment aComment() {
        return Comment.builder()
                .content("aComment")
                .authorId(1L)
                .boardId(1L)
                .build();
    }

    @Nested
    @DisplayName("addReply 메소드는")
    class Describe_addReply {

        @Nested
        @DisplayName("올바른 reply가 주어지면")
        class Context_with_right_reply {

            Comment comment;

            Comment reply;

            @BeforeEach
            void setUp() {
                comment = Comment.builder()
                        .content("amumu")
                        .authorId(1L)
                        .boardId(1L)
                        .build();

                reply = Comment.builder()
                        .content("reply")
                        .authorId(1L)
                        .boardId(1L)
                        .build();
            }

            @Test
            @DisplayName("replies에 reply가 추가된다.")
            void it_added_reply_to_replies() {
                comment.addReply(reply);

                List<Comment> replies = comment.getReplies();

                assertThat(replies).hasSize(1);
                assertThat(replies).contains(reply);
            }
        }

        @Nested
        @DisplayName("root가 아닌 Comment가 주어지면")
        class Context_with_not_root_comment {

            Comment notRootComment;

            Comment reply;

            @BeforeEach
            void setUp() {
                Comment rootComment = aComment();

                notRootComment = aComment();

                rootComment.addReply(notRootComment);

                reply = aComment();
            }

            @Test
            @DisplayName("CommentNotRootException 예외를 던진다.")
            void it_throws_CommentNotRootException() {
                assertThatThrownBy(() -> notRootComment.addReply(reply))
                        .isInstanceOf(CommentNotRootException.class);
            }
        }

        @Nested
        @DisplayName("comment와 reply가 같은 board가 아니면")
        class Context_with_not_same_board {

            Comment comment;

            Comment notSameBoardReply;

            @BeforeEach
            void setUp() {
                comment = Comment.builder()
                        .content("amumu")
                        .authorId(1L)
                        .boardId(1L)
                        .build();

                notSameBoardReply = Comment.builder()
                        .content("reply")
                        .authorId(1L)
                        .boardId(2L)
                        .build();
            }

            @Test
            @DisplayName("ReplyNotSameBoard 예외를 던진다.")
            void it_throws_ReplyNotSameBoard() {
                assertThatThrownBy(() -> comment.addReply(notSameBoardReply))
                        .isInstanceOf(CommentAndReplyNotSameBoardException.class);
            }
        }
    }
}
