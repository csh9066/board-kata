package boardkata.sever.dto.comment;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class CommentCommandDto {
    private final String content;

    @JsonCreator
    public CommentCommandDto(@JsonProperty("content") String content) {
        this.content = content;
    }
}
