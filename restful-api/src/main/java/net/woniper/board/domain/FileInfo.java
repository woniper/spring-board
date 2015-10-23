package net.woniper.board.domain;

import lombok.*;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.Serializable;

/**
 * Created by woniper on 2015. 10. 18..
 */
@Entity
@Slf4j
@Getter @Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class FileInfo implements Serializable {

    @Id @GeneratedValue
    private Long fileId;

    @Column(unique = true)
    private String fileName;

    private Long fileSize;

    private boolean dailyFolder;

    private String dailyFolderPath;

    public String getFileFullPath() {
        return isDailyFolder() ? getDailyFolderPath() + getFileName() : getFileName();
    }

}
