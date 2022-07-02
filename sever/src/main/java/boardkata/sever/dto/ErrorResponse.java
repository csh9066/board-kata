package boardkata.sever.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class ErrorResponse {
    private final String message;

    private final List<String> errors = new ArrayList<>();

    @JsonCreator
    public ErrorResponse(@JsonProperty("message") String message) {
        this.message = message;
    }

    public void addError(String error) {
        errors.add(error);
    }
}
