package net.woniper.board.test.domain;

import net.woniper.board.domain.User;
import org.junit.Before;
import org.junit.Test;

import static net.woniper.board.domain.type.AuthorityType.ADMIN;
import static net.woniper.board.domain.type.AuthorityType.USER;

/**
 * Created by woniper on 15. 9. 25..
 */
public class UserTest {

    User admin;
    User user;

    @Before
    public void setUp() throws Exception {
        admin = new User("username", "password", "fistName", "lastName", "nickName", ADMIN, true);
        user = new User("username", "password", "fistName", "lastName", "nickName", USER, true);
    }

    @Test
    public void testName() throws Exception {

    }
}
