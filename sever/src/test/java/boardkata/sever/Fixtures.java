package boardkata.sever;

import boardkata.sever.domain.board.Board;
import boardkata.sever.domain.user.User;

public class Fixtures {
    public static User aUser() {
        return User.builder()
                .email("test@gmail.com")
                .nickname("test")
                .password("12345678")
                .build();
    }

    public static Board aBoard() {
        return Board.builder()
                .title("title")
                .authorId(1L)
                .content("kimchi")
                .build();
    }
}
