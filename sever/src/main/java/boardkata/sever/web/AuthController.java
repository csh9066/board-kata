package boardkata.sever.web;

import boardkata.sever.application.AuthService;
import boardkata.sever.dto.auth.LoginDto;
import boardkata.sever.securituy.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
                = UsernamePasswordAuthenticationToken.authenticated(userPrincipal,
                userPrincipal.getPassword(),
                userPrincipal.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @GetMapping("/logout")
    public void logout(HttpServletRequest request,
                       HttpServletResponse response,
                       Authentication authentication
    ) {
        // 세션을 무효화하고, SecurityContextHolder 에서 인증 객체를 비움
        new SecurityContextLogoutHandler().logout(request, response, authentication);
    }
}
