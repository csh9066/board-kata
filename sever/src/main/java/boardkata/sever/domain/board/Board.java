package boardkata.sever.domain.board;

import boardkata.sever.domain.generic.BaseTimeEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Getter
@Entity
public class Board extends BaseTimeEntity {

    @Id
    @GeneratedValue
    private Long id;

    private Long authorId;

    private String title;

    private String content;

    protected Board() {}

    @Builder
    private Board(Long authorId,
                 @NonNull String title,
                 @NonNull String content) {
        this.authorId = authorId;
        this.title = title;
        this.content = content;
    }

    public void update(Board source) {
        this.title = source.title;
        this.content = source.content;
    }
}
