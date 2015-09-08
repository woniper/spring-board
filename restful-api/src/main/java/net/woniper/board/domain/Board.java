package net.woniper.board.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import net.woniper.board.support.dto.BoardDto;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
* Created by woniper on 15. 1. 26..
*/
@Entity(name = "board")
@Getter @Setter
public class Board implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long boardId;

    @Column(nullable = false, length = 50)
    private String title;

    @Lob
    @Column(nullable = false)
    private String content;

    private int readCount;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate = new Date();

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    private User user;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "board", cascade = CascadeType.ALL)
    private List<Comment> comments = new ArrayList<>();

    public Board() {}

    public Board(String title, String content) {
        setTitle(title);
        setContent(content);
    }

    public void setUser(User user) {
        if(this.user != null) {
            this.user.getBoards().remove(this);
        }
        this.user = user;
        user.getBoards().add(this);
    }

    public int commentCount() {
        return comments != null ? comments.size() : 0;
    }

    public void update(BoardDto boardDto) {
        setTitle(boardDto.getTitle());
        setContent(boardDto.getContent());
    }

    public void read() {
        readCount++;
    }
}