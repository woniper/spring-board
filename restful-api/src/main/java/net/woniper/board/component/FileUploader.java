package net.woniper.board.component;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * Created by woniper on 2015. 10. 15..
 */
public interface FileUploader {

    List<String> uploads(List<MultipartFile> files);
}
