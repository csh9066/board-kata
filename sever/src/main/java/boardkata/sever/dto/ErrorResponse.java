package boardkata.sever.dto;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class ErrorResponse {
    private final String error;

    private final List<String> messages = new ArrayList<>();

    public ErrorResponse(String error) {
        this.error = error;
    }

    public void addMessage(String error) {
        messages.add(error);
    }
}
