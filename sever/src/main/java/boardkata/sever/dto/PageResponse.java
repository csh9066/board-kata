package boardkata.sever.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class PageResponse<T> {
    private final List<T> results;
    private final Long totalCount;

    public PageResponse(List<T> results, Long totalCount) {
        this.results = results;
        this.totalCount = totalCount;
    }
}
