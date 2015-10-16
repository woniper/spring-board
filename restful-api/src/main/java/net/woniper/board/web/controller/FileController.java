package net.woniper.board.web.controller;

import lombok.extern.slf4j.Slf4j;
import net.woniper.board.component.FileManager;
import net.woniper.board.support.dto.FileDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;

/**
 * Created by woniper on 2015. 10. 16..
 */
@Slf4j
@RestController
public class FileController {

    @Autowired private FileManager fileManager;

    @RequestMapping(value = "/download", method = RequestMethod.GET)
    public ResponseEntity<?> download(@RequestParam("fileName") String fileName) {
        File file = fileManager.getFile(fileName);
        if(file != null) {
            try {
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.parseMediaType("application/x-msdownload"));
                headers.setContentLength(file.length());
                headers.setContentDispositionFormData("attachment", file.getName());

                return ResponseEntity.ok().headers(headers)
                        .body(new InputStreamResource(new FileInputStream(file)));
            } catch (FileNotFoundException e) {
                log.error("=======> Not Found File");
                e.printStackTrace();
            }
        }

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @RequestMapping(value = "/uploads", method = RequestMethod.POST)
    public ResponseEntity<?> uploadFiles(@RequestParam("file") List<MultipartFile> files) {
        List<FileDto> fileDtos = fileManager.saveFiles(files);
        if(fileDtos != null && !fileDtos.isEmpty())
            return ResponseEntity.ok(fileDtos);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @RequestMapping(value = "/file-update", method = RequestMethod.POST)
    public ResponseEntity<?> updateFile(@RequestParam("fileName") String oldFileName,
                                        @RequestParam("file") MultipartFile file) {
        FileDto fileDto = fileManager.updateFile(oldFileName, file);
        if(fileDto != null) {
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(fileDto);
        }
        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
    }


}
