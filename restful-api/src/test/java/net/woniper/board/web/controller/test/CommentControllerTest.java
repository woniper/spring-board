package net.woniper.board.web.controller.test;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.woniper.board.BoardApplication;
import net.woniper.board.config.SecurityUserDetails;
import net.woniper.board.domain.Board;
import net.woniper.board.domain.Comment;
import net.woniper.board.domain.User;
import net.woniper.board.domain.type.AuthorityType;
import net.woniper.board.repository.BoardRepository;
import net.woniper.board.repository.CommentRepository;
import net.woniper.board.repository.UserRepository;
import net.woniper.board.support.dto.CommentDto;
import net.woniper.board.web.config.test.TestDatabaseConfig;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.Filter;
import java.util.Arrays;

import static org.hamcrest.Matchers.is;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

/**
 * Created by woniper on 15. 2. 4..
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = {BoardApplication.class, TestDatabaseConfig.class})
@WebAppConfiguration
@IntegrationTest("server.port=8888")
@Transactional
public class CommentControllerTest {

    @Autowired private ObjectMapper objectMapper;
    @Autowired private ModelMapper modelMapper;
    @Autowired private WebApplicationContext webApplicationContext;
    @Autowired private Filter springSecurityFilterChain;
    @Autowired private BoardRepository boardRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private CommentRepository commentRepository;

    private MockMvc mock;
    private String mediaType = MediaType.APPLICATION_JSON_VALUE;

    private Board board;
    private User boardUser;
//    private Comment comment;

    @Before
    public void setUp() throws Exception {
        this.mock = webAppContextSetup(webApplicationContext).addFilter(springSecurityFilterChain).build();

        boardUser = new User();
        boardUser.setUsername("boardUser");
        boardUser.setPassword("user Password");
        boardUser.setFirstName("board");
        boardUser.setLastName("user");
        boardUser.setNickName("board user nickName");
        boardUser.setAuthorityType(AuthorityType.ADMIN);
        boardUser = userRepository.save(boardUser);

        board = new Board();
        board.setTitle("comment board title");
        board.setContent("comment board content");
        board.setUser(boardUser);
        board = boardRepository.save(board);
    }

    @Test
    public void test_댓글_등록() throws Exception {
        // given
        CommentDto commentDto = new CommentDto();
        commentDto.setContent("new comment content");

        // when
        ResultActions resultActions = mock.perform(post("/boards/" + board.getBoardId() + "/comments")
                                            .with(user(new SecurityUserDetails(boardUser)))
                                            .contentType(mediaType)
                                            .content(objectMapper.writeValueAsString(commentDto)));

        // then
        resultActions.andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.content", is(commentDto.getContent())))
                .andExpect(jsonPath("$.userId", is(boardUser.getUserId().intValue())))
                .andExpect(jsonPath("$.username", is(boardUser.getUsername())));
    }

    @Test
    public void test_댓글_조회() throws Exception {
        // given
        Comment comment = new Comment();
        comment.setContent("comment content");
        comment.setBoard(board);
        comment.setUser(boardUser);
        comment.setDepth(0);
        board.setComments(Arrays.asList(comment));
        comment = commentRepository.save(comment);

        // when
        ResultActions resultActions = mock.perform(get("/boards/" + board.getBoardId())
                .contentType(mediaType)
                .with(user(new SecurityUserDetails(boardUser))));

        // then
        resultActions.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.boardId", is(board.getBoardId().intValue())))
                .andExpect(jsonPath("$.title", is(board.getTitle())))
                .andExpect(jsonPath("$.comments[0].commentId", is(board.getComments().get(0).getCommentId().intValue())))
                .andExpect(jsonPath("$.comments[0].content", is(board.getComments().get(0).getContent())))
                .andExpect(jsonPath("$.userId", is(boardUser.getUserId().intValue())))
                .andExpect(jsonPath("$.username", is(boardUser.getUsername())));
    }
}