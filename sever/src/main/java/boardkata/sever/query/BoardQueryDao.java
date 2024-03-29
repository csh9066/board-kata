package boardkata.sever.query;

import boardkata.sever.dto.PageResponse;
import boardkata.sever.dto.board.BoardDto;
import boardkata.sever.dto.board.QBoardDto;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

import static boardkata.sever.domain.board.QBoard.board;
import static boardkata.sever.domain.user.QUser.user;
import static org.springframework.data.support.PageableExecutionUtils.getPage;

@RequiredArgsConstructor
@Repository
public class BoardQueryDao {

    private final JPAQueryFactory jpaQueryFactory;

    public Page<BoardDto> searchBoards(Pageable pageable) {
        List<BoardDto> result = jpaQueryFactory.select(new QBoardDto(board, user))
                .from(board)
                .join(user).on(user.id.eq(board.authorId))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(board.createdAt.desc())
                .fetch();

        return new PageImpl<>(result, pageable, totalCount());
    }

    private Long totalCount() {
        return jpaQueryFactory.select(board.count())
                .from(board)
                .join(user).on(user.id.eq(board.authorId))
                .fetchOne();
    }
}
