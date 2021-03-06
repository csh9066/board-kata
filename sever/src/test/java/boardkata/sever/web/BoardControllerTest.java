package boardkata.sever.web;

import boardkata.sever.application.BoardService;
import boardkata.sever.dto.PageResponse;
import boardkata.sever.dto.board.BoardCommandDto;
import boardkata.sever.dto.board.BoardDto;
import boardkata.sever.exception.AccessDeniedException;
import boardkata.sever.exception.ResourceNotFoundException;
import boardkata.sever.query.BoardQueryDao;
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
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.put;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ImportAutoConfiguration(WebSecurityConfig.class)
@AutoConfigureRestDocs
@WebMvcTest(BoardController.class)
class BoardControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private BoardService boardService;

    @MockBean
    private BoardQueryDao boardQueryDao;

    private BoardCommandDto aBoardCommandDto() {
        return BoardCommandDto.builder()
                .title("title")
                .content("content")
                .build();
    }

    private BoardDto aBoardDto() {
        return BoardDto.builder()
                .id(1L)
                .title("title")
                .content("content")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .author(new BoardDto.Author(2L, "kmichi"))
                .build();
    }

    @Nested
    @DisplayName("POST /boards ?????????")
    class Describe_createBoard {

        @Nested
        @DisplayName("????????? ?????? ????????? ????????????")
        class Context_with_right_request_values {
            final Long userId = 2L;
            BoardCommandDto boardCommandDto;

            @BeforeEach
            void setUp() {
                boardCommandDto = BoardCommandDto.builder()
                        .title("title")
                        .content("content")
                        .build();

                given(boardService.createBoard(eq(userId), any(BoardCommandDto.class)))
                        .willReturn(aBoardDto());

            }

            @WithMockAuthUser(id = 2L)
            @Test
            @DisplayName("????????? borad??? ????????????.")
            void it_responses_created_board() throws Exception {
                mockMvc.perform(post("/boards")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(boardCommandDto))
                        )
                        .andExpect(status().isCreated())
                        .andDo(document("post-boards",
                                requestFields(
                                        fieldWithPath("title").description("?????????"),
                                        fieldWithPath("content").description("?????????")
                                ),
                                responseFields(
                                        fieldWithPath("id").description("?????????"),
                                        fieldWithPath("title").description("?????????"),
                                        fieldWithPath("content").description("?????????"),
                                        fieldWithPath("createdAt").description("????????? ??????"),
                                        fieldWithPath("updatedAt").description("????????? ??????"),
                                        fieldWithPath("author.id").description("????????? ?????????"),
                                        fieldWithPath("author.nickname").description("????????? ?????????")
                                )
                        ));
            }
        }

        @Nested
        @DisplayName("???????????? ?????? ????????? ????????????")
        class Context_with_not_authenticated_request {

            @WithAnonymousUser
            @Test
            @DisplayName("401 ????????? ????????????.")
            void it_responses_401_status() throws Exception {
                mockMvc.perform(post("/boards")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(aBoardCommandDto()))
                        )
                        .andExpect(status().isUnauthorized());
            }
        }
    }

    @Nested
    @DisplayName("PUT /boards/{boardId} ?????????")
    class Describe_updateBoard {

        @Nested
        @DisplayName("????????? ?????? ????????? ????????????")
        class Context_with_right_request_values {
            final Long userId = 2L;
            final Long boardId = 1L;

            @BeforeEach
            void setUp() {
                given(boardService.updateBoard(eq(userId), eq(boardId), any(BoardCommandDto.class)))
                        .willReturn(aBoardDto());
            }

            @WithMockAuthUser(id = 2L)
            @Test
            @DisplayName("????????? borad??? ????????????.")
            void it_responses_updated_board() throws Exception {
                mockMvc.perform(put("/boards/{boardId}", 1L)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(aBoardCommandDto()))
                        )
                        .andExpect(status().isOk())
                        .andDo(document("put-boards",
                                requestFields(
                                        fieldWithPath("title").description("?????????"),
                                        fieldWithPath("content").description("?????????")
                                ),
                                responseFields(
                                        fieldWithPath("id").description("?????????"),
                                        fieldWithPath("title").description("?????????"),
                                        fieldWithPath("content").description("?????????"),
                                        fieldWithPath("createdAt").description("????????? ??????"),
                                        fieldWithPath("updatedAt").description("????????? ??????"),
                                        fieldWithPath("author.id").description("????????? ?????????"),
                                        fieldWithPath("author.nickname").description("????????? ?????????")
                                )
                        ));
            }
        }

        @Nested
        @DisplayName("????????? ????????? ?????????")
        class Context_without_permission_to_update {
            final Long unAccessibleUserId = 100L;
            final Long boardId = 1L;

            @BeforeEach
            void setUp() {
                given(boardService.updateBoard(eq(unAccessibleUserId),
                        eq(boardId),
                        any(BoardCommandDto.class)))
                        .willThrow(AccessDeniedException.class);
            }

            @WithMockAuthUser(id = 100L)
            @Test
            @DisplayName("403 status??? ????????????.")
            void it_responses_403_status() throws Exception {
                mockMvc.perform(put("/boards/{boardId}", boardId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(aBoardCommandDto()))
                        )
                        .andExpect(status().isForbidden());
            }
        }

        @Nested
        @DisplayName("borad??? ???????????? ?????????")
        class Context_when_not_exists_board {
            final Long userId = 2L;
            final Long notExistsBoardId = 112L;

            @BeforeEach
            void setUp() {
                given(boardService.updateBoard(eq(userId),
                        eq(notExistsBoardId),
                        any(BoardCommandDto.class)))
                        .willThrow(ResourceNotFoundException.class);
            }

            @WithMockAuthUser(id = 2L)
            @Test
            @DisplayName("404 status??? ????????????.")
            void it_responses_404_status() throws Exception {
                mockMvc.perform(put("/boards/{boardId}", notExistsBoardId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(aBoardCommandDto()))
                        )
                        .andExpect(status().isNotFound());
            }
        }

        @Nested
        @DisplayName("???????????? ?????? ????????? ????????????")
        class Context_with_not_authenticated_request {
            final Long boardId = 1L;

            @WithAnonymousUser
            @Test
            @DisplayName("401 status??? ????????????.")
            void it_responses_401_status() throws Exception {
                mockMvc.perform(put("/boards/{boardId}", boardId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(aBoardCommandDto()))
                        )
                        .andExpect(status().isUnauthorized());
            }
        }
    }

    @Nested
    @DisplayName("DELETE /boards ?????????")
    class Describe_deleteBoard {

        @Nested
        @DisplayName("????????? ??????????????? ????????????")
        class Context_with_right_request_values {
            final Long boardId = 1L;

            @WithMockAuthUser(id = 2L)
            @Test
            @DisplayName("204 status??? ????????????.")
            void it_responses_204_status() throws Exception {
                mockMvc.perform(delete("/boards/{boardId}", boardId))
                        .andExpect(status().isNoContent())
                        .andDo(document("delete-board"));
            }
        }

        @Nested
        @DisplayName("????????? ????????? ?????????")
        class Context_without_permission_to_delete {
            final Long unAccessibleUserId = 100L;
            final Long boardId = 1L;

            @BeforeEach
            void setUp() {
                doThrow(AccessDeniedException.class)
                        .when(boardService).deleteBoard(eq(unAccessibleUserId), eq(boardId));
            }

            @WithMockAuthUser(id = 100L)
            @Test
            @DisplayName("403 status??? ????????????.")
            void it_responses_403_status() throws Exception {
                mockMvc.perform(delete("/boards/{boardId}", boardId))
                        .andExpect(status().isForbidden());
            }
        }

        @Nested
        @DisplayName("borad??? ???????????? ?????????")
        class Context_when_not_exists_board {
            final Long userId = 2L;
            final Long notExistsBoardId = 112L;

            @BeforeEach
            void setUp() {
                doThrow(ResourceNotFoundException.class)
                        .when(boardService).deleteBoard(userId, notExistsBoardId);
            }

            @WithMockAuthUser(id = 2L)
            @Test
            @DisplayName("404 status??? ????????????.")
            void it_responses_404_status() throws Exception {
                mockMvc.perform(delete("/boards/{boardId}", notExistsBoardId))
                        .andExpect(status().isNotFound());
            }
        }

        @Nested
        @DisplayName("???????????? ?????? ????????? ????????????")
        class Context_with_not_authenticated_request {

            @WithAnonymousUser
            @Test
            @DisplayName("403 status??? ????????????.")
            void it_responses_403_status() throws Exception {
                mockMvc.perform(delete("/boards/{boardId}", 1L))
                        .andExpect(status().isUnauthorized());
            }
        }
    }

    @Nested
    @DisplayName("GET /boards ?????????")
    class Describe_searchBoards {

        @BeforeEach
        void setUp() {
            given(boardQueryDao.searchBoards(any(Pageable.class)))
                    .willReturn(new PageResponse(List.of(aBoardDto()), 1L));
        }

        @Test
        @DisplayName("List ????????? BoardDto??? ?????? PageResponse??? ????????????")
        void it_returns_PageResponse() throws Exception {

            mockMvc.perform(
                            get("/boards")
                                    .param("page", "0")
                                    .param("size", "10")
                    )
                    .andExpect(status().isOk())
                    .andDo(document("get-boards",
                            requestParameters(
                                    parameterWithName("page").description("?????????"),
                                    parameterWithName("size").description("?????????(?????? 30)")
                            ),
                            responseFields(
                                    fieldWithPath("results[].id").description("?????????"),
                                    fieldWithPath("results[].title").description("?????????"),
                                    fieldWithPath("results[].content").description("?????????"),
                                    fieldWithPath("results[].createdAt").description("????????? ??????"),
                                    fieldWithPath("results[].updatedAt").description("????????? ??????"),
                                    fieldWithPath("results[].author.id").description("????????? ?????????"),
                                    fieldWithPath("results[].author.nickname").description("????????? ?????????"),
                                    fieldWithPath("totalCount").description("??? ????????? ???")
                            )
                    ));
        }

        @Nested
        @DisplayName("GET /boards/{id} ?????????")
        class Describe_getBoard {

            @Nested
            @DisplayName("board??? ????????????")
            class Context_with_exists_board {
                @BeforeEach
                void setUp() {
                    given(boardService.getBoard(1L))
                            .willReturn(aBoardDto());
                }

                @Test
                @DisplayName("BoardDto??? ????????????.")
                void it_responses_BoardDto() throws Exception {

                    mockMvc.perform(
                                    get("/boards/{boardId}", 1L)
                            )
                            .andExpect(status().isOk())
                            .andDo(document("get-boards",
                                    pathParameters(
                                            parameterWithName("boardId").description("?????? ?????????")
                                    ),
                                    responseFields(
                                            fieldWithPath("id").description("?????????"),
                                            fieldWithPath("title").description("?????????"),
                                            fieldWithPath("content").description("?????????"),
                                            fieldWithPath("createdAt").description("????????? ??????"),
                                            fieldWithPath("updatedAt").description("????????? ??????"),
                                            fieldWithPath("author.id").description("????????? ?????????"),
                                            fieldWithPath("author.nickname").description("????????? ?????????")
                                    )
                            ));
                }
            }

            @Nested
            @DisplayName("board??? ???????????? ?????????")
            class Context_with_not_exists_board {
                final Long notExistsBoardId = 100L;

                @BeforeEach
                void setUp() {
                    given(boardService.getBoard(notExistsBoardId))
                            .willThrow(ResourceNotFoundException.class);
                }

                @Test
                @DisplayName("404 status??? ????????????.")
                void it_responses_404_status() throws Exception {
                    mockMvc.perform(get("/boards/{boardId}", notExistsBoardId))
                            .andExpect(status().isNotFound());
                }
            }
        }
    }

}


