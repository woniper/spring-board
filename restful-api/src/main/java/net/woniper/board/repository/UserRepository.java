package net.woniper.board.repository;

import net.woniper.board.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by woniper on 15. 1. 28..
 */
public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
    User findByNickName(String nickName);
}
