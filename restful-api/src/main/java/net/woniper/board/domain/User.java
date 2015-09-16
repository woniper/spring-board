package net.woniper.board.domain;

import lombok.Getter;
import lombok.Setter;
import net.woniper.board.domain.type.AuthorityType;
import net.woniper.board.support.dto.UserDto;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by woniper on 15. 1. 28..
 */
@Entity(name = "user")
@Getter @Setter
public class User implements Serializable {

    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private Long userId;

    @Column(unique = true, nullable = false)
    private String username;

    private String password;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(unique = true, nullable = false)
    private String nickName;

    @Enumerated(EnumType.STRING)
    private AuthorityType authorityType;

    private boolean active = true;

    @Temporal(TemporalType.TIMESTAMP)
    private Date joinDate = new Date();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    private List<Board> boards = new ArrayList<>();

    public User() {}

    public User(String username, String password, String firstName, String lastName,
                String nickName, AuthorityType authorityType, boolean active) {
        setUsername(username);
        setPassword(password);
        setFirstName(firstName);
        setLastName(lastName);
        setNickName(nickName);
        setAuthorityType(authorityType);
        setActive(active);
    }

    @Transient
    public int getBoardCount() {
        return boards != null ? boards.size() : 0;
    }

    @Transient
    public String getFullName() {
        return getLastName() + " " + getFirstName();
    }

    @Transient
    public void patch(UserDto.Request userDto) {
        String password = userDto.getPassword();
        String nickName = userDto.getNickName();
        String firstName = userDto.getFirstName();
        String lastName = userDto.getLastName();

        if(StringUtils.isNotEmpty(password))
            setPassword(password);
        if(StringUtils.isNotEmpty(nickName))
            setNickName(nickName);
        if(StringUtils.isNotEmpty(firstName))
            setFirstName(firstName);
        if(StringUtils.isNotEmpty(lastName))
            setLastName(lastName);
    }

    @Transient
    public void update(UserDto.Request userDto) {
        setPassword(userDto.getPassword());
        setNickName(userDto.getNickName());
        setFirstName(userDto.getFirstName());
        setLastName(userDto.getLastName());
    }
}
