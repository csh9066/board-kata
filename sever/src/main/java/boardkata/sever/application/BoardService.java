package boardkata.sever.application;

import boardkata.sever.domain.board.Board;
import boardkata.sever.domain.board.BoardRepository;
import boardkata.sever.domain.user.User;
import boardkata.sever.domain.user.UserRepository;
import boardkata.sever.dto.board.BoardCommandDto;
import boardkata.sever.dto.board.BoardDto;
import boardkata.sever.exception.ResourceNotFoundException;
import boardkata.sever.exception.AccessDeniedException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@RequiredArgsConstructor
@Service
public class BoardService {

    private final BoardRepository boardRepository;

    private final UserRepository userRepository;

    public BoardDto createBoard(Long authorId, BoardCommandDto boardCommandDto) {
        User author = userRepository.findById(authorId).get();

        Board board = Board.builder()
                .authorId(authorId)
                .title(boardCommandDto.getTitle())
                .content(boardCommandDto.getContent())
                .build();

        boardRepository.save(board);

        return BoardDto.of(board, author);
    }

    public BoardDto updateBoard(Long userId, Long boardId, BoardCommandDto boardCommandDto) {
        Board board = findBoard(boardId);

        if (!board.getAuthorId().equals(userId)) {
            throw new AccessDeniedException("board를 변경할 권한이 없습니다.");
        }

        Board source = Board.builder()
                .content(boardCommandDto.getContent())
                .title(boardCommandDto.getTitle())
                .build();

        board.update(source);

        User user = userRepository.findById(userId).get();

        return BoardDto.of(board, user);
    }

    public void deleteBoard(Long userId, Long boardId) {
        Board board = findBoard(boardId);

        if (!board.getAuthorId().equals(userId)) {
            throw new AccessDeniedException("board를 삭제할 권한이 없습니다.");
        }

        boardRepository.delete(board);
    }

    @Transactional(readOnly = true)
    public BoardDto getBoard(Long boardId) {
        Board board = findBoard(boardId);

        User author = userRepository.findById(board.getAuthorId()).get();

        return BoardDto.of(board, author);
    }

    private Board findBoard(Long boardId) {
        return boardRepository.findById(boardId)
                .orElseThrow(() -> new ResourceNotFoundException("board", "id", boardId));
    }

}
