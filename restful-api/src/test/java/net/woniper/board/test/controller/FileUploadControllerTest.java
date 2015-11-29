package net.woniper.board.test.controller;

import net.woniper.board.component.FileManagerImpl;
import net.woniper.board.config.SecurityUserDetails;
import net.woniper.board.support.dto.FileDto;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.fileUpload;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by woniper on 2015. 11. 29..
 */
public class FileUploadControllerTest extends BaseControllerTest {

    @Autowired private FileManagerImpl fileManager;

    @Value("${file.upload.path}")
    private String filePath;

    private MockMultipartFile mockMultipartFile;
    private FileDto.Response fileDto;

    @Before
    public void setUp() throws Exception {
        fileManager.setIsDailyFolder(false);
        fileManager.setFilePath(filePath + "test/");
        mockMultipartFile = new MockMultipartFile("mockFile.jpg", "orgMockFile.jpg", null, "bar".getBytes());
        fileDto = fileManager.saveFiles(Arrays.asList(mockMultipartFile)).get(0);
    }

    @Test
    public void test_uploads() throws Exception {
        // given
        List<MockMultipartFile> mockMultipartFiles = Arrays.asList(mockMultipartFile);

        // when
        ResultActions result = mock.perform(fileUpload("/uploads").file("file", objectMapper.writeValueAsBytes(mockMultipartFiles))
                .with(user(new SecurityUserDetails(user))));

        // then
        result.andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void test_update() throws Exception {
        // given
        String orgFileName = fileDto.getFileName();
        MockMultipartFile updateMultipartFile = new MockMultipartFile("updateFile.png", "orgUpdateFile.png", null, "bar".getBytes());

        // when
        ResultActions result = mock.perform(fileUpload("/file-update")
                .file("file", objectMapper.writeValueAsBytes(updateMultipartFile))
                .param("fileName", orgFileName)
                .with(user(new SecurityUserDetails(user))));

        // then
        result.andDo(print())
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.oldFileName", is(orgFileName)));
    }
}
