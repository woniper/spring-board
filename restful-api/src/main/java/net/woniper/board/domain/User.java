package net.woniper.board.domain;

import lombok.Data;
import net.woniper.board.domain.type.AuthorityType;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Created by woniper on 15. 1. 28..
 */
@Entity(name = "user")
@Data
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

//    private boolean admin = false;

    @Enumerated(EnumType.STRING)
    private AuthorityType authorityType;

    private boolean active = true;

    @Temporal(TemporalType.TIMESTAMP)
    private Date joinDate = new Date();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    private List<Board> boards;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    private List<Comment> comments;

    public int boardCount() {
        return boards != null ? boards.size() : 0;
    }

    public int commentCount() {
        return comments != null ? comments.size() : 0;
    }

}
