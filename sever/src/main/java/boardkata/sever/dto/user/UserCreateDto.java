package boardkata.sever.dto.user;

import lombok.Builder;
import lombok.Getter;

@Getter
public class UserCreateDto {
    private final String email;

    private final String password;

    @Builder
    public UserCreateDto(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
