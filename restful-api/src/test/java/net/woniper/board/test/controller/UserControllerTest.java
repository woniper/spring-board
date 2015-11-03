package net.woniper.board.test.controller;

import net.woniper.board.builder.EntityBuilder;
import net.woniper.board.config.SecurityUserDetails;
import net.woniper.board.domain.Board;
import net.woniper.board.domain.KindBoard;
import net.woniper.board.domain.User;
import net.woniper.board.domain.type.AuthorityType;
import net.woniper.board.repository.BoardRepository;
import net.woniper.board.service.KindBoardService;
import net.woniper.board.support.dto.UserDto;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultMatcher;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by woniper on 15. 1. 28..
 */
public class UserControllerTest extends BaseControllerTest {

    @Autowired private BoardRepository boardRepository;
    @Autowired private KindBoardService kindBoardService;
    private KindBoard kindBoard;

    @Before
    public void setUp() throws Exception {
        kindBoard = kindBoardService.createKindBoard("General");
    }

    @Test
    public void test_회원가입() throws Exception {
        // given
        UserDto.Request newUser = modelMapper.map(EntityBuilder.createUser(AuthorityType.ADMIN), UserDto.Request.class);
        newUser.setUsername("signupTestUsername");
        newUser.setNickName("signupTestNickName");

        // when
        ResultActions resultActions = mock.perform(post("/users")
                .contentType(mediaType)
                .content(objectMapper.writeValueAsBytes(newUser)));

        // then
        equalsResultDataAndThen(resultActions, status().isCreated(), newUser);
    }

    @Test
    public void test_회원_정보_수정() throws Exception {
        // given
        UserDto.Request newUser = modelMapper.map(admin, UserDto.Request.class);
        newUser.setPassword("updatePassword");
        newUser.setFirstName("updateFirstName");
        newUser.setLastName("updateLastName");
        newUser.setNickName("updateNickName");

        // when
        ResultActions resultActions = mock.perform(put("/users")
                .with(user(new SecurityUserDetails(admin)))
                .contentType(mediaType)
                .content(objectMapper.writeValueAsBytes(newUser)));

        // then
        equalsResultDataAndThen(resultActions, status().isAccepted(), newUser);
    }

    @Test
    public void test_회원_탈퇴() throws Exception {
        // when
        ResultActions resultActions = mock.perform(delete("/users")
                .contentType(mediaType)
                .with(user(new SecurityUserDetails(admin))));

        // then
        resultActions.andDo(print())
                .andExpect(status().isAccepted());
    }

    @Test
    public void test_회원_아이디_중복() throws Exception {
        // given
        UserDto.Request requestUser = modelMapper.map(EntityBuilder.createUser(AuthorityType.ADMIN), UserDto.Request.class);
        requestUser.setUsername(admin.getUsername());

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
        UserDto.Request requestUser = modelMapper.map(EntityBuilder.createUser(AuthorityType.ADMIN), UserDto.Request.class);
        requestUser.setUsername("nickNameTest");
        requestUser.setNickName(admin.getNickName());

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
        createBoardList(10, admin);
        createBoardList(10, user);

        // when
        ResultActions resultActions = mock.perform(get("/users/boards?page=0&size=20")
                .with(user(new SecurityUserDetails(user))));

        // then
        resultActions.andDo(print()).andExpect(status().isOk());
    }

    @Test
    public void test_회원_조회_자기자신_user() throws Exception {
        // given
        Long userId = user.getUserId();

        // when
        ResultActions resultActions = mock.perform(get("/users/" + userId.intValue())
                .with(user(new SecurityUserDetails(user))));

        // then
        equalsResultDataAndThen(resultActions, status().isOk(), user);
    }

    @Test
    public void test_회원_조회_자기자신_admin() throws Exception {
        // given
        Long userId = admin.getUserId();

        // when
        ResultActions resultActions = mock.perform(get("/users/" + userId.intValue())
                .with(user(new SecurityUserDetails(admin))));

        // then
        equalsResultDataAndThen(resultActions, status().isOk(), admin);
    }

    @Test
    public void test_다른_회원_조회_admin() throws Exception {
        // given
        Long userId = user.getUserId();

        // when
        ResultActions resultActions = mock.perform(get("/users/" + userId.intValue())
                .with(user(new SecurityUserDetails(admin))));

        // then
        equalsResultDataAndThen(resultActions, status().isOk(), user);
    }

    @Test
    public void test_다른_회원_조회_user() throws Exception {
        // given
        Long adminId = admin.getUserId();
        HttpStatus status = HttpStatus.NOT_ACCEPTABLE;

        // when
        ResultActions resultActions = mock.perform(get("/users/" + adminId.intValue())
                .with(user(new SecurityUserDetails(user))));

        // then
        resultActions.andDo(print())
                .andExpect(status().isNotAcceptable())
                .andExpect(jsonPath("$.status", is(status.value())))
                .andExpect(jsonPath("$.message", is(status.getReasonPhrase())));
    }

    @Test
    public void test_회원_리스트_조회_admin() throws Exception {
        // when
        ResultActions resultActions = mock.perform(get("/users")
                .with(user(new SecurityUserDetails(admin))));

        // then
        resultActions.andDo(print()).andExpect(status().isOk());
    }

    @Test
    public void test_회원_리스트_조회_user() throws Exception {
        // when
        ResultActions resultActions = mock.perform(get("/users")
                .with(user(new SecurityUserDetails(user))));

        // then
        resultActions.andDo(print()).andExpect(status().isForbidden());
    }

    @Test
    public void test_getUser_UserNotFoundException() throws Exception {
        // given
        Long userId = Long.MAX_VALUE;
        HttpStatus status = HttpStatus.NOT_FOUND;

        // when
        ResultActions resultActions = mock.perform(get("/users/" + userId.intValue())
                .with(user(new SecurityUserDetails(admin))));

        // then
        resultActions.andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status", is(status.value())))
                .andExpect(jsonPath("$.message", is(status.getReasonPhrase())));
    }

    private void equalsResultDataAndThen(ResultActions resultActions, ResultMatcher resultMatcher,
                                         UserDto.Request userDto) throws Exception {
        resultActions.andDo(print())
                .andExpect(resultMatcher)
                .andExpect(jsonPath("$.username", is(userDto.getUsername())))
                .andExpect(jsonPath("$.firstName", is(userDto.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(userDto.getLastName())))
                .andExpect(jsonPath("$.nickName", is(userDto.getNickName())))
                .andExpect(jsonPath("$.authorityType", is(userDto.getAuthorityType().toString())));
    }

    private void equalsResultDataAndThen(ResultActions resultActions, ResultMatcher resultMatcher,
                                         User user) throws Exception {
        UserDto.Request userDto = modelMapper.map(user, UserDto.Request.class);
        equalsResultDataAndThen(resultActions, resultMatcher, userDto);
    }

    private List<Board> createBoardList(int size, User user) {
        List<Board> list = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            Board newBoard = new Board();
            newBoard.setTitle("list title" + i);
            newBoard.setContent("list content" + i);
            newBoard.setUser(user);
            newBoard.setKindBoard(kindBoard);
            list.add(newBoard);
        }
        return boardRepository.save(list);
    }
}
