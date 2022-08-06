package boardkata.sever.web;

import boardkata.sever.application.CommentService;
import boardkata.sever.dto.comment.CommentCreateDto;
import boardkata.sever.dto.comment.CommentUpdateDto;
import boardkata.sever.exception.AccessDeniedException;
import boardkata.sever.exception.ResourceNotFoundException;
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
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willThrow;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.put;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ImportAutoConfiguration(WebSecurityConfig.class)
@AutoConfigureRestDocs
@WebMvcTest(CommentController.class)
class CommentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CommentService commentService;

    private CommentCreateDto aCommentCreateDto() {
        return new CommentCreateDto("content", 1L);
    }

    private CommentUpdateDto aCommentUpdateDto() {
        return new CommentUpdateDto("update content");
    }

    @Nested
    @DisplayName("POST /comments 요청은")
    class Describe_createComment {

        @Nested
        @DisplayName("올바른 요청이 오면")
        class Context_when_right_request {
            Long boardId;
            Long authorId;
            CommentCreateDto commentCreateDto;

            @BeforeEach
            void setUp() {
                boardId = 1L;
                authorId = 1L;
                commentCreateDto = new CommentCreateDto("content", boardId);

                given(commentService.createComment(eq(authorId), any(CommentCreateDto.class)))
                        .willReturn(1L);
            }

            @WithMockAuthUser(id = 1L)
            @Test
            @DisplayName("201 status 응답한다.")
            void it_responses_201_status() throws Exception {
                mockMvc.perform(
                                post("/comments", boardId)
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(objectMapper.writeValueAsString(commentCreateDto))
                        )
                        .andExpect(status().isCreated())
                        .andDo(document("post-comment",
                                requestFields(
                                        fieldWithPath("content").description("컨텐츠"),
                                        fieldWithPath("boardId").description("보드 아이디")
                                )
                        ));
            }
        }

        @Nested
        @DisplayName("board가 존재하지 않으면")
        class Context_when_not_exists_board {
            Long notExistsBoardId;
            Long authorId;
            CommentCreateDto commentCreateDto;

            @BeforeEach
            void setUp() {
                notExistsBoardId = 100L;
                authorId = 1L;
                commentCreateDto =  new CommentCreateDto("content", notExistsBoardId);

                given(commentService.
                        createComment(
                                eq(authorId),
                                any(CommentCreateDto.class)
                        ))
                        .willThrow(ResourceNotFoundException.class);
            }

            @WithMockAuthUser
            @Test
            @DisplayName("404 status를 응답한다.")
            void it_responses_404_status() throws Exception {
                mockMvc.perform(
                                post("/comments", notExistsBoardId)
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(objectMapper.writeValueAsString(commentCreateDto))
                        )
                        .andExpect(status().isNotFound());
            }
        }

        @Nested
        @DisplayName("인증하지 않은 사용자 요청이 오면")
        class Context_when_unAuthenticated_request {
            final Long boardId = 1L;
            final CommentCreateDto commentCreateDto = aCommentCreateDto();

            @WithAnonymousUser
            @Test
            @DisplayName("401 status를 응답한다.")
            void it_responses_401_status() throws Exception {
                mockMvc.perform(
                                post("/comments", boardId)
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(objectMapper.writeValueAsString(commentCreateDto))
                        )
                        .andExpect(status().isUnauthorized());
            }
        }
    }

    @Nested
    @DisplayName("PUT /comments 요청은")
    class Describe_updateComment {

        @Nested
        @DisplayName("올바른 요청이 오면")
        class Context_when_right_request {
            Long commentId;
            Long authorId;
            CommentUpdateDto commentUpdateDto;

            @BeforeEach
            void setUp() {
                commentId = 1L;
                authorId = 1L;
                commentUpdateDto = new CommentUpdateDto("content");
            }

            @WithMockAuthUser(id = 1L)
            @Test
            @DisplayName("204 status를 응답한다.")
            void it_responses_204_status() throws Exception {
                mockMvc.perform(
                                put("/comments/{commentId}", commentId)
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(objectMapper.writeValueAsString(commentUpdateDto))
                        )
                        .andExpect(status().isNoContent())
                        .andDo(document("post-comment",
                                pathParameters(
                                        parameterWithName("commentId").description("comment 아이디")
                                ),
                                requestFields(
                                        fieldWithPath("content").description("컨텐츠")
                                )
                        ));
            }
        }

        @Nested
        @DisplayName("comment가 존재하지 않으면")
        class Context_when_not_exists_comment {
            Long notExistsCommentId;
            CommentUpdateDto commentUpdateDto;

            @BeforeEach
            void setUp() {
                notExistsCommentId = 100L;

                commentUpdateDto = aCommentUpdateDto();

                willThrow(ResourceNotFoundException.class)
                        .given(commentService).updateComment(
                                eq(notExistsCommentId), eq(1L), any(CommentUpdateDto.class));
            }

            @WithMockAuthUser(id = 1L)
            @Test
            @DisplayName("404 status를 응답한다.")
            void it_responses_404_status() throws Exception {
                mockMvc.perform(
                                put("/comments/{commentId}", notExistsCommentId)
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(objectMapper.writeValueAsString(commentUpdateDto))
                        )
                        .andExpect(status().isNotFound());
            }
        }

        @Nested
        @DisplayName("인증하지 않은 사용자 요청이 오면")
        class Context_when_unAuthenticated_request {

            @WithAnonymousUser
            @Test
            @DisplayName("401 status를 응답한다.")
            void it_responses_401_status() throws Exception {
                mockMvc.perform(
                                put("/comments/{commentId}", 1L)
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(objectMapper.writeValueAsString(aCommentUpdateDto()))
                        )
                        .andExpect(status().isUnauthorized());
            }
        }

        @Nested
        @DisplayName("변경할 권한이 없는 요청이 오면")
        class Context_when_access_denied_request {
            Long commentId;

            @BeforeEach
            void setUp() {
                commentId = 1L;

                willThrow(AccessDeniedException.class)
                        .given(commentService).updateComment(
                                eq(commentId), eq(100L), any(CommentUpdateDto.class));
            }

            @WithMockAuthUser(id = 100L)
            @Test
            @DisplayName("403 status를 응답한다.")
            void it_responses_403_status() throws Exception {
                mockMvc.perform(
                                put("/comments/{commentId}", commentId)
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(objectMapper.writeValueAsString(aCommentUpdateDto()))
                        )
                        .andExpect(status().isForbidden());
            }
        }
    }

    @Nested
    @DisplayName("DELETE /comments 요청은")
    class Describe_deleteComment {

        @Nested
        @DisplayName("올바른 요청이 오면")
        class Context_when_right_request {

            @WithMockAuthUser(id = 1L)
            @Test
            @DisplayName("204 status를 응답한다.")
            void it_responses_created_comment_id() throws Exception {
                mockMvc.perform(
                                delete("/comments/{commentId}", 1L)
                        )
                        .andExpect(status().isNoContent())
                        .andDo(document("delete-comment",
                                pathParameters(
                                        parameterWithName("commentId").description("comment 아이디")
                                )
                        ));
            }
        }

        @Nested
        @DisplayName("comment가 존재하지 않으면")
        class Context_when_not_exists_comment {
            Long notExistsCommentId;

            @BeforeEach
            void setUp() {
                notExistsCommentId = 100L;


                willThrow(ResourceNotFoundException.class)
                        .given(commentService).deleteComment(
                                eq(notExistsCommentId), eq(1L));
            }

            @WithMockAuthUser(id = 1L)
            @Test
            @DisplayName("404 status를 응답한다.")
            void it_responses_404_status() throws Exception {
                mockMvc.perform(
                                delete("/comments/{commentId}", notExistsCommentId)
                        )
                        .andExpect(status().isNotFound());
            }
        }

        @Nested
        @DisplayName("인증하지 않은 사용자 요청이 오면")
        class Context_when_unAuthenticated_request {

            @WithAnonymousUser
            @Test
            @DisplayName("401 status를 응답한다.")
            void it_responses_401_status() throws Exception {
                mockMvc.perform(
                                delete("/comments/{commentId}", 1L)
                        )
                        .andExpect(status().isUnauthorized());
            }
        }

        @Nested
        @DisplayName("변경할 권한이 없는 요청이 오면")
        class Context_when_access_denied_request {
            Long commentId;

            @BeforeEach
            void setUp() {
                commentId = 1L;

                willThrow(AccessDeniedException.class)
                        .given(commentService).deleteComment(
                                eq(commentId), eq(100L));
            }

            @WithMockAuthUser(id = 100L)
            @Test
            @DisplayName("403 status를 응답한다.")
            void it_responses_403_status() throws Exception {
                mockMvc.perform(
                                delete("/comments/{commentId}", commentId)
                        )
                        .andExpect(status().isForbidden());
            }
        }
    }
}
