package boardkata.sever.query;

import boardkata.sever.Fixtures;
import boardkata.sever.JpaConfig;
import boardkata.sever.domain.board.Board;
import boardkata.sever.domain.board.BoardRepository;
import boardkata.sever.domain.user.User;
import boardkata.sever.dto.PageResponse;
import boardkata.sever.dto.board.BoardDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageRequest;

import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Import({JpaConfig.class, BoardQueryDao.class})
@DataJpaTest
class BoardQueryDaoTest {

    @Autowired
    BoardRepository boardRepository;

    @Autowired
    EntityManager em;

    @Autowired
    BoardQueryDao boardQueryDao;

    private void createBoard(int numberOfCreation) throws InterruptedException {
        User user = Fixtures.aUser();
        em.persist(user);

        // 생성된 Borad 기준으로 내림차순 정렬을 확인하기 위해 0.01초 마다 Borad 생성
        Thread thread = new Thread(() -> {
            for (int i = 0; i < numberOfCreation; i++) {
                try {
                    Thread.sleep(10);
                    Board board = Board.builder()
                            .authorId(user.getId())
                            .content("content" + i)
                            .title("title" + i)
                            .build();

                    boardRepository.save(board);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        thread.start();
        thread.join();
    }

    @Nested
    @DisplayName("searchBoard 메소드는")
    class Describe_searchBoard {

        @Nested
        @DisplayName("데이터가 20개 주어지고 사이즈가 10이고 page가 1인 Pageable이 주어지면")
        class Context_with_20_data_and_a_Pageable_with_size_10_and_the_1st_page {
            PageRequest pageable;

            @BeforeEach
            void setUp() throws InterruptedException {
                createBoard(20);

                pageable = PageRequest.of(1, 10);
            }

            // 페이지는 0번째 부터 시작한다.
            @DisplayName("1번째의 페이지인 10개의 PageResponse타입의 데이터가 반환된다.")
            @Test
            void it_returns_PageResponse_List_of_size_10() {
                PageResponse<BoardDto> pageResponse = boardQueryDao.searchBoards(pageable);

                assertThat(pageResponse.getResults()).hasSize(10);
                assertThat(pageResponse.getTotalCount()).isEqualTo(20);
                // 생성된 기준으로 정렬 확인
                assertThat(pageResponse.getResults()).element(0)
                        .extracting("title")
                        .isEqualTo("title9");
            }
        }

        @Nested
        @DisplayName("데이터가 20개 주어지고 사이즈가 20이고 page가 0인 Pageable이 주어지면")
        class Context_with_20_data_and_a_Pageable_with_size_20_and_the_0st_page {
            PageRequest pageable;

            @BeforeEach
            void setUp() throws InterruptedException {
                createBoard(20);

                pageable = PageRequest.of(0, 20);
            }

            @DisplayName("0번째의 페이지인 20개의 PageResponse타입의 데이터가 반환된다.")
            @Test
            void it_returns_BoardDto_List_of_size_20() {
                PageResponse<BoardDto> pageResponse = boardQueryDao.searchBoards(pageable);

                assertThat(pageResponse.getResults()).hasSize(20);
                assertThat(pageResponse.getTotalCount()).isEqualTo(20);
                // 생성된 기준으로 정렬 확인
                assertThat(pageResponse.getResults()).element(0)
                        .extracting("title")
                        .isEqualTo("title19");
            }
        }
    }
}
