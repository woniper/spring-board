package net.woniper.board.service;

import net.woniper.board.domain.User;
import net.woniper.board.repository.UserRepository;
import net.woniper.board.support.dto.UserDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Created by woniper on 15. 4. 6..
 */
public interface UserService {

    void setUserRepository(UserRepository userRepository);

    User createUser(UserDto.Request userDto);

    User getUser(String username);

    User getUser(Long userId);

    Page<User> getUser(Pageable pageable);

    User getUser(Long userId, String username);

    boolean isDuplicationUserName(String username);

    boolean isDuplicationNickName(String nickName);

    User updateUser(UserDto.Request userDto, String username, String method);

    boolean deleteUser(String username);
}
