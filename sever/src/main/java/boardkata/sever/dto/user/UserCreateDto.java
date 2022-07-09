package boardkata.sever.dto.user;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@EqualsAndHashCode
@Getter
public class UserCreateDto {
    @Email
    private final String email;

    @Size(min = 8, max = 16)
    private final String password;

    @NotEmpty
    private final String nickname;

    @Builder
    @JsonCreator
    public UserCreateDto(@JsonProperty("email") String email,
                         @JsonProperty("password") String password,
                         @JsonProperty("nickname") String nickname) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
    }
}
