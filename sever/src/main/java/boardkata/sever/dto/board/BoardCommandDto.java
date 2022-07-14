package boardkata.sever.dto.board;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

/**
 * 보드 생성, 업데이트 공통 DTO
 */
@Getter
public class BoardCommandDto {
    private final String title;

    private final String content;

    @Builder
    @JsonCreator
    public BoardCommandDto(@JsonProperty("title") String title,
                           @JsonProperty("content") String content) {
        this.title = title;
        this.content = content;
    }
}
