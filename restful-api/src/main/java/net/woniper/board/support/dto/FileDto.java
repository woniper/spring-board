package net.woniper.board.support.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Created by woniper on 2015. 10. 16..
 */
@Getter @Setter
@ToString
public class FileDto {

    private String fileName;
    private long fileSize;

    private String oldFileName;
    private long oldFileSize;

}
