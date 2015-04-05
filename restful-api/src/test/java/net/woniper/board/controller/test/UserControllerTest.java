package net.woniper.board.controller.test;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.woniper.board.BoardApplication;
import net.woniper.board.config.SecurityUserDetails;
import net.woniper.board.domain.Board;
import net.woniper.board.domain.User;
import net.woniper.board.repository.BoardRepository;
import net.woniper.board.repository.UserRepository;
import net.woniper.board.service.impl.UserServiceImpl;
import net.woniper.board.support.dto.UserDto;
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
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.Matchers.is;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

/**
 * Created by woniper on 15. 1. 28..
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = BoardApplication.class)
@WebAppConfiguration
@IntegrationTest("server.port=8888")
@Transactional
public class UserControllerTest {

    @Autowired private UserRepository userRepository;
    @Autowired private UserServiceImpl userService;
    @Autowired private ObjectMapper objectMapper;
    @Autowired private WebApplicationContext webApplicationContext;
    @Autowired private ModelMapper modelMapper;
    @Autowired private BoardRepository boardRepository;
    @Autowired private Filter springSecurityFilterChain;

    private MockMvc mock;
    private User user = new User();
    private String mediaType = MediaType.APPLICATION_JSON_VALUE;

    @Before
    public void setUp() throws Exception {
        this.mock = webAppContextSetup(webApplicationContext)
                        .addFilter(springSecurityFilterChain).build();

        user.setUsername("woniper");
        user.setPassword("12345");
        user.setFirstName("kyung-won");
        user.setLastName("lee");
        user.setNickName("woniper");
        user.setAdmin(true);
        user = userRepository.save(user);
    }

    @Test
    public void test_회원가입() throws Exception {
        // given
        UserDto.Request newUser = createUserRequest(true);

        // when
        ResultActions resultActions = mock.perform(post("/users")
                                            .contentType(mediaType)
                                            .content(objectMapper.writeValueAsBytes(newUser)));

        // then
        resultActions.andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.username", is(newUser.getUsername())))
                .andExpect(jsonPath("$.firstName", is(newUser.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(newUser.getLastName())))
                .andExpect(jsonPath("$.nickName", is(newUser.getNickName())))
                .andExpect(jsonPath("$.admin", is(newUser.isAdmin())));
    }

    @Test
    public void test_회원_정보_수정() throws Exception {
        // given
        UserDto.Request newUser = modelMapper.map(user, UserDto.Request.class);
        newUser.setPassword("updatePassword");
        newUser.setFirstName("updateFirstName");
        newUser.setLastName("updateLastName");
        newUser.setNickName("updateNickName");

        // when
        ResultActions resultActions = mock.perform(put("/users")
                                            .with(user(new SecurityUserDetails(user)))
                                            .contentType(mediaType)
                                            .content(objectMapper.writeValueAsBytes(newUser)));

        // then
        resultActions.andDo(print())
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.username", is(newUser.getUsername())))
                .andExpect(jsonPath("$.firstName", is(newUser.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(newUser.getLastName())))
                .andExpect(jsonPath("$.nickName", is(newUser.getNickName())));
    }

    @Test
    public void test_회원_탈퇴() throws Exception {
        // when
        ResultActions resultActions = mock.perform(delete("/users")
                                            .with(user(new SecurityUserDetails(user))));

        // then
        resultActions.andDo(print())
                .andExpect(status().isAccepted());
    }

    @Test
    public void test_회원_아이디_중복() throws Exception {
        // given
        UserDto.Request requestUser = createUserRequest(true);
        requestUser.setUsername("woniper");

        // when
        ResultActions resultActions = mock.perform(post("/users")
                .contentType(mediaType)
                .content(objectMapper.writeValueAsBytes(requestUser)));

        // then
        resultActions.andDo(print())
                .andExpect(status().isConflict());
    }

    @Test
    public void test_회원_닉네임_중복() throws Exception {
        // given
        UserDto.Request requestUser = createUserRequest(true);
        requestUser.setNickName("woniper");

        // when
        ResultActions resultActions = mock.perform(post("/users")
                .contentType(mediaType)
                .content(objectMapper.writeValueAsBytes(requestUser)));

        // then
        resultActions.andDo(print())
                .andExpect(status().isConflict());
    }

    @Test
    public void test_내가_쓴_게시글_리스트_조회() throws Exception {
        // given
        User newUser = createUser(true);
        createBoardList(10, user);
        List<Board> boardList = createBoardList(10, newUser)
                .stream().sorted((b1, b2) -> b2.getBoardId().compareTo(b1.getBoardId())).collect(Collectors.toList());

        // when
        ResultActions resultActions = mock.perform(get("/users/boards/1")
                .with(user(new SecurityUserDetails(newUser))));

        // then
        resultActions.andDo(print()).andExpect(status().isOk());

        int count = boardList.size();
        for (int i = 0; i < count; i++) {
            Board board = boardList.get(i);
            resultActions.andExpect(jsonPath("$.boards[" + i + "].boardId", is(board.getBoardId().intValue())))
                    .andExpect(jsonPath("$.boards[" + i + "].title", is(board.getTitle())))
                    .andExpect(jsonPath("$.boards[" + i + "].content", is(board.getContent())))
                    .andExpect(jsonPath("$.boards[" + i + "].readCount", is(board.getReadCount())))
                    .andExpect(jsonPath("$.boards[" + i + "].userId", is(newUser.getUserId().intValue())));
        }
    }

    @Test
    public void test_내가_쓴_게시글_정렬_리스트_조회() throws Exception {
        // given
        User newUser = createUser(true);
        List<Board> boardList = createBoardList(10, newUser)
                .stream().sorted((b1, b2) -> b2.getTitle().compareTo(b1.getTitle())).collect(Collectors.toList());

        // when
        ResultActions resultActions = mock.perform(get("/users/boards/1?orderBy=title")
                                            .with(user(new SecurityUserDetails(newUser))));

        // then
        resultActions.andDo(print()).andExpect(status().isOk());
        int count = boardList.size();

        for (int i = 0; i < count; i++) {
            Board board = boardList.get(i);
            resultActions.andExpect(jsonPath("$.boards[" + i + "].boardId", is(board.getBoardId().intValue())))
                    .andExpect(jsonPath("$.boards[" + i + "].title", is(board.getTitle())))
                    .andExpect(jsonPath("$.boards[" + i + "].content", is(board.getContent())))
                    .andExpect(jsonPath("$.boards[" + i + "].readCount", is(board.getReadCount())))
                    .andExpect(jsonPath("$.boards[" + i + "].username", is(newUser.getUsername())));
        }

    }

    private User createUser(boolean isAdmin) {
        User newUser = new User();
        newUser.setUsername("newUser");
        newUser.setPassword("12345");
        newUser.setFirstName("kyung-won");
        newUser.setLastName("lee");
        newUser.setNickName("newUser");
        newUser.setAdmin(isAdmin);
        return userRepository.save(newUser);
    }

    private UserDto.Request createUserRequest(boolean isAdmin) {
        UserDto.Request newUser = new UserDto.Request();
        newUser.setUsername("newUser");
        newUser.setPassword("12345");
        newUser.setFirstName("kyung-won");
        newUser.setLastName("lee");
        newUser.setNickName("newUser");
        newUser.setAdmin(true);
        return newUser;
    }

    private List<Board> createBoardList(int size, User user) {
        List<Board> list = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            Board newBoard = new Board();
            newBoard.setTitle("list title" + i);
            newBoard.setContent("list content" + i);
            newBoard.setUser(user);
            list.add(newBoard);
        }
        return boardRepository.save(list);
    }
}
