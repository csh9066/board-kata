package boardkata.sever.application;

import boardkata.sever.domain.user.User;
import boardkata.sever.domain.user.UserRepository;
import boardkata.sever.dto.user.UserCreateDto;
import boardkata.sever.dto.user.UserDto;
import boardkata.sever.exception.ResourceNotFoundException;
import boardkata.sever.exception.UserEmailDuplicationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

@DataJpaTest
class UserServiceTest {

    @Autowired
    private UserRepository userRepository;

    private UserService userService;

    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        passwordEncoder = spy(new BCryptPasswordEncoder());

        userService = new UserService(userRepository, passwordEncoder);
    }

    @Nested
    @DisplayName("signup 메소드는")
    class Describe_signup {

        @DataJpaTest
        @Nested
        @DisplayName("회원가입이 가능한 값들이 주어지면")
        class Context_with_valid_values {
            UserCreateDto userCreateDto;

            @BeforeEach
            void setUp() {
                userCreateDto = UserCreateDto.builder()
                        .email("babo123@naver.com")
                        .password("12345678")
                        .build();
            }

            @Test
            @DisplayName("UserDto를 반환한다.")
            void it_returns_UserDto() {
                final UserDto userDto = userService.signup(userCreateDto);

                assertThat(userDto.getId()).isNotNull();
                assertThat(userDto.getEmail()).isEqualTo("babo123@naver.com");

                verify(passwordEncoder).encode("12345678");
            }
        }

        @DataJpaTest
        @Nested
        @DisplayName("이미 존재하는 이메일이 주어지면")
        class Context_with_already_existing_email {
            UserCreateDto userCreateDto;

            @BeforeEach
            void setUp() {
                final User user = User.builder()
                        .email("babo1234@naver.com")
                        .password("12345567")
                        .build();

                userRepository.save(user);

                userCreateDto = UserCreateDto.builder()
                        .email("babo1234@naver.com")
                        .password("13123123")
                        .build();
            }

            @Test
            @DisplayName("UserEmailDuplicationException을 던진다.")
            void it_throws_UserEmailDuplicationException() {
                assertThatThrownBy(() -> userService.signup(userCreateDto))
                        .isInstanceOf(UserEmailDuplicationException.class);
            }
        }
    }

    @DataJpaTest
    @Nested
    @DisplayName("getUser 메소드는")
    class Describe_getUser {

        @Nested
        @DisplayName("유저가 존재하면")
        class Context_when_user_exists {
            Long id;

            @BeforeEach
            void setUp() {
                User user = User.builder()
                        .email("kimchi@naver.com")
                        .password("12345678")
                        .build();

                userRepository.save(user);

                id = user.getId();
            }

            @Test
            @DisplayName("UserDto를 반환한다.")
            void it_returns_UserDto() {
                UserDto user = userService.getUser(id);

                assertThat(user.getId()).isNotNull();
            }
        }

        @Nested
        @DisplayName("유저가 존재하지 않으면")
        class Context_with_user_not_exists {
            final Long nonExistsUserId = 102L;

            @Test
            @DisplayName("ResourceNotFoundException을 던진다.")
            void it_throws_ResourceNotFoundException() {
                assertThatThrownBy(() -> userService.getUser(nonExistsUserId))
                        .isInstanceOf(ResourceNotFoundException.class);
            }
        }
    }
}
