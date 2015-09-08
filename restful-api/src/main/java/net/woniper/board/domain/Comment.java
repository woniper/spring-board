package net.woniper.board.domain;

import lombok.Getter;
import lombok.Setter;
import net.woniper.board.support.dto.CommentDto;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by woniper on 15. 2. 4..
 */
@Entity(name = "comment")
@Getter @Setter
public class Comment implements Serializable {

    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private Long commentId;

    private String content;

    private int depth;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate = new Date();

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Board board;

    public void setBoard(Board board) {
        if(this.board != null) {
            this.board.getComments().remove(this);
        }
        this.board = board;
        board.getComments().add(this);
    }

    public void update(CommentDto commentDto) {
        setContent(commentDto.getContent());
    }
}
