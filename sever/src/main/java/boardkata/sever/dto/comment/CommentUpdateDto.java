package boardkata.sever.dto.comment;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import javax.validation.constraints.NotBlank;

@Getter
public class CommentUpdateDto {
    @NotBlank
    private final String content;

    @JsonCreator
    public CommentUpdateDto(@JsonProperty("content") String content) {
        this.content = content;
    }
}
