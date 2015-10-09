package net.woniper.board.test.service;

import net.woniper.board.BoardApplication;
import net.woniper.board.test.config.TestDatabaseConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.assertNotNull;

/**
 * Created by woniper on 15. 2. 4..
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = {BoardApplication.class, TestDatabaseConfig.class})
@IntegrationTest("server.port=8888")
@WebAppConfiguration
@Transactional
public class BaseServiceTest {

    @Autowired protected ModelMapper modelMapper;

    @Test
    public void testContextLoader() throws Exception {
        assertNotNull(modelMapper);
    }
}