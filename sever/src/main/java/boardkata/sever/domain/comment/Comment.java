package boardkata.sever.domain.comment;

import boardkata.sever.domain.generic.BaseTimeEntity;
import boardkata.sever.exception.CommentNotRootException;
import boardkata.sever.exception.CommentAndReplyNotSameBoardException;
import lombok.Builder;
import lombok.Getter;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@Entity
public class Comment extends BaseTimeEntity {

    @Id @GeneratedValue
    private Long id;

    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "root_comment_id")
    private Comment root;

    @OneToMany(mappedBy = "root")
    private List<Comment> replies = new ArrayList<>();

    private Long authorId;

    private Long boardId;

    protected Comment() {}

    @Builder
    private Comment(String content, Long authorId, Long boardId) {
        this.content = content;
        this.authorId = authorId;
        this.boardId = boardId;
    }

    public void addReply(Comment reply) {
        if (this.isNotRoot()) {
            throw new CommentNotRootException(this.getId());
        }

        if (this.isNotSameBoard(reply.getBoardId())) {
            throw new CommentAndReplyNotSameBoardException("babo");
        }

        this.replies.add(reply);
        reply.setRoot(this);
    }

    public void update(String content) {
        this.content = content;
    }

    public boolean isAuthor(Long authorId) {
        return this.authorId.equals(authorId);
    }

    private void setRoot(Comment root) {
        this.root = root;
    }

    private boolean isNotRoot() {
        return Objects.nonNull(this.root);
    }

    private boolean isNotSameBoard(Long boardId) {
        return !this.boardId.equals(boardId);
    }
}
