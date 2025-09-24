package com.khoahd7621.youngblack.controllers.admin;

import com.khoahd7621.youngblack.App;
import com.khoahd7621.youngblack.dtos.response.SuccessResponse;
import com.khoahd7621.youngblack.dtos.response.image.UploadImageResponse;
import com.khoahd7621.youngblack.repositories.UserRepository;
import com.khoahd7621.youngblack.security.CorsConfig;
import com.khoahd7621.youngblack.security.WebSecurityConfig;
import com.khoahd7621.youngblack.services.UploadImageService;
import com.khoahd7621.youngblack.utils.JwtTokenUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.when;
import static org.hamcrest.core.Is.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UploadImageController.class)
@ContextConfiguration(classes = {App.class, WebSecurityConfig.class, CorsConfig.class})
@AutoConfigureMockMvc(addFilters = false)
class UploadImageControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private UploadImageService uploadImageService;
    @MockBean
    private JwtTokenUtil jwtTokenUtil;
    @MockBean
    private UserRepository userRepository;

    private UploadImageResponse uploadImageResponse;

    @BeforeEach
    void beforeEach() {
        uploadImageResponse = UploadImageResponse.builder().imageName("imageName").imageUrl("imageUrl").build();
    }

    @Test
    void uploadSingleImage_ShouldReturnSuccess() throws Exception {
        MultipartFile file = new MockMultipartFile("image-name", "image-name.png",
                MediaType.IMAGE_PNG_VALUE, "image data".getBytes());
        SuccessResponse<UploadImageResponse> expected = new SuccessResponse<>(uploadImageResponse, "message");

        when(uploadImageService.uploadSingleImage(file)).thenReturn(expected);

        MockHttpServletResponse actual = mockMvc.perform(multipart(HttpMethod.POST, "/api/v1/admin/image/upload")
                        .file("file", file.getBytes())
                        .contentType(MediaType.MULTIPART_FORM_DATA_VALUE))
                .andExpect(status().isOk())
                .andReturn().getResponse();

        assertThat(actual.getContentAsString(), is(""));
    }

    @Test
    void uploadMultiImages_ShouldReturnSuccess() throws Exception {
        MultipartFile file1 = new MockMultipartFile("image-name-1", "image-1.png",
                MediaType.IMAGE_PNG_VALUE, "image data".getBytes());
        MultipartFile file2 = new MockMultipartFile("image-name-2", "image-2.png",
                MediaType.IMAGE_PNG_VALUE, "image data".getBytes());
        List<MultipartFile> multipartFileList = new ArrayList<>();
        multipartFileList.add(file1);
        multipartFileList.add(file2);
        List<UploadImageResponse> responseData = new ArrayList<>();
        responseData.add(UploadImageResponse.builder().imageName("imageName1").imageUrl("imageUrl1").build());
        responseData.add(UploadImageResponse.builder().imageName("imageName2").imageUrl("imageUrl2").build());
        SuccessResponse<List<UploadImageResponse>> expected = new SuccessResponse<>(responseData, "message");

        when(uploadImageService.uploadMultiImages(multipartFileList)).thenReturn(expected);

        MvcResult actual = mockMvc.perform(multipart(HttpMethod.POST, "/api/v1/admin/image/upload-multiple")
                        .file("file", file1.getBytes())
                        .file("file", file2.getBytes())
                        .contentType(MediaType.MULTIPART_FORM_DATA_VALUE))
                .andExpect(status().isOk())
                .andReturn();

        assertThat(actual.getResponse().getContentAsString(), is(""));
    }
}