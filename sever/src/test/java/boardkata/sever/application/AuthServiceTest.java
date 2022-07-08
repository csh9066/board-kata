package boardkata.sever.application;

import boardkata.sever.domain.user.User;
import boardkata.sever.domain.user.UserRepository;
import boardkata.sever.dto.auth.LoginDto;
import boardkata.sever.exception.AuthenticationException;
import boardkata.sever.securituy.UserPrincipal;
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

@DataJpaTest
class AuthServiceTest {

    private AuthService authService;

    @Autowired
    private UserRepository userRepository;

    PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        passwordEncoder = new BCryptPasswordEncoder();

        authService = new AuthService(userRepository, passwordEncoder);
    }

    @Nested
    @DisplayName("authenticate 메소드는")
    class Describe_authenticate {

        @DataJpaTest
        @Nested
        @DisplayName("올바른 데이터가 주어지면")
        class Context_with_right_data {
            LoginDto loginDto;
            Long userId;

            @BeforeEach
            void setUp() {
                User user = User.builder()
                        .email("test123@naver.com")
                        .password(passwordEncoder.encode("12345678"))
                        .build();


                User savedUser = userRepository.save(user);

                userId = savedUser.getId();

                loginDto = LoginDto.builder()
                        .email("test123@naver.com")
                        .password("12345678")
                        .build();
            }

            @Test
            @DisplayName("AuthUser를 반환한다.")
            void it_returns_AuthenticateResult() {
                UserPrincipal userPrincipal = authService.authenticate(loginDto);

                assertThat(userPrincipal.getId()).isEqualTo(userId);
                assertThat(userPrincipal.getAuthorities())
                        .extractingResultOf("getAuthority")
                        .contains("ROLE_USER");
                assertThat(userPrincipal.getUsername()).isEqualTo("test123@naver.com");
                assertThat(userPrincipal.getPassword()).isNotEqualTo("12345678");
            }
        }

        @DataJpaTest
        @Nested
        @DisplayName("존재하지 않는 이메일이 주어지면")
        class Context_with_nonexistent_email {
            LoginDto loginDto;

            @BeforeEach
            void setUp() {
                loginDto = LoginDto.builder()
                        .email("doesntExistEmail@naver.com")
                        .password("12345678")
                        .build();
            }

            @Test
            @DisplayName("AuthenticationException 예외를 던진다")
            void it_throws_AuthenticationException() {
                assertThatThrownBy(() -> authService.authenticate(loginDto))
                        .isInstanceOf(AuthenticationException.class);
            }
        }

        @DataJpaTest
        @Nested
        @DisplayName("틀린 패스워드가 주어지면")
        class Context_with_mismatches_password {
            LoginDto loginDto;

            @BeforeEach
            void setUp() {
                User user = User.builder()
                        .email("test123@naver.com")
                        .password(passwordEncoder.encode("12345678"))
                        .build();


                User savedUser = userRepository.save(user);

                loginDto = LoginDto.builder()
                        .email(savedUser.getEmail())
                        .password("12312kladsjlad")
                        .build();
            }

            @Test
            @DisplayName("AuthenticationException 예외를 던진다")
            void it_throws_AuthenticationException() {
                assertThatThrownBy(() -> authService.authenticate(loginDto))
                        .isInstanceOf(AuthenticationException.class);
            }
        }
    }
}
