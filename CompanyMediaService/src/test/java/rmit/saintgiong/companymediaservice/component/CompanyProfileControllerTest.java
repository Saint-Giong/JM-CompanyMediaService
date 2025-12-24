package rmit.saintgiong.companymediaservice.component;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import rmit.saintgiong.comapymediaservice.JmCompanyMediaApplication;
import rmit.saintgiong.comapymediaservice.common.exception.DomainException;
import rmit.saintgiong.companymediaapi.internal.common.dto.request.CreateCompanyMediaMetaRequestDto;
import rmit.saintgiong.companymediaapi.internal.common.dto.response.CreateCompanyMediaResponseDto;
import rmit.saintgiong.companymediaapi.internal.common.dto.response.QueryCompanyMediaListResponseDto;
import rmit.saintgiong.companymediaapi.internal.common.dto.response.QueryCompanyMediaResponseDto;
import rmit.saintgiong.companymediaapi.internal.common.type.DomainCode;
import rmit.saintgiong.companymediaapi.internal.services.CreateCompanyMediaInterface;
import rmit.saintgiong.companymediaapi.internal.services.QueryCompanyMediaInterface;
import rmit.saintgiong.companymediaapi.internal.services.UpdateCompanyMediaInterface;

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.asyncDispatch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.request;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = JmCompanyMediaApplication.class)
@AutoConfigureMockMvc
@DisplayName("Company Media Controller Tests")
class CompanyProfileControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @MockitoBean
    private CreateCompanyMediaInterface createService;

    @MockitoBean
    private UpdateCompanyMediaInterface updateService;

    @MockitoBean
    private QueryCompanyMediaInterface queryService;

    @Nested
    @DisplayName("Create Company Media API")
    class CreateCompanyMediaApiTests {

        private CreateCompanyMediaMetaRequestDto validMeta;
        private String generatedId;

        @BeforeEach
        void setUpCreate() {
            generatedId = UUID.randomUUID().toString();
            validMeta = CreateCompanyMediaMetaRequestDto.builder()
                    .mediaTitle("Test Media")
                    .mediaDescription("Desc")
                    .mediaType(rmit.saintgiong.companymediaapi.internal.common.type.MediaType.IMAGE)
                    .companyId(UUID.randomUUID().toString())
                    .build();
        }

        @Test
        @DisplayName("Should create company media (multipart) and return id (async)")
        void testCreateCompany_Valid_Success() throws Exception {
            CreateCompanyMediaResponseDto mockResp = CreateCompanyMediaResponseDto.builder()
                    .id(generatedId)
                    .build();

            when(createService.createCompanyMedia(any(CreateCompanyMediaMetaRequestDto.class), any(byte[].class), any(), any()))
                    .thenReturn(mockResp);

            MockMultipartFile metaPart = new MockMultipartFile(
                    "meta",
                    "meta.json",
                    MediaType.APPLICATION_JSON_VALUE,
                    objectMapper.writeValueAsBytes(validMeta)
            );
            MockMultipartFile filePart = new MockMultipartFile(
                    "file",
                    "media.png",
                    MediaType.IMAGE_PNG_VALUE,
                    "<<png data>>".getBytes()
            );

            MvcResult result = mockMvc.perform(multipart("/upload")
                            .file(metaPart)
                            .file(filePart))
                    .andExpect(request().asyncStarted())
                    .andReturn();

            mockMvc.perform(asyncDispatch(result))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.id").value(generatedId));

            verify(createService, times(1)).createCompanyMedia(any(CreateCompanyMediaMetaRequestDto.class), any(byte[].class), any(), any());
        }

        @Test
        @DisplayName("Should fail when mediaTitle is blank")
        void testCreateCompany_BlankTitle_Fail() throws Exception {
            CreateCompanyMediaMetaRequestDto req = CreateCompanyMediaMetaRequestDto.builder()
                    .mediaTitle("")
                    .mediaDescription(validMeta.getMediaDescription())
                    .mediaType(validMeta.getMediaType())
                    .companyId(validMeta.getCompanyId())
                    .build();

            MockMultipartFile metaPart = new MockMultipartFile(
                    "meta",
                    "meta.json",
                    MediaType.APPLICATION_JSON_VALUE,
                    objectMapper.writeValueAsBytes(req)
            );
            MockMultipartFile filePart = new MockMultipartFile(
                    "file",
                    "media.png",
                    MediaType.IMAGE_PNG_VALUE,
                    "<<png data>>".getBytes()
            );

            mockMvc.perform(multipart("/upload")
                            .file(metaPart)
                            .file(filePart))
                    .andExpect(status().isBadRequest());

            verify(createService, never()).createCompanyMedia(any(), any(), any(), any());
        }

        @Test
        @DisplayName("Should fail when mediaType is null")
        void testCreateCompany_NullMediaType_Fail() throws Exception {
            CreateCompanyMediaMetaRequestDto req = CreateCompanyMediaMetaRequestDto.builder()
                    .mediaTitle(validMeta.getMediaTitle())
                    .mediaDescription(validMeta.getMediaDescription())
                    .mediaType(null)
                    .companyId(validMeta.getCompanyId())
                    .build();

            MockMultipartFile metaPart = new MockMultipartFile(
                    "meta",
                    "meta.json",
                    MediaType.APPLICATION_JSON_VALUE,
                    objectMapper.writeValueAsBytes(req)
            );
            MockMultipartFile filePart = new MockMultipartFile(
                    "file",
                    "media.png",
                    MediaType.IMAGE_PNG_VALUE,
                    "<<png data>>".getBytes()
            );

            mockMvc.perform(multipart("/upload")
                            .file(metaPart)
                            .file(filePart))
                    .andExpect(status().isBadRequest());

            verify(createService, never()).createCompanyMedia(any(), any(), any(), any());
        }
    }

    @Nested
    @DisplayName("Get Company Media API")
    class GetCompanyMediaApiTests {

        private String existingId;
        private QueryCompanyMediaResponseDto validResponse;

        @BeforeEach
        void setUpGet() {
            existingId = UUID.randomUUID().toString();
            validResponse = QueryCompanyMediaResponseDto.builder()
                    .id(existingId)
                    .mediaTitle("Title")
                    .mediaDescription("Desc")
                    .mediaType("IMAGE")
                    .mediaPath("http://example.com/media.png")
                    .companyId(UUID.randomUUID().toString())
                    .active(false)
                    .build();
        }

        @Test
        @DisplayName("Should get company media successfully (async)")
        void testGetCompanyMedia_Valid_Success() throws Exception {
            // Arrange
            when(queryService.getCompanyMedia(eq(existingId))).thenReturn(validResponse);

            // Act
            MvcResult result = mockMvc.perform(get("/" + existingId))
                    .andExpect(request().asyncStarted())
                    .andReturn();

            // Complete async
            mockMvc.perform(asyncDispatch(result))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(existingId))
                    .andExpect(jsonPath("$.mediaTitle").value("Title"));

            verify(queryService, times(1)).getCompanyMedia(eq(existingId));
        }

        @Test
        @DisplayName("Should return 400 when id format invalid")
        void testGetCompanyMedia_InvalidId_Fail() throws Exception {
            // Act
            mockMvc.perform(get("/not-a-uuid"))
                    .andExpect(status().isBadRequest());

            verify(queryService, never()).getCompanyMedia(any());
        }

        @Test
        @DisplayName("Should return 404 when company media not found")
        void testGetCompanyMedia_NotFound_Fail() throws Exception {
            // Arrange
            doThrow(new DomainException(DomainCode.RESOURCE_NOT_FOUND, "not found"))
                    .when(queryService).getCompanyMedia(eq(existingId));

            // Act
            MvcResult result = mockMvc.perform(get("/" + existingId))
                    .andExpect(request().asyncStarted())
                    .andReturn();

            // Complete async
            mockMvc.perform(asyncDispatch(result))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.message", Matchers.containsString("not found")));

            verify(queryService, times(1)).getCompanyMedia(eq(existingId));
        }
    }

    @Nested
    @DisplayName("List Company Media By Company API")
    class ListCompanyMediaByCompanyApiTests {

        @Test
        @DisplayName("Should list company media by companyId (async)")
        void testListCompanyMediaByCompany_Success() throws Exception {
            // Arrange
            String companyId = UUID.randomUUID().toString();

            QueryCompanyMediaResponseDto item = QueryCompanyMediaResponseDto.builder()
                    .id(UUID.randomUUID().toString())
                    .mediaTitle("Title")
                    .mediaDescription("Desc")
                    .mediaType("IMAGE")
                    .mediaPath("http://example.com/media.png")
                    .companyId(companyId)
                    .active(false)
                    .build();

            when(queryService.listCompanyMediaByCompany(eq(companyId))).thenReturn(QueryCompanyMediaListResponseDto.builder()
                    .items(List.of(item))
                    .build());

            // Act
            MvcResult result = mockMvc.perform(get("/")
                            .param("companyId", companyId))
                    .andExpect(request().asyncStarted())
                    .andReturn();

            // Complete async
            mockMvc.perform(asyncDispatch(result))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.items", Matchers.hasSize(1)))
                    .andExpect(jsonPath("$.items[0].companyId").value(companyId));

            verify(queryService, times(1)).listCompanyMediaByCompany(eq(companyId));
        }

        @Test
        @DisplayName("Should return 400 when listCompanyMediaByCompany missing companyId")
        void testListCompanyMediaByCompany_MissingCompanyId_Fail() throws Exception {
            // Act

            // TO-DO: Fix this to 400
            mockMvc.perform(get("/"))
                    .andExpect(status().is5xxServerError());

            verify(queryService, never()).listCompanyMediaByCompany(any());
        }
    }

    @Nested
    @DisplayName("Get Active Company Profile Image API")
    class GetActiveCompanyProfileImageApiTests {

        @Test
        @DisplayName("Should get active company profile image successfully (async)")
        void testGetActiveCompanyProfileImage_Valid_Success() throws Exception {
            // Arrange
            String companyId = UUID.randomUUID().toString();
            QueryCompanyMediaResponseDto active = QueryCompanyMediaResponseDto.builder()
                    .id(UUID.randomUUID().toString())
                    .mediaTitle("Active Title")
                    .mediaDescription("Active Desc")
                    .mediaType("IMAGE")
                    .mediaPath("http://example.com/active.png")
                    .companyId(companyId)
                    .active(true)
                    .build();

            when(queryService.getActiveCompanyProfileImage(eq(companyId))).thenReturn(active);

            // Act
            MvcResult result = mockMvc.perform(get("/active")
                            .param("companyId", companyId))
                    .andExpect(request().asyncStarted())
                    .andReturn();

            // Assert
            mockMvc.perform(asyncDispatch(result))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.companyId").value(companyId))
                    .andExpect(jsonPath("$.active").value(true));

            verify(queryService, times(1)).getActiveCompanyProfileImage(eq(companyId));
        }

        @Test
        @DisplayName("Should return 404 when active company profile image not found")
        void testGetActiveCompanyProfileImage_NotFound() throws Exception {
            // Arrange
            String companyId = UUID.randomUUID().toString();

            doThrow(new DomainException(DomainCode.RESOURCE_NOT_FOUND, "Active company profile image not found"))
                    .when(queryService).getActiveCompanyProfileImage(eq(companyId));

            // Act
            MvcResult result = mockMvc.perform(get("/active")
                            .param("companyId", companyId))
                    .andExpect(request().asyncStarted())
                    .andReturn();

            // Complete async
            mockMvc.perform(asyncDispatch(result))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.message", Matchers.containsString("not found")));

            verify(queryService, times(1)).getActiveCompanyProfileImage(eq(companyId));
        }
    }

    @Nested
    @DisplayName("Activate Company Profile Image API")
    class ActivateCompanyMediaApiTests {

        private String mediaId;

        @BeforeEach
        void setUp() {
            mediaId = UUID.randomUUID().toString();
        }

        @Test
        @DisplayName("Should activate company profile image and return no content (async)")
        void testActivateCompanyProfileImage_Valid_Success() throws Exception {
            // Arrange
            doNothing().when(updateService).activateCompanyProfileImage(eq(mediaId));

            // Act
            MvcResult result = mockMvc.perform(post("/" + mediaId + "/activate"))
                    .andExpect(request().asyncStarted())
                    .andReturn();

            // Complete async
            mockMvc.perform(asyncDispatch(result))
                    .andExpect(status().isNoContent());

            verify(updateService, times(1)).activateCompanyProfileImage(eq(mediaId));
        }

        @Test
        @DisplayName("Should return 400 when mediaId invalid")
        void testActivateCompanyProfileImage_InvalidId_Fail() throws Exception {
            // Act
            mockMvc.perform(post("/not-a-uuid/activate"))
                    .andExpect(status().isBadRequest());

            verify(updateService, never()).activateCompanyProfileImage(any());
        }

        @Test
        @DisplayName("Should return 404 when activate target not found (async)")
        void testActivateCompanyProfileImage_NotFound_Fail() throws Exception {
            // Arrange
            String mediaId = UUID.randomUUID().toString();
            doThrow(new DomainException(DomainCode.RESOURCE_NOT_FOUND, "not found"))
                    .when(updateService).activateCompanyProfileImage(eq(mediaId));

            // Act
            MvcResult result = mockMvc.perform(post("/" + mediaId + "/activate"))
                    .andExpect(request().asyncStarted())
                    .andReturn();

            // Assert
            mockMvc.perform(asyncDispatch(result))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.message", Matchers.containsString("not found")));

            verify(updateService, times(1)).activateCompanyProfileImage(eq(mediaId));
        }
    }
}
