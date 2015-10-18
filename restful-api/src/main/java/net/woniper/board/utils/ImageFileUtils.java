package net.woniper.board.utils;

import org.apache.commons.io.FilenameUtils;
import org.springframework.http.MediaType;

/**
 * Created by woniper on 2015. 10. 18..
 */
public class ImageFileUtils {

    public static MediaType getMediaType(String fileName) {
        String extension = FilenameUtils.getExtension(fileName);

        switch (extension.toLowerCase()) {
            case "png" :
                return MediaType.IMAGE_PNG;

            case "jpg" :
            case "jpeg" :
                return MediaType.IMAGE_JPEG;

            case "gif" :
                return MediaType.IMAGE_GIF;

            default :
                return MediaType.IMAGE_JPEG;
        }
    }
}
