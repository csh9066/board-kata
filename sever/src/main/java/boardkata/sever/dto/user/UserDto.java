package boardkata.sever.dto.user;

import lombok.Builder;
import lombok.Getter;

@Getter
public class UserDto {
    private final Long id;

    private final String email;

    @Builder
    public UserDto(Long id, String email) {
        this.id = id;
        this.email = email;
    }
}
