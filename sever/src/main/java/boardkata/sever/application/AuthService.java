package boardkata.sever.application;

import boardkata.sever.domain.user.User;
import boardkata.sever.domain.user.UserRepository;
import boardkata.sever.dto.auth.AuthenticationResult;
import boardkata.sever.dto.auth.LoginDto;
import boardkata.sever.exception.AuthenticationException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * 주어진 데이터로 인증 후 AuthenticateResult 객체를 반환한다.
     * @param loginDto
     * @throws AuthenticationException - 인증에 실패 했을때
     */
    public AuthenticationResult authenticate(LoginDto loginDto) {
        User user = userRepository.findByEmail(loginDto.getEmail())
                .orElseThrow(() -> new AuthenticationException("존재하지 않는 이메일"));


        if (!passwordEncoder.matches(loginDto.getPassword(), user.getPassword())) {
            throw new AuthenticationException("패스워드가 일치하지 않음");
        }

        return AuthenticationResult.builder()
                .id(user.getId())
                .build();
    }
}
