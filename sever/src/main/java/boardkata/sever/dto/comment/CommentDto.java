package boardkata.sever.dto.comment;


import boardkata.sever.domain.comment.Comment;
import boardkata.sever.domain.user.User;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class CommentDto {
    private final Long id;
    private final String content;
    private final CommentAuthor author;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    @Builder
    public CommentDto(Long id, String content,
                      CommentAuthor author,
                      LocalDateTime createdAt,
                      LocalDateTime updatedAt) {
        this.id = id;
        this.content = content;
        this.author = author;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    @QueryProjection
    public CommentDto(Comment comment, User user) {
        this.id = comment.getId();
        this.content = comment.getContent();
        this.author = new CommentAuthor(user.getId(), user.getNickname());
        this.createdAt = comment.getCreatedAt();
        this.updatedAt = comment.getUpdatedAt();
    }

    @Getter
    public static class CommentAuthor {
        private final Long id;
        private final String nickname;

        public CommentAuthor(Long id, String nickname) {
            this.id = id;
            this.nickname = nickname;
        }
    }
}
