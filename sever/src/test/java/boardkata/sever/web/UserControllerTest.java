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
    @DisplayName("POST /users ?????????")
    class Describe_signup {

        @Nested
        @DisplayName("????????? ??????????????? ????????????")
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
            @DisplayName("????????? ????????? ????????????.")
            void it_responses_created_user() throws Exception {
                mockMvc.perform(
                                post("/users")
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(objectMapper.writeValueAsString(userCreateDto))
                        )
                        .andExpect(status().isCreated())
                        .andDo(document("sign-up",
                                requestFields(
                                        fieldWithPath("email").description("?????????"),
                                        fieldWithPath("password").description("????????????"),
                                        fieldWithPath("nickname").description("?????????")
                                ),
                                responseFields(
                                        fieldWithPath("id").description("?????????"),
                                        fieldWithPath("email").description("?????????"),
                                        fieldWithPath("nickname").description("?????????")
                                )
                        ));
            }
        }

        @Nested
        @DisplayName("????????? ???????????? ????????????")
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
            @DisplayName("400 ????????? ????????????.")
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
    @DisplayName("GET /users/me ?????????")
    class Describe_getMe {

        @Nested
        @DisplayName("????????? ????????? ????????? ????????????")
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
            @DisplayName("UserDto??? ????????????.")
            @Test
            void it_responses_UserDto() throws Exception {
                mockMvc.perform(get("/users/me"))
                        .andExpect(status().isOk())
                        .andDo(document("get-me",
                                responseFields(
                                        fieldWithPath("id").description("?????????"),
                                        fieldWithPath("email").description("?????????"),
                                        fieldWithPath("nickname").description("?????????")
                                )
                        ));
            }
        }

        @Nested
        @DisplayName("???????????? ?????? ????????? ????????? ????????????")
        class Context_with_not_authenticated_user {

            @WithAnonymousUser
            @DisplayName("401 status??? ????????????.")
            @Test
            void it_responses_401_status() throws Exception {
                mockMvc.perform(get("/users/me"))
                        .andExpect(status().isUnauthorized());
            }
        }
    }

}
