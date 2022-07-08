package boardkata.sever.web;

import boardkata.sever.application.AuthService;
import boardkata.sever.dto.auth.LoginDto;
import boardkata.sever.securituy.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
public class AuthController {

    private final AuthService authService;

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PostMapping("/login")
    public void login(@RequestBody @Valid LoginDto loginDto) {
        UserPrincipal userPrincipal = authService.authenticate(loginDto);

        UsernamePasswordAuthenticationToken authenticationToken
                = new UsernamePasswordAuthenticationToken(userPrincipal, null, userPrincipal.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
    }
}
