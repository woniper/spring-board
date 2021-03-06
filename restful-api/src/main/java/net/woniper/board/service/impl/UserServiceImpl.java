package net.woniper.board.service.impl;

import lombok.extern.slf4j.Slf4j;
import net.woniper.board.domain.User;
import net.woniper.board.domain.type.AuthorityType;
import net.woniper.board.errors.support.NickNameDuplicateException;
import net.woniper.board.errors.support.UserNotFoundException;
import net.woniper.board.errors.support.UsernameDuplicateException;
import net.woniper.board.repository.UserRepository;
import net.woniper.board.component.MailAsyncSender;
import net.woniper.board.service.UserService;
import net.woniper.board.support.dto.UserDto;
import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by woniper on 15. 1. 28..
 */
@Service
@Transactional
@Slf4j
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;
    private MailAsyncSender mailAsyncSender;
    @Autowired private ModelMapper modelMapper;
    @Autowired private PasswordEncoder passwordEncoder;

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Autowired
    public void setMailAsyncSender(MailAsyncSender mailAsyncSender) {
        this.mailAsyncSender = mailAsyncSender;
    }

    @Override
    public User save(UserDto.Request userDto) {
        if(isDuplicationUserName(userDto.getUsername())) {
            throw new UsernameDuplicateException(userDto.getUsername());
        }

        if(isDuplicationNickName(userDto.getNickName())) {
            throw new NickNameDuplicateException(userDto.getNickName());
        }

        User user = modelMapper.map(userDto, User.class);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user = userRepository.save(user);
        mailAsyncSender.send("account " + user.getUsername(), user.toString());
        return user;
    }

    @Override
    public User find(String username) {
        User user = userRepository.findByUsername(username);

        if(user == null)
            throw new UserNotFoundException(username);

        return user;
    }

    @Override
    public User find(Long userId) {
        User user = userRepository.findOne(userId);

        if(user == null)
            throw new UserNotFoundException(String.valueOf(userId));

        return userRepository.findOne(userId);
    }

    @Override
    public Page<User> find(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    @Override
    public User find(Long userId, String username) {
        User user = find(username);
        User resultUser = find(userId);
        if(isAccessUser(user, resultUser)) {
            log.info("request User : {}, response User : {}", user, resultUser);
            return resultUser;
        }
        throw new AccessDeniedException("accessDenied " + username);
    }

    private boolean isDuplicationUserName(String username) {
        return userRepository.findByUsername(username) != null;
    }
    private boolean isDuplicationNickName(String nickName) {
        return userRepository.findByNickName(nickName) != null;
    }

    @Override
    public User update(UserDto.Request userDto, String username, String method) {
        if(!userDto.getUsername().equals(username))
            throw new AccessDeniedException("accessDenied" + username);

        User user = find(username);
        if(isDuplicationNickName(userDto.getNickName()))
            throw new NickNameDuplicateException(userDto.getNickName());

        String password = userDto.getPassword();
        if(StringUtils.isNotEmpty(password))
            userDto.setPassword(passwordEncoder.encode(password));

        if(RequestMethod.valueOf(method) == RequestMethod.PATCH)
            user.patch(userDto);
        else
            user.update(userDto);

        return user;
    }

    @Override
    public boolean delete(String username) {
        User user = userRepository.findByUsername(username);
        if(user != null) {
            user.setActive(false);
            return true;
        }
        return false;
    }

    private boolean isAccessUser(User user, User requestUser) {
        return (AuthorityType.ADMIN == user.getAuthorityType()) ||
               (user.getUserId().equals(requestUser.getUserId()) && user.isActive());
    }
}
