package boardkata.sever.dto.user;

import boardkata.sever.domain.user.User;
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

    public static UserDto of(User user) {
        return UserDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .build();
    }
}
