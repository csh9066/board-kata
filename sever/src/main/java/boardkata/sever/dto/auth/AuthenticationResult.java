package boardkata.sever.dto.auth;

import lombok.Builder;
import lombok.Getter;

@Getter
public class AuthenticationResult {
    private final Long id;

    @Builder
    public AuthenticationResult(Long id) {
        this.id = id;
    }
}
