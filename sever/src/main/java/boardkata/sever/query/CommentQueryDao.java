package boardkata.sever.query;

import boardkata.sever.dto.comment.CommentDto;
import boardkata.sever.dto.comment.QCommentDto;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

import static boardkata.sever.domain.comment.QComment.comment;
import static boardkata.sever.domain.user.QUser.user;

@RequiredArgsConstructor
@Component
public class CommentQueryDao {

    private final JPAQueryFactory jpaQueryFactory;

    public List<CommentDto> findComments(Long boardId) {
        return jpaQueryFactory
                .select(new QCommentDto(comment, user))
                .from(comment)
                .join(user).on(comment.authorId.eq(user.id))
                .where(comment.boardId.eq(boardId))
                .fetch();
    }
}
