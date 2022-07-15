package boardkata.sever;

import boardkata.sever.domain.user.User;

public class Fixtures {
    public static User aUser() {
        return User.builder()
                .email("test@gmail.com")
                .nickname("test")
                .password("12345678")
                .build();
    }
}
