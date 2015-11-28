package net.woniper.board.test.service;

import net.woniper.board.component.FileManagerImpl;
import net.woniper.board.support.dto.FileDto;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mock.web.MockMultipartFile;

import java.io.File;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;

/**
 * Created by woniper on 15. 2. 4..
 */
public class FileManagerTest extends BaseServiceTest {

    @Autowired
    private FileManagerImpl fileManager;

    @Value("${file.upload.path}")
    private String filePath;

    private FileDto.Response fileDto;
    private MockMultipartFile multipartFile;

    @Before
    public void setUp() throws Exception {
        fileManager.setIsDailyFolder(false);
        fileManager.setFilePath(filePath + "test/");
        multipartFile = new MockMultipartFile("mockFile.jpg", "orgMockFile.jpg", null, "bar".getBytes());
        fileDto = fileManager.saveFiles(Arrays.asList(multipartFile)).get(0);
    }

    @Test
    public void test_updateFile() throws Exception {
        // given
        String oldFileName = fileDto.getFileName();
        MockMultipartFile updateFile = new MockMultipartFile("updateMockFile.png", "orgUpdateMockFile.png", null,  "bar".getBytes());

        // when
        FileDto.Response fileResponse = fileManager.updateFile(oldFileName, updateFile);

        // then
        assertEquals(fileDto.getFileName(), fileResponse.getOldFileName());
        assertEquals(fileDto.getFileSize(), fileResponse.getFileSize());
    }

    @Test
    public void test_getFile() throws Exception {
        // given
        String fileName = fileDto.getFileName();

        // when
        File file = fileManager.getFile(fileName);

        // then
        assertEquals(file.getName(), fileName);
    }
}