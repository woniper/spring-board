package net.woniper.board.test.service;

import net.woniper.board.BoardApplication;
import net.woniper.board.builder.EntityBuilder;
import net.woniper.board.domain.User;
import net.woniper.board.domain.type.AuthorityType;
import net.woniper.board.errors.support.NickNameDuplicateException;
import net.woniper.board.errors.support.UserNotFoundException;
import net.woniper.board.errors.support.UsernameDuplicateException;
import net.woniper.board.service.UserService;
import net.woniper.board.support.dto.UserDto;
import net.woniper.board.test.config.TestDatabaseConfig;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.*;

/**
 * Created by woniper on 15. 2. 4..
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = {BoardApplication.class, TestDatabaseConfig.class})
@WebAppConfiguration
@IntegrationTest("server.port=8888")
@Transactional
public class UserServiceTest {

    @Autowired private UserService userService;

    private UserDto.Request admin;
    private UserDto.Request user;

    @Before
    public void setUp() throws Exception {
        admin = EntityBuilder.createUser(AuthorityType.ADMIN);
        user = EntityBuilder.createUser(AuthorityType.USER);
    }

    @Test(expected = UsernameDuplicateException.class)
    public void test_username_중복() throws Exception {
        // given
        UserDto.Request usernameDuplUser = EntityBuilder.createUser(AuthorityType.USER);

        // when
        userService.createUser(user);
        userService.createUser(usernameDuplUser);

        // then
        fail("username 중복 에러");
    }

    @Test(expected = NickNameDuplicateException.class)
    public void test_nickName_중복() throws Exception {
        // given
        UserDto.Request nickNameDuplUser = EntityBuilder.createUser(AuthorityType.USER);
        nickNameDuplUser.setUsername("duplUser");

        // when
        userService.createUser(user);
        userService.createUser(nickNameDuplUser);

        // then
        fail("nickName 중복 에러");
    }

    @Test
    public void test_회원등록_and_패스워드_암호화() throws Exception {
        // given
        User createUser = null;

        // when
        createUser = userService.createUser(this.user);

        // then
        assertNotEquals("service를 통한 회원등록 시 비밀번호 암호화",
                this.user.getPassword(), createUser.getPassword());
        assertEquals(this.user.getUsername(), createUser.getUsername());
        String fullName = this.user.getLastName() + " " + this.user.getFirstName();
        assertEquals(fullName, createUser.getFullName());
        assertEquals(this.user.getAuthorityType(), createUser.getAuthorityType());
    }

    @Test(expected = UserNotFoundException.class)
    public void test_Not_Found_User_Name_Exception() throws Exception {
        // given
        String username = "notUser";

        // when
        userService.getUser(username);

        // then
        fail("Not Found User Name : " + username);
    }

    @Test(expected = UserNotFoundException.class)
    public void test_Not_Foun_User_Id_Exception() throws Exception {
        // given
        Long userId = Long.MAX_VALUE;

        // when
        userService.getUser(userId);

        // then
        fail("Not Foun User Id : " + userId);
    }
}