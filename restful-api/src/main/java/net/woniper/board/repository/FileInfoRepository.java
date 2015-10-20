package net.woniper.board.repository;

import net.woniper.board.domain.FileInfo;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by woniper on 2015. 10. 20..
 */
public interface FileInfoRepository extends JpaRepository<FileInfo, Long> {

    FileInfo findByFileName(String fileName);

}
