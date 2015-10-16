package net.woniper.board.component;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;

/**
 * Created by woniper on 2015. 10. 15..
 */
public interface FileManager {

    List<String> uploads(List<MultipartFile> files);

    String update(String oldFileName, MultipartFile file);

    File download(String fileName);
}
