package boardkata.sever.web;

import boardkata.sever.application.BoardService;
import boardkata.sever.dto.board.BoardCommandDto;
import boardkata.sever.dto.board.BoardDto;
import boardkata.sever.securituy.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RequiredArgsConstructor
@RequestMapping("/boards")
@RestController
public class BoardController {

    private final BoardService boardService;

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
}
