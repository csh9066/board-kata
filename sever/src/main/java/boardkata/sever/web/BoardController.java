package boardkata.sever.web;

import boardkata.sever.application.BoardService;
import boardkata.sever.dto.board.BoardCommandDto;
import boardkata.sever.dto.board.BoardDto;
import boardkata.sever.query.BoardQueryDao;
import boardkata.sever.securituy.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/boards")
@RestController
public class BoardController {

    private final BoardService boardService;
    private final BoardQueryDao boardQueryDao;

    @PreAuthorize("isAuthenticated()")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public BoardDto createBoard(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                @RequestBody @Valid BoardCommandDto boardCommandDto) {
        return boardService.createBoard(userPrincipal.getId(), boardCommandDto);
    }

    @PreAuthorize("isAuthenticated()")
    @PutMapping("/{boardId}")
    public BoardDto updateBoard(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                @PathVariable Long boardId,
                                @RequestBody @Valid BoardCommandDto boardCommandDto) {
        return boardService.updateBoard(userPrincipal.getId(), boardId, boardCommandDto);
    }

    @PreAuthorize("isAuthenticated()")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{boardId}")
    public void deleteBoard(@AuthenticationPrincipal UserPrincipal userPrincipal,
                            @PathVariable Long boardId) {
        boardService.deleteBoard(userPrincipal.getId(), boardId);
    }

    @GetMapping
    public List<BoardDto> searchBoards(@PageableDefault Pageable pageable) {
        return boardQueryDao.searchBoards(pageable);
    }

    @GetMapping("/{boardId}")
    public BoardDto getBoard(@PathVariable Long boardId) {
        return boardService.getBoard(boardId);
    }
}
