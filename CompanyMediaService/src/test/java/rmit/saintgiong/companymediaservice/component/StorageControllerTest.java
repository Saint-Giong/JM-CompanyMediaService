//package rmit.saintgiong.companymediaservice.component;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.http.MediaType;
//import org.springframework.mock.web.MockMultipartFile;
//import org.springframework.test.context.bean.override.mockito.MockitoBean;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.MvcResult;
//import rmit.saintgiong.mediaservice.JmCompanyMediaApplication;
//import rmit.saintgiong.mediaapi.internal.common.dto.request.UploadStorageRequestDto;
//import rmit.saintgiong.mediaapi.internal.common.dto.response.UploadStorageResponseDto;
//import rmit.saintgiong.mediaapi.internal.services.UploadStorageInterface;
//
//import java.util.UUID;
//
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.ArgumentMatchers.eq;
//import static org.mockito.Mockito.times;
//import static org.mockito.Mockito.verify;
//import static org.mockito.Mockito.when;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.asyncDispatch;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.request;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//@SpringBootTest(classes = JmCompanyMediaApplication.class)
//@AutoConfigureMockMvc
//@DisplayName("Storage Controller Tests")
//class StorageControllerTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    private final ObjectMapper objectMapper = new ObjectMapper();
//
//    @MockitoBean
//    private UploadStorageInterface uploadStorageService;
//
//    @Test
//    @DisplayName("Should upload company media and return url (async)")
//    void testUpload_Success() throws Exception {
//        String companyId = UUID.randomUUID().toString();
//        UploadStorageResponseDto resp = UploadStorageResponseDto.builder()
//                .isSuccess(true)
//                .url("company-media/company/" + companyId + "/x.png")
//                .build();
//
//        when(uploadStorageService.uploadCompanyMedia(eq(companyId), any(byte[].class), any(), any()))
//                .thenReturn(resp);
//
//        MockMultipartFile metaPart = new MockMultipartFile(
//                "meta",
//                "meta.json",
//                MediaType.APPLICATION_JSON_VALUE,
//                objectMapper.writeValueAsBytes(UploadStorageRequestDto.builder().companyId(companyId).build())
//        );
//        MockMultipartFile filePart = new MockMultipartFile(
//                "file",
//                "x.png",
//                MediaType.IMAGE_PNG_VALUE,
//                "<<png>>".getBytes()
//        );
//
//        MvcResult result = mockMvc.perform(multipart("/storage/upload")
//                        .file(metaPart)
//                        .file(filePart))
//                .andExpect(request().asyncStarted())
//                .andReturn();
//
//        mockMvc.perform(asyncDispatch(result))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.success").value(true))
//                .andExpect(jsonPath("$.url").value(resp.getUrl()));
//
//        verify(uploadStorageService, times(1)).uploadCompanyMedia(eq(companyId), any(byte[].class), any(), any());
//    }
//}
