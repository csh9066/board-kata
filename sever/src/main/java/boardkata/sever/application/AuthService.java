package boardkata.sever.application;

import boardkata.sever.domain.user.User;
import boardkata.sever.domain.user.UserRepository;
import boardkata.sever.dto.auth.LoginDto;
import boardkata.sever.exception.AuthenticationException;
import boardkata.sever.securituy.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserPrincipal authenticate(LoginDto loginDto) {
        User user = userRepository.findByEmail(loginDto.getEmail())
                .orElseThrow(() -> new AuthenticationException("존재하지 않는 이메일"));


        if (!passwordEncoder.matches(loginDto.getPassword(), user.getPassword())) {
            throw new AuthenticationException("패스워드가 일치하지 않음");
        }

        return new UserPrincipal(user.getId(), user.getEmail(), user.getPassword());
    }
}
