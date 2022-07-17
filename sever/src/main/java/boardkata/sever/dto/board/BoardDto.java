package boardkata.sever.dto.board;

import boardkata.sever.domain.board.Board;
import boardkata.sever.domain.user.User;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class BoardDto {

    private final Long id;

    private final String title;

    private final String content;

    private final Author author;

    private final LocalDateTime createdAt;

    private final LocalDateTime updatedAt;

    @Builder
    public BoardDto(Long id,
                    String title,
                    String content,
                    Author author,
                    LocalDateTime createdAt,
                    LocalDateTime updatedAt) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.author = author;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    @QueryProjection
    public BoardDto(Board board, User user) {
        this.id = board.getId();
        this.title = board.getTitle();
        this.content = board.getContent();
        this.author = new Author(user.getId(), user.getNickname());
        this.createdAt = board.getCreatedAt();
        this.updatedAt = board.getUpdatedAt();
    }

    public static BoardDto of(Board board, User user) {
        return BoardDto.builder()
                .id(board.getId())
                .title(board.getTitle())
                .author(new Author(user.getId(), user.getNickname()))
                .content(board.getContent())
                .createdAt(board.getCreatedAt())
                .updatedAt(board.getUpdatedAt())
                .build();
    }

    @Getter
    public static class Author {
        private final Long id;
        private final String nickname;

        public Author(Long id, String nickname) {
            this.id = id;
            this.nickname = nickname;
        }
    }
}
