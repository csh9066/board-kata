package boardkata.sever.domain.board;

import boardkata.sever.JpaConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@Import(JpaConfig.class)
@DataJpaTest
class BoardRepositoryTest {

    @Autowired
    private BoardRepository boardRepository;

    @Test
    void save() {
        // given
        Board board = Board.builder()
                .authorId(1L)
                .title("kimchi")
                .content("amauauasdasd")
                .build();

        LocalDateTime postSaveTime = LocalDateTime.now();

        // when
        boardRepository.save(board);

        // then
        assertThat(board.getId()).isNotNull();
        assertThat(board.getCreatedAt()).isAfter(postSaveTime);
        assertThat(board.getUpdatedAt()).isAfter(postSaveTime);
    }

    @Test
    void update() {
        // given
        Board board = Board.builder()
                .authorId(1L)
                .title("kimuchi")
                .content("김치 좋아!")
                .build();

        boardRepository.save(board);

        LocalDateTime saveUpdateAt = board.getUpdatedAt();
        
        Board source = Board.builder()
                .title("update")
                .content("content")
                .build();

        // when
        board.update(source);
        
        boardRepository.flush();

        // then
        assertThat(board.getUpdatedAt()).isAfter(saveUpdateAt);
        assertThat(board.getTitle()).isEqualTo("update");
        assertThat(board.getContent()).isEqualTo("content");
    }
}
