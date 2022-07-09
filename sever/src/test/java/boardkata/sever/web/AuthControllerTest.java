package boardkata.sever.web;

import boardkata.sever.application.AuthService;
import boardkata.sever.dto.auth.LoginDto;
import boardkata.sever.exception.AuthenticationException;
import boardkata.sever.securituy.UserPrincipal;
import boardkata.sever.securituy.WebSecurityConfig;
import boardkata.sever.security.WithMockAuthUser;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ImportAutoConfiguration(WebSecurityConfig.class)
@AutoConfigureRestDocs
@WebMvcTest(AuthController.class)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AuthService authService;

    @Nested
    @DisplayName("POST /login 요청은")
    class Describe_login {

        @Nested
        @DisplayName("올바른 요청 값들이 주어지면")
        class Context_with_right_request_values {
            LoginDto loginDto;

            @BeforeEach
            void setUp() {
                loginDto = LoginDto.builder()
                        .email("kimchi@1234")
                        .password("12345678")
                        .build();

                UserPrincipal userPrincipal = UserPrincipal.builder()
                        .id(1L)
                        .email("kimchi@1234")
                        .password("12345678")
                        .build();

                given(authService.authenticate(eq(loginDto)))
                        .willReturn(userPrincipal);
            }

            @Test
            @DisplayName("204 status와 인증이 완료 된다.")
            void it_responses_204_status() throws Exception {
                mockMvc.perform(
                                post("/login")
                                        .content(objectMapper.writeValueAsString(loginDto))
                                        .contentType(MediaType.APPLICATION_JSON)
                        )
                        .andExpect(status().isNoContent())
                        .andExpect(authenticated().withUsername("kimchi@1234"))
                        .andDo(document("login",
                                requestFields(
                                        fieldWithPath("email").description("이메일"),
                                        fieldWithPath("password").description("패스워드")
                                )
                        ));
            }
        }

        @Nested
        @DisplayName("로그인 인증에 실패하면")
        class Context_when_failure_login {
            LoginDto loginDto;

            @BeforeEach
            void setUp() {
                loginDto = LoginDto.builder()
                        .email("xxxx123@naver.com")
                        .password("12345678")
                        .build();

                given(authService.authenticate(eq(loginDto)))
                        .willThrow(AuthenticationException.class);
            }

            @Test
            @DisplayName("400 status를 응답한다.")
            void it_responses_204_status() throws Exception {
                mockMvc.perform(
                                post("/login")
                                        .content(objectMapper.writeValueAsString(loginDto))
                                        .contentType(MediaType.APPLICATION_JSON)
                        )
                        .andExpect(status().isBadRequest());
            }
        }
    }

    @Nested
    @DisplayName("GET /logout 요청은")
    class Describe_logout {

        @Nested
        @DisplayName("인증된 사용자 요청이 오면")
        class Context_with_authenticated_user {

            @WithMockAuthUser
            @DisplayName("204 status와 인증을 만료시킨다.")
            @Test
            void it_responses_204_status() throws Exception {
                mockMvc.perform(get("/logout"))
                        .andExpect(status().isNoContent())
                        .andExpect(unauthenticated());
            }
        }
    }

}
