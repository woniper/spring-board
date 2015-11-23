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

    User save(UserDto.Request userDto);

    User find(String username);

    User find(Long userId);

    Page<User> find(Pageable pageable);

    User find(Long userId, String username);

    User update(UserDto.Request userDto, String username, String method);

    boolean delete(String username);
}
