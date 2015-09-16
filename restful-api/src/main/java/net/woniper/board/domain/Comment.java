package net.woniper.board.domain;

import lombok.Getter;
import lombok.Setter;
import net.woniper.board.support.dto.CommentDto;
import org.apache.commons.lang3.StringUtils;

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

    @Transient
    public void patch(CommentDto commentDto) {
        String content = commentDto.getContent();
        if(StringUtils.isNotEmpty(content))
            setContent(content);
    }

    @Transient
    public void update(CommentDto commentDto) {
        setContent(commentDto.getContent());
    }
}
