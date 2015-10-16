package net.woniper.board.component;

import net.woniper.board.support.dto.FileDto;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;

/**
 * Created by woniper on 2015. 10. 15..
 */
public interface FileManager {

    List<FileDto> saveFiles(List<MultipartFile> files);

    FileDto updateFile(String oldFileName, MultipartFile file);

    File getFile(String fileName);

}
