package demo.Board;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * Created by woniper on 15. 4. 27..
 */
@Entity(name = "board")
@Data
public class Board {

    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer boardId;

    private String content;
}
