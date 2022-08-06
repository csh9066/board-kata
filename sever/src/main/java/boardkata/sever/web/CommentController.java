package boardkata.sever.web;

import boardkata.sever.application.CommentService;
import boardkata.sever.dto.comment.CommentCreateDto;
import boardkata.sever.dto.comment.CommentUpdateDto;
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
@RequestMapping("/comments")
@RestController
public class CommentController {

    private final CommentService commentService;

    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("isAuthenticated()")
    @PostMapping
    public Long createdComment(@AuthenticationPrincipal UserPrincipal userPrincipal,
                               @RequestBody @Valid CommentCreateDto commentCreateDto) {
        Long authorId = userPrincipal.getId();
        return commentService.createComment(authorId, commentCreateDto);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("isAuthenticated()")
    @PutMapping("/{commentId}")
    public void updateComment(@AuthenticationPrincipal UserPrincipal userPrincipal,
                              @PathVariable Long commentId,
                              @RequestBody @Valid CommentUpdateDto commentUpdateDto) {
        Long userId = userPrincipal.getId();
        commentService.updateComment(commentId, userId, commentUpdateDto);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("/{commentId}")
    public void deleteComment(@AuthenticationPrincipal UserPrincipal userPrincipal,
                              @PathVariable Long commentId) {
        Long userId = userPrincipal.getId();
        commentService.deleteComment(commentId, userId);
    }
}
