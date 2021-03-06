package net.woniper.board.test.service;

import net.woniper.board.builder.EntityBuilder;
import net.woniper.board.domain.User;
import net.woniper.board.domain.type.AuthorityType;
import net.woniper.board.errors.support.NickNameDuplicateException;
import net.woniper.board.errors.support.UserNotFoundException;
import net.woniper.board.errors.support.UsernameDuplicateException;
import net.woniper.board.service.UserService;
import net.woniper.board.support.dto.UserDto;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.RequestMethod;

import static org.junit.Assert.*;

/**
 * Created by woniper on 15. 2. 4..
 */
public class UserServiceTest extends BaseServiceTest {

    @Autowired private UserService userService;

    private UserDto.Request adminDto;
    private UserDto.Request userDto;

    @Before
    public void setUp() throws Exception {
        adminDto = EntityBuilder.createUser(AuthorityType.ADMIN);
        userDto = EntityBuilder.createUser(AuthorityType.USER);
    }

    @Test(expected = UsernameDuplicateException.class)
    public void test_username_중복() throws Exception {
        // given
        UserDto.Request usernameDuplUser = EntityBuilder.createUser(AuthorityType.USER);

        // when
        userService.save(usernameDuplUser);

        // then
        fail("username 중복 에러");
    }

    @Test(expected = NickNameDuplicateException.class)
    public void test_nickName_중복() throws Exception {
        // given
        UserDto.Request nickNameDuplUser = EntityBuilder.createUser(AuthorityType.USER);
        nickNameDuplUser.setUsername("duplUser");

        // when
        userService.save(nickNameDuplUser);

        // then
        fail("nickName 중복 에러");
    }

    @Test
    public void test_회원등록_and_패스워드_암호화() throws Exception {
        // then
        assertNotEquals("service를 통한 회원등록 시 비밀번호 암호화",
                this.userDto.getPassword(), user.getPassword());
        assertEquals(this.userDto.getUsername(), user.getUsername());
        String fullName = this.userDto.getLastName() + " " + this.userDto.getFirstName();
        assertEquals(fullName, user.getFullName());
        assertEquals(this.userDto.getAuthorityType(), user.getAuthorityType());
    }

    @Test(expected = UserNotFoundException.class)
    public void test_Not_Found_User_Name_Exception() throws Exception {
        // given
        String username = "notUser";

        // when
        userService.find(username);

        // then
        fail("Not Found User Name : " + username);
    }

    @Test(expected = UserNotFoundException.class)
    public void test_Not_Foun_User_Id_Exception() throws Exception {
        // given
        Long userId = Long.MAX_VALUE;

        // when
        userService.find(userId);

        // then
        fail("Not Foun User Id : " + userId);
    }

    @Test(expected = AccessDeniedException.class)
    public void test_user가_다른회원_조회() throws Exception {
        // when
        userService.find(admin.getUserId(), user.getUsername());

        // then
        fail("조회 불가");
    }

    @Test
    public void test_user_self_조회() throws Exception {
        // when
        User selectUser = userService.find(user.getUserId(), user.getUsername());

        // then
        assertEquals(user.getUserId(), selectUser.getUserId());
        assertEquals(user.getUsername(), selectUser.getUsername());
    }

    @Test
    public void test_admin이_user_조회() throws Exception {
        // when
        User selectUser = userService.find(user.getUserId(), admin.getUsername());

        // then
        assertEquals(user.getUserId(), selectUser.getUserId());
        assertEquals(user.getUsername(), selectUser.getUsername());
    }

    @Test(expected = AccessDeniedException.class)
    public void test_회원_정보_수정_accessDenied() throws Exception {
        // given
        userDto.setPassword("tt11");
        userDto.setNickName("woniper");
        userDto.setFirstName("won");
        userDto.setLastName("lee");

        // when
        userService.update(userDto, admin.getUsername(), RequestMethod.PUT.toString());

        // then
        fail("회원 수정 불가");
    }

    @Test
    public void test_회원수정_patch_null() throws Exception {
        // given
        userDto.setPassword(null);
        userDto.setNickName("updateNickName");

        // when
        User updateUser = userService.update(userDto, user.getUsername(), RequestMethod.PATCH.toString());

        // then
        assertNotNull(updateUser.getPassword());
        assertEquals(userDto.getNickName(), updateUser.getNickName());
    }

    @Test
    public void test_회원수정_patch_not_null() throws Exception {
        // given
        userDto.setPassword("updatePass");
        userDto.setNickName("updateNickName");

        // when
        User updateUser = userService.update(userDto, user.getUsername(), RequestMethod.PATCH.toString());

        // then
        assertEquals(userDto.getPassword(), updateUser.getPassword());
        assertEquals(userDto.getNickName(), updateUser.getNickName());
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_회원수정_update_null() throws Exception {
        // given
        userDto.setPassword(null);
        userDto.setNickName("updateNickName");

        // when
        userService.update(userDto, user.getUsername(), RequestMethod.PUT.toString());

        // then
        fail("IllegalArgumentException");
    }

    @Test
    public void test_회원수정_update_not_null() throws Exception {
        // given
        userDto.setPassword("tt11");
        userDto.setNickName("updateNickName");
        userDto.setFirstName("won");
        userDto.setLastName("lee");

        // when
        User updateUser = userService.update(userDto, user.getUsername(), RequestMethod.PUT.toString());

        // then
        assertEquals(userDto.getUsername(), updateUser.getUsername());
        assertEquals(userDto.getPassword(), updateUser.getPassword());
        assertEquals(userDto.getNickName(), updateUser.getNickName());
        assertEquals(userDto.getFirstName(), updateUser.getFirstName());
        assertEquals(userDto.getLastName(), updateUser.getLastName());
    }

    @Test
    public void test_회원탈퇴() throws Exception {
        // when
        boolean isDelete = userService.delete(user.getUsername());

        // then
        assertTrue(isDelete);
        assertEquals(false, user.isActive());

    }
}