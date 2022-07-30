package boardkata.sever.dto.comment;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
public class CommentCreateDto {
    @NotBlank
    private final String content;

    @NotNull
    private final Long boardId;

    @JsonCreator
    public CommentCreateDto(@JsonProperty("content") String content,
                            @JsonProperty("boardId") Long boardId) {
        this.content = content;
        this.boardId = boardId;
    }
}
