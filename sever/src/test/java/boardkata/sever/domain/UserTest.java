package boardkata.sever.domain;

import boardkata.sever.domain.user.User;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class UserTest {

    @Test
    void builder() {
        User user = User.builder()
                .email("email12@naver.com")
                .password("12341234")
                .build();

        assertThat(user.getEmail()).isEqualTo("email12@naver.com");
        assertThat(user.getPassword()).isEqualTo("12341234");
    }
}
