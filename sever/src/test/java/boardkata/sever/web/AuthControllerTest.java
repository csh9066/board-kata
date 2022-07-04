package boardkata.sever.web;

import boardkata.sever.application.AuthService;
import boardkata.sever.dto.auth.AuthenticationResult;
import boardkata.sever.dto.auth.LoginDto;
import boardkata.sever.exception.AuthenticationException;
import boardkata.sever.securituy.WebSecurityConfig;
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
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
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

                AuthenticationResult authenticationResult =
                        AuthenticationResult.builder()
                                .id(1L)
                                .build();

                given(authService.authenticate(eq(loginDto)))
                        .willReturn(authenticationResult);
            }

            @Test
            @DisplayName("204 status를 응답한다.")
            void it_responses_204_status() throws Exception {
                mockMvc.perform(
                                post("/login")
                                        .content(objectMapper.writeValueAsString(loginDto))
                                        .contentType(MediaType.APPLICATION_JSON)
                        )
                        .andExpect(status().isNoContent())
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
}
