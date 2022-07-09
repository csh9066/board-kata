package boardkata.sever.web;

import boardkata.sever.application.UserService;
import boardkata.sever.dto.user.UserCreateDto;
import boardkata.sever.dto.user.UserDto;
import boardkata.sever.securituy.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RequiredArgsConstructor
@RequestMapping("/users")
@RestController
public class UserController {

    private final UserService userService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto signup(@RequestBody @Valid UserCreateDto userCreateDto) {
        return userService.signup(userCreateDto);
    }

    /**
     * 인증된 유저를 응답한다.
     */
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/me")
    public UserDto getMe(@AuthenticationPrincipal UserPrincipal principal) {
        return userService.getUser(principal.getId());
    }
}
