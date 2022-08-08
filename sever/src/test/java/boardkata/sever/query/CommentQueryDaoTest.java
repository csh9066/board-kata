package boardkata.sever.query;

import boardkata.sever.JpaConfig;
import boardkata.sever.domain.board.Board;
import boardkata.sever.domain.comment.Comment;
import boardkata.sever.domain.user.User;
import boardkata.sever.dto.comment.CommentDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import javax.persistence.EntityManager;
import java.util.List;

import static boardkata.sever.Fixtures.aBoard;
import static boardkata.sever.Fixtures.aUser;
import static org.assertj.core.api.Assertions.assertThat;

@Import({JpaConfig.class, CommentQueryDao.class})
@DataJpaTest
class CommentQueryDaoTest {

    @Autowired
    EntityManager em;

    @Autowired
    CommentQueryDao commentQueryDao;

    private Board subject() {
        // given
        User user = aUser();
        em.persist(user);

        Board board = aBoard();
        em.persist(board);

        Comment comment1 = Comment.builder()
                .authorId(user.getId())
                .boardId(board.getId())
                .content("content1")
                .build();

        Comment comment2 = Comment.builder()
                .authorId(user.getId())
                .boardId(board.getId())
                .content("content2")
                .build();

        em.persist(comment1);
        em.persist(comment2);
        return board;
    }

    @Test
    void findComments() {
        Board board = subject();

        // when
        List<CommentDto> comments = commentQueryDao.findComments(board.getId());

        // then
        assertThat(comments).hasSize(2);
        assertThat(comments)
                .extracting("content")
                .containsExactly("content1", "content2");
    }

    @Test
    void findCommentsWithUnregisteredCommentBoardId() {
        subject();
        Long unregisteredCommentBoardId = 100L;
        // when
        List<CommentDto> comments = commentQueryDao.findComments(unregisteredCommentBoardId);
        // then
        assertThat(comments).hasSize(0);
    }
}
