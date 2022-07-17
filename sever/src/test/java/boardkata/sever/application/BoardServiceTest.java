package boardkata.sever.application;

import boardkata.sever.domain.board.Board;
import boardkata.sever.domain.board.BoardRepository;
import boardkata.sever.domain.user.User;
import boardkata.sever.domain.user.UserRepository;
import boardkata.sever.dto.board.BoardCommandDto;
import boardkata.sever.dto.board.BoardDto;
import boardkata.sever.exception.ResourceNotFoundException;
import boardkata.sever.exception.AccessDeniedException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static boardkata.sever.Fixtures.aUser;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
class BoardServiceTest {

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private UserRepository userRepository;

    private BoardService boardService;

    @BeforeEach
    void setUp() {
        boardService = new BoardService(boardRepository, userRepository);
    }

    private Board aBoard() {
        User author = userRepository.save(aUser());

        Board board = Board.builder()
                .title("test title")
                .content("test content")
                .authorId(author.getId())
                .build();

        return boardRepository.save(board);
    }

    @DataJpaTest
    @Nested
    @DisplayName("createBoard 메소드는")
    class Describe_createBoard {
        Long authorId;
        BoardCommandDto boardCommandDto;
        User user;

        @BeforeEach
        void setUp() {
            user = userRepository.save(aUser());
            authorId = user.getId();

            boardCommandDto = new BoardCommandDto("title", "content");
        }

        @Test
        @DisplayName("생성된 BoardDto를 반환한다.")
        void it_returns_created_BoardDto() {
            BoardDto boardDto = boardService.createBoard(authorId, boardCommandDto);

            assertThat(boardDto.getId()).isNotNull();
            assertThat(boardDto.getTitle()).isEqualTo("title");
            assertThat(boardDto.getContent()).isEqualTo("content");
            assertThat(boardDto.getAuthor().getId()).isEqualTo(user.getId());
            assertThat(boardDto.getAuthor().getNickname()).isEqualTo(user.getNickname());
        }
    }

    @Nested
    @DisplayName("updateBoard 메소드는")
    class Describe_updateBoard {

        @Nested
        @DisplayName("유효한 값들이 주어지면")
        class Context_with_invalid_data {
            Long userId;
            Long boardId;
            BoardCommandDto boardCommandDto;

            @BeforeEach
            void setUp() {
                Board board = aBoard();

                userId = board.getAuthorId();
                boardId = board.getId();
                boardCommandDto = BoardCommandDto
                        .builder()
                        .title("update title")
                        .content("update content")
                        .build();
            }

            @Test
            @DisplayName("변경된 BoardDto를 반환한다.")
            void it_returns_updated_BoardDto() {
                BoardDto boardDto = boardService.updateBoard(userId, boardId, boardCommandDto);

                assertThat(boardDto.getTitle()).isEqualTo("update title");
                assertThat(boardDto.getContent()).isEqualTo("update content");
            }
        }

        @Nested
        @DisplayName("board가 존재하지 않으면")
        class Context_when_not_exists_board {
            Long nonExistsUserId;
            Long userId;
            BoardCommandDto boardCommandDto;

            @BeforeEach
            void setUp() {
                nonExistsUserId = 199L;

                User user = userRepository.save(aUser());
                userId = user.getId();

                boardCommandDto = BoardCommandDto
                        .builder()
                        .title("update title")
                        .content("update content")
                        .build();
            }

            @Test
            @DisplayName("ResourceNotFoundException 예외를 던진다.")
            void it_throws_ResourceNotFoundException() {
                assertThatThrownBy(
                        () -> boardService.updateBoard(userId, nonExistsUserId, boardCommandDto)
                )
                        .isInstanceOf(ResourceNotFoundException.class);
            }
        }

        @Nested
        @DisplayName("변경할 권한이 없으면")
        class Context_when_do_not_have_permission_to_update {
            Long unAuthorizedUserId;
            Long boardId;
            BoardCommandDto boardCommandDto;

            @BeforeEach
            void setUp() {
                unAuthorizedUserId = 123L;

                Board board = aBoard();
                boardId = board.getId();

                boardCommandDto = BoardCommandDto
                        .builder()
                        .title("update title")
                        .content("update content")
                        .build();
            }

            @Test
            @DisplayName("UnAuthorizedException 예외를 던진다.")
            void it_throws_UnAuthorizedException() {
                assertThatThrownBy(
                        () -> boardService.updateBoard(unAuthorizedUserId, boardId, boardCommandDto)
                )
                        .isInstanceOf(AccessDeniedException.class);
            }
        }
    }

    @Nested
    @DisplayName("deleteBoard 메소드는")
    class Describe_deleteBoard {

        @Nested
        @DisplayName("정상적으로 메소드가 호출되었을 때 해당 board를 다시 조회하면")
        class Context_with_success {
            Long userId;
            Long boardId;

            @BeforeEach
            void setUp() {
                Board board = aBoard();

                userId = board.getAuthorId();
                boardId = board.getId();
            }

            @DisplayName("board는 존재하지 않는다.")
            @Test
            void it_does_not_exists_resource() {
                boardService.deleteBoard(userId, boardId);

                boolean isEmptyBoard = boardRepository.findById(boardId).isEmpty();
//
                assertThat(isEmptyBoard).isTrue();
            }

        }

        @Nested
        @DisplayName("board가 존재하지 않으면")
        class Context_when_not_exists_board {
            final Long notExistsBoardId = 123L;

            @Test
            @DisplayName("ResourceNotFoundException 예외를 던진다.")
            void it_throws_ResourceNotFoundException() {
                assertThatThrownBy(() -> boardService.deleteBoard(1L, notExistsBoardId))
                        .isInstanceOf(ResourceNotFoundException.class);
            }
        }

        @Nested
        @DisplayName("변경할 권한이 없으면")
        class Context_when_do_not_have_permission_to_update {
            Long boardId;
            Long unAuthorizedUserId;

            @BeforeEach
            void setUp() {
                unAuthorizedUserId = 1231L;

                Board board = aBoard();
                boardId = board.getId();
            }

            @Test
            @DisplayName("UnAuthorizedException 예외를 던진다.")
            void it_throws_UnAuthorizedException() {
                assertThatThrownBy(
                        () -> boardService.deleteBoard(unAuthorizedUserId, boardId)
                ).isInstanceOf(AccessDeniedException.class);
            }
        }
    }

    @Nested
    @DisplayName("getBoard 메소드는")
    class Describe_getBoard {

        @Nested
        @DisplayName("board가 존재하면")
        class Context_with_exists_board {
            Board board;
            Long boardId;

            @BeforeEach
            void setUp() {
                board = aBoard();
                boardId = board.getId();
            }

            @Test
            @DisplayName("BoardDto를 반환한다.")
            void it_returns_BoardDto() {
                BoardDto boardDto = boardService.getBoard(boardId);

                assertThat(boardDto.getId()).isEqualTo(boardId);
                assertThat(boardDto.getTitle()).isEqualTo(board.getTitle());
                assertThat(boardDto.getContent()).isEqualTo(board.getContent());
            }
        }

        @Nested
        @DisplayName("board가 존재하지 않으면")
        class Context_with_not_exists_board {
            final Long notExistsBoardId = 100L;

            @Test
            @DisplayName("BoardDto를 반환한다.")
            void it_returns_BoardDto() {
                assertThatThrownBy(() -> boardService.getBoard(notExistsBoardId))
                        .isInstanceOf(ResourceNotFoundException.class);
            }
        }
    }

}
