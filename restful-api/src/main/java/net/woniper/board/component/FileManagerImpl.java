package net.woniper.board.component;

import lombok.extern.slf4j.Slf4j;
import net.woniper.board.domain.FileInfo;
import net.woniper.board.repository.FileInfoRepository;
import net.woniper.board.support.dto.FileDto;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by woniper on 2015. 10. 10..
 */
@Slf4j
@Component
public class FileManagerImpl implements FileManager {

    private String filePath;

    private boolean isDailyFolder;

    @Autowired private FileInfoRepository fileInfoRepository;
    @Autowired private ModelMapper modelMapper;

    @Value("${file.upload.path}")
    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    @Value("${file.upload.daily.folder}")
    public void setIsDailyFolder(boolean isDailyFolder) {
        this.isDailyFolder = isDailyFolder;
    }

    @Override
    public List<FileDto.Response> saveFiles(List<MultipartFile> files) {
        Assert.notNull(files);
        mkdir();
        List<FileDto.Response> fileDtos = new ArrayList<>();
        for (MultipartFile file : files) {
            String fileName = createFileName(file.getOriginalFilename());
            try (BufferedOutputStream stream = new BufferedOutputStream(
                    new FileOutputStream(new File(getSaveFolderPath() + fileName)))) {
                stream.write(file.getBytes());

                FileDto.Response fileDto = new FileDto.Response();
                fileDto.setFileName(fileName);
                fileDto.setFileSize(file.getSize());
                fileDto.setOldFileName(fileName);
                fileDto.setOldFileSize(file.getSize());

                log.info("=======> upload file info : {}", fileDto.toString());

                fileDtos.add(fileDto);
                saveFileInfo(fileDto);
            } catch (IOException e) {
                log.error("=======> file upload fail, file name : {}, size : {}", fileName, file.getSize());
                e.printStackTrace();
            }
        }
        return fileDtos;
    }

    @Override
    public FileDto.Response updateFile(String oldFileName, MultipartFile file) {
        Assert.notNull(oldFileName);
        Assert.notNull(file);

        File oldFile = new File(filePath + oldFileName);

        if(oldFile.exists()) {
            String fileName = createFileName(file.getOriginalFilename());
            try(BufferedOutputStream stream = new BufferedOutputStream(
                    new FileOutputStream(new File(getSaveFolderPath() + fileName)))) {
                stream.write(file.getBytes());

                FileDto.Response fileDto = new FileDto.Response();
                fileDto.setFileName(fileName);
                fileDto.setFileSize(file.getSize());
                fileDto.setOldFileName(oldFileName);
                fileDto.setOldFileSize(oldFile.length());

                log.info("=======> update file info :{}", fileDto.toString());

                oldFile.delete();
                saveFileInfo(fileDto);

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
        FileInfo fileInfo = fileInfoRepository.findByFileName(fileName);
        return fileInfo.isDailyFolder() ? new File(filePath + fileInfo.getfileFullPath())
                : new File(fileInfo.getfileFullPath());
    }

    /**
     * file upload 경로 폴더 생성
     */
    private void mkdir() {
        File file = new File(getSaveFolderPath());
        if(!file.exists()) {
            file.mkdirs();
            log.info("=======> upload File Path Directory mkdir!!");
        }
    }

    private void saveFileInfo(FileDto.Response fileDto) {
        FileInfo fileInfo = modelMapper.map(fileDto, FileInfo.class);
        fileInfo.setDailyFolder(isDailyFolder);
        if(isDailyFolder)
            fileInfo.setDailyFolderPath(getDailyFolderPath());
        fileInfoRepository.save(fileInfo);
    }

    /**
     * 저장위치
     * @return
     */
    private String getSaveFolderPath() {
        return isDailyFolder ? filePath + getDailyFolderPath() : filePath;
    }

    private String getDailyFolderPath() {
        Calendar calendar = Calendar.getInstance();
        return String.format("%04d%s%02d%s%02d%s", calendar.get(Calendar.YEAR), File.separator
                , calendar.get(Calendar.MONTH) + 1, File.separator
                , calendar.get(Calendar.DAY_OF_MONTH), File.separator);
    }

    /**
     * 파일 이름 생성
     * @param fileName
     * @return
     */
    private String createFileName(String fileName) {
        return System.currentTimeMillis() + "_" + fileName;
    }
}
