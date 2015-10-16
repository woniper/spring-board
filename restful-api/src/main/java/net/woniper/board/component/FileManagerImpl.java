package net.woniper.board.component;

import lombok.extern.slf4j.Slf4j;
import net.woniper.board.support.dto.FileDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by woniper on 2015. 10. 10..
 */
@Slf4j
@Component
public class FileManagerImpl implements FileManager {

    @Value("${file.upload.path}")
    private String filePath;

    @Override
    public List<FileDto> saveFiles(List<MultipartFile> files) {
        Assert.notNull(files);
        mkdir();
        List<FileDto> fileDtos = new ArrayList<>();
        for (MultipartFile file : files) {
            String fileName = getFileName(file.getOriginalFilename());
            try (BufferedOutputStream stream = new BufferedOutputStream(
                    new FileOutputStream(new File(filePath + fileName)))) {
                stream.write(file.getBytes());

                FileDto fileDto = new FileDto();
                fileDto.setFileName(file.getOriginalFilename());
                fileDto.setFileSize(file.getSize());
                fileDto.setOldFileName(file.getOriginalFilename());
                fileDto.setOldFileSize(file.getSize());

                log.info("=======> upload file info : {}", fileDto.toString());

                fileDtos.add(fileDto);
            } catch (IOException e) {
                log.error("=======> file upload fail, file name : {}, size : {}", fileName, file.getSize());
                e.printStackTrace();
            }
        }
        return fileDtos;
    }

    @Override
    public FileDto updateFile(String oldFileName, MultipartFile file) {
        Assert.notNull(oldFileName);
        Assert.notNull(file);

        File oldFile = new File(filePath + oldFileName);

        if(oldFile.exists()) {
            String fileName = getFileName(file.getOriginalFilename());
            try(BufferedOutputStream stream = new BufferedOutputStream(
                    new FileOutputStream(new File(filePath + fileName)))) {
                stream.write(file.getBytes());

                FileDto fileDto = new FileDto();
                fileDto.setFileName(file.getOriginalFilename());
                fileDto.setFileSize(file.getSize());
                fileDto.setOldFileName(oldFile.getName());
                fileDto.setOldFileSize(oldFile.length());

                log.info("=======> update file info :{}", fileDto.toString());

                oldFile.delete();
                return fileDto;
            } catch (IOException e) {
                log.error("=======> file update fail, file name : {}, size : {}", fileName, file.getSize());
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    public File getFile(String fileName) {
        return new File(filePath + fileName);
    }

    /**
     * file upload 경로 폴더 생성
     */
    private void mkdir() {
        File file = new File(filePath);
        if(!file.exists()) {
            file.mkdirs();
            log.info("=======> upload File Path Directory mkdir!!");
        }
    }

    private String getFileName(String originalFileName) {
        return System.currentTimeMillis() + "_" + originalFileName;
    }
}
