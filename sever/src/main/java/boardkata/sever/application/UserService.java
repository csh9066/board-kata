package boardkata.sever.application;

import boardkata.sever.domain.user.User;
import boardkata.sever.domain.user.UserRepository;
import boardkata.sever.dto.user.UserCreateDto;
import boardkata.sever.dto.user.UserDto;
import boardkata.sever.exception.UserEmailDuplicationException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserDto signup(UserCreateDto userCreateDto) {
        String email = userCreateDto.getEmail();

        if (userRepository.existsByEmail(email)) {
            throw new UserEmailDuplicationException("이미 존재하는 이메일 입니다.");
        }

        String rawPassword = userCreateDto.getPassword();

        String encodedPassword = passwordEncoder.encode(rawPassword);

        User user = User.builder()
                .email(email)
                .password(encodedPassword)
                .build();

        userRepository.save(user);

        return UserDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .build();
    }
}
