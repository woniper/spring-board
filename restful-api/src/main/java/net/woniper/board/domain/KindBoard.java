package net.woniper.board.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by woniper on 2015. 10. 30..
 */
@Entity
@Getter @Setter
@NoArgsConstructor
public class KindBoard {

    @Id @GeneratedValue
    private Long kindBoardId;

    @Column(unique = true, nullable = false)
    private String kindBoardName;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;

    @Column(nullable = false)
    private boolean enable = true;

    public KindBoard(String kindBoardName) {
        setKindBoardName(kindBoardName);
        this.createDate = Calendar.getInstance().getTime();
    }

}
