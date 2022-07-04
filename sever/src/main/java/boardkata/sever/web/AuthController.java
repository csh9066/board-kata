package boardkata.sever.web;

import boardkata.sever.application.AuthService;
import boardkata.sever.dto.auth.AuthenticationResult;
import boardkata.sever.dto.auth.LoginDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@RestController
public class AuthController {

    private final AuthService authService;

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PostMapping("/login")
    public void login(@RequestBody @Valid LoginDto loginDto) {
        AuthenticationResult authenticate = authService.authenticate(loginDto);

        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));

        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken
                = new UsernamePasswordAuthenticationToken(authenticate.getId(), null, authorities);

        SecurityContextHolder
                .getContext()
                .setAuthentication(usernamePasswordAuthenticationToken);
    }
}
