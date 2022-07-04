package boardkata.sever.dto.auth;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import javax.validation.constraints.Email;
import javax.validation.constraints.Size;

@EqualsAndHashCode
@Getter
public class LoginDto {
    @Email
    private final String email;

    @Size(min = 8, max = 16)
    private final String password;

    @Builder
    @JsonCreator
    public LoginDto(@JsonProperty("email") String email,
                         @JsonProperty("password")String password) {
        this.email = email;
        this.password = password;
    }
}
