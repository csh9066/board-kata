package boardkata.sever.web;

import boardkata.sever.application.UserService;
import boardkata.sever.dto.user.UserCreateDto;
import boardkata.sever.dto.user.UserDto;
import boardkata.sever.exception.UserEmailDuplicationException;
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
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ImportAutoConfiguration(WebSecurityConfig.class)
@AutoConfigureRestDocs
@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    @Nested
    @DisplayName("POST /users 요청은")
    class Describe_signup {

        @Nested
        @DisplayName("올바른 요청값들이 주어지면")
        class Context_with_valid_request_values {
            UserCreateDto userCreateDto;

            @BeforeEach
            void setUp() {
                userCreateDto = UserCreateDto.builder()
                        .email("kimchi123@naver.com")
                        .password("12345678")
                        .nickname("kimchi")
                        .build();

                given(userService.signup(any(UserCreateDto.class)))
                        .willReturn(
                                UserDto.builder()
                                        .id(1L)
                                        .email("kimchi123@naver.com")
                                        .nickname("kimchi")
                                        .build()
                        );

            }

            @Test
            @DisplayName("생성된 유저를 응답한다.")
            void it_responses_created_user() throws Exception {
                mockMvc.perform(
                                post("/users")
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(objectMapper.writeValueAsString(userCreateDto))
                        )
                        .andExpect(status().isCreated())
                        .andDo(document("sign-up",
                                requestFields(
                                        fieldWithPath("email").description("이메일"),
                                        fieldWithPath("password").description("패스워드"),
                                        fieldWithPath("nickname").description("닉네임")
                                ),
                                responseFields(
                                        fieldWithPath("id").description("아이디"),
                                        fieldWithPath("email").description("이메일"),
                                        fieldWithPath("nickname").description("닉네임")
                                )
                        ));
            }
        }

        @Nested
        @DisplayName("중복된 이메일이 주어지면")
        class Context_with_duplicate_email {
            UserCreateDto userCreateDto;

            @BeforeEach
            void setUp() {
                userCreateDto = UserCreateDto.builder()
                        .email("jungbok123@naver.com")
                        .password("123456")
                        .nickname("junbok")
                        .build();

                given(userService.signup(eq(userCreateDto)))
                        .willThrow(UserEmailDuplicationException.class);
            }

            @Test
            @DisplayName("400 상태를 응답한다.")
            void it_responses_400_status() throws Exception {
                mockMvc.perform(
                                post("/users")
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(objectMapper.writeValueAsString(userCreateDto))
                        )
                        .andExpect(status().isBadRequest());
            }
        }
    }

    @Nested
    @DisplayName("GET /users/me 요청은")
    class Describe_getMe {

        @Nested
        @DisplayName("인증된 유저의 요청이 주어지면")
        class Context_with_authenticated_user {

            @BeforeEach
            void setUp() {
                given(userService.getUser(2L))
                        .willReturn(
                                UserDto.builder()
                                        .email("asdasd@naver.com")
                                        .id(2L)
                                        .nickname("babo")
                                        .build()
                        );
            }

            @WithMockAuthUser(id = 2L)
            @DisplayName("UserDto를 응답한다.")
            @Test
            void it_responses_UserDto() throws Exception {
                mockMvc.perform(get("/users/me"))
                        .andExpect(status().isOk())
                        .andDo(document("get-me",
                                responseFields(
                                        fieldWithPath("id").description("아이디"),
                                        fieldWithPath("email").description("이메일"),
                                        fieldWithPath("nickname").description("닉네임")
                                )
                        ));
            }
        }

        @Nested
        @DisplayName("인증되지 않은 유저의 요청이 주어지면")
        class Context_with_not_authenticated_user {

            @WithAnonymousUser
            @DisplayName("401 status를 응답한다.")
            @Test
            void it_responses_401_status() throws Exception {
                mockMvc.perform(get("/users/me"))
                        .andExpect(status().isUnauthorized());
            }
        }
    }

}
