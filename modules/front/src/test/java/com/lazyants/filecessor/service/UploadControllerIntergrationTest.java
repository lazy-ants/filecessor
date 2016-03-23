package com.lazyants.filecessor.service;

import com.lazyants.filecessor.Application;
import com.lazyants.filecessor.configuration.BaseApplicationConfiguration;
import com.lazyants.filecessor.configuration.BaseRabbitConfig;
import com.lazyants.filecessor.controller.UploadController;
import com.lazyants.filecessor.model.PhotoFile;
import com.lazyants.filecessor.model.PhotoRepository;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@TestPropertySource(properties = {"application.media-directory-path = test_data/media/"})
public class UploadControllerIntergrationTest {

    @Autowired
    private PhotoRepository photoRepository;

    @Autowired
    private BaseApplicationConfiguration configuration;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private BaseRabbitConfig rabbitConfiguration;

    @Autowired
    private ImageDownloader imageDownloader;

    private String testRoot = "test_data";

    private File testImagesDir = new File(testRoot + "/images");

    private File mediaDir = new File(testRoot + "/media/");

    private MockMvc mockMvc;

    @Before
    public void setUp() {
        if (!testImagesDir.exists() && !testImagesDir.mkdirs() || !mediaDir.exists() && !mediaDir.mkdirs()) {
            throw new RuntimeException("temp directory cannot be created");
        }
        mockMvc = MockMvcBuilders.standaloneSetup(new UploadController(photoRepository,rabbitTemplate, configuration,
                rabbitConfiguration, imageDownloader)).build();
    }

    @Test
    public void test_upload() throws Exception {
        File testImage = copyTestFile("src/test/resources/images/nikon.jpg");

        PhotoFile file = new PhotoFile();
        file.setFileName("nikon.jpg");
        file.setFileContentType("image/jpeg");
        file.setFileSize(testImage.getTotalSpace());
        file.setFilePath(testImage.getAbsolutePath());

        mockMvc.perform(post("/upload")
//                .params(PhotoFileConverter.convert(file))
                .contentType(MediaType.MULTIPART_FORM_DATA)
        )
                .andExpect(status().isCreated())
                .andReturn();
    }

    private File copyTestFile(String path) throws IOException {
        File from = new File(path);
        File to = new File(testImagesDir.getAbsolutePath() + "/test_image.jpg");
        Files.copy(from.toPath(), to.toPath(), StandardCopyOption.REPLACE_EXISTING);

        return to;
    }

    @After
    public void dropAll() throws IOException {
        FileUtils.deleteDirectory(new File(testRoot));
    }
}
