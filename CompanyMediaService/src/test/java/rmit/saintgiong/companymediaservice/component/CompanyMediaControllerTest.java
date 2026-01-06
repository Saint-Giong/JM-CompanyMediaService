//package rmit.saintgiong.companymediaservice.component;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.hamcrest.Matchers;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Nested;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.http.MediaType;
//import org.springframework.test.context.bean.override.mockito.MockitoBean;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.MvcResult;
//import rmit.saintgiong.mediaservice.JmCompanyMediaApplication;
//import rmit.saintgiong.mediaservice.common.exception.domain.DomainException;
//import rmit.saintgiong.mediaapi.internal.common.dto.request.CreateCompanyMediaRequestDto;
//import rmit.saintgiong.mediaapi.internal.common.dto.response.CreateCompanyMediaResponseDto;
//import rmit.saintgiong.mediaapi.internal.common.dto.response.QueryCompanyMediaListResponseDto;
//import rmit.saintgiong.mediaapi.internal.common.dto.response.QueryCompanyMediaResponseDto;
//import rmit.saintgiong.mediaapi.internal.common.type.DomainCode;
//import rmit.saintgiong.mediaapi.internal.services.CreateCompanyMediaInterface;
//import rmit.saintgiong.mediaapi.internal.services.DeleteCompanyMediaInterface;
//import rmit.saintgiong.mediaapi.internal.services.QueryCompanyMediaInterface;
//
//import java.util.List;
//import java.util.UUID;
//
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.doNothing;
//import static org.mockito.Mockito.doThrow;
//import static org.mockito.Mockito.eq;
//import static org.mockito.Mockito.never;
//import static org.mockito.Mockito.times;
//import static org.mockito.Mockito.verify;
//import static org.mockito.Mockito.when;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.asyncDispatch;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.request;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//@SpringBootTest(classes = JmCompanyMediaApplication.class)
//@AutoConfigureMockMvc
//@DisplayName("Company Media Controller Tests")
//class CompanyMediaControllerTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    private final ObjectMapper objectMapper = new ObjectMapper();
//
//    @MockitoBean
//    private CreateCompanyMediaInterface createService;
//
//    @MockitoBean
//    private QueryCompanyMediaInterface queryService;
//
//    @MockitoBean
//    private DeleteCompanyMediaInterface deleteService;
//
//    @Nested
//    @DisplayName("Create Company Media API")
//    class CreateCompanyMediaApiTests {
//
//        private CreateCompanyMediaRequestDto validMeta;
//        private String generatedId;
//
//        @BeforeEach
//        void setUpCreate() {
//            generatedId = UUID.randomUUID().toString();
//            validMeta = CreateCompanyMediaRequestDto.builder()
//                    .mediaTitle("Test Media")
//                    .mediaDescription("Desc")
//                    .mediaType(rmit.saintgiong.mediaapi.internal.common.type.MediaType.IMAGE)
//                    .companyId(UUID.randomUUID().toString())
//                    .build();
//        }
//
//        @Test
//        @DisplayName("Should create company media (json) and return id (async)")
//        void testCreateCompany_Valid_Success() throws Exception {
//            CreateCompanyMediaResponseDto mockResp = CreateCompanyMediaResponseDto.builder()
//                    .id(generatedId)
//                    .build();
//
//            when(createService.createCompanyMedia(any(CreateCompanyMediaRequestDto.class), any(), any(), any()))
//                    .thenReturn(mockResp);
//
//            MvcResult result = mockMvc.perform(post("/")
//                            .contentType(MediaType.APPLICATION_JSON)
//                            .content(objectMapper.writeValueAsBytes(validMeta)))
//                    .andExpect(request().asyncStarted())
//                    .andReturn();
//
//            mockMvc.perform(asyncDispatch(result))
//                    .andExpect(status().isCreated())
//                    .andExpect(jsonPath("$.id").value(generatedId));
//
//            verify(createService, times(1)).createCompanyMedia(any(CreateCompanyMediaRequestDto.class), any(), any(), any());
//        }
//
//        @Test
//        @DisplayName("Should fail with 400 when mediaTitle is blank")
//        void testCreateCompany_BlankTitle_Fail() throws Exception {
//            CreateCompanyMediaRequestDto req = CreateCompanyMediaRequestDto.builder()
//                    .mediaTitle("")
//                    .mediaDescription(validMeta.getMediaDescription())
//                    .mediaType(validMeta.getMediaType())
//                    .companyId(validMeta.getCompanyId())
//                    .build();
//
//            mockMvc.perform(post("/")
//                            .contentType(MediaType.APPLICATION_JSON)
//                            .content(objectMapper.writeValueAsBytes(req)))
//                    .andExpect(status().isBadRequest());
//
//            verify(createService, never()).createCompanyMedia(any(), any(), any(), any());
//        }
//    }
//
//    @Nested
//    @DisplayName("Get Company Media API")
//    class GetCompanyMediaApiTests {
//
//        private String existingId;
//        private QueryCompanyMediaResponseDto validResponse;
//
//        @BeforeEach
//        void setUpGet() {
//            existingId = UUID.randomUUID().toString();
//            validResponse = QueryCompanyMediaResponseDto.builder()
//                    .id(existingId)
//                    .mediaTitle("Title")
//                    .mediaDescription("Desc")
//                    .mediaType("IMAGE")
//                    .mediaPath("http://example.com/media.png")
//                    .companyId(UUID.randomUUID().toString())
//                    .build();
//        }
//
//        @Test
//        @DisplayName("Should get company media successfully (async)")
//        void testGetCompanyMedia_Valid_Success() throws Exception {
//            // Arrange
//            when(queryService.getCompanyMedia(eq(existingId))).thenReturn(validResponse);
//
//            // Act
//            MvcResult result = mockMvc.perform(get("/" + existingId))
//                    .andExpect(request().asyncStarted())
//                    .andReturn();
//
//            // Complete async
//            mockMvc.perform(asyncDispatch(result))
//                    .andExpect(status().isOk())
//                    .andExpect(jsonPath("$.id").value(existingId))
//                    .andExpect(jsonPath("$.mediaTitle").value("Title"));
//
//            verify(queryService, times(1)).getCompanyMedia(eq(existingId));
//        }
//
//        @Test
//        @DisplayName("Should return 400 when id format invalid")
//        void testGetCompanyMedia_InvalidId_Fail() throws Exception {
//            // Act
//            mockMvc.perform(get("/not-a-uuid"))
//                    .andExpect(status().isBadRequest());
//
//            verify(queryService, never()).getCompanyMedia(any());
//        }
//
//        @Test
//        @DisplayName("Should return 404 when company media not found")
//        void testGetCompanyMedia_NotFound_Fail() throws Exception {
//            // Arrange
//            doThrow(new DomainException(DomainCode.RESOURCE_NOT_FOUND, "not found"))
//                    .when(queryService).getCompanyMedia(eq(existingId));
//
//            // Act
//            MvcResult result = mockMvc.perform(get("/" + existingId))
//                    .andExpect(request().asyncStarted())
//                    .andReturn();
//
//            // Complete async
//            mockMvc.perform(asyncDispatch(result))
//                    .andExpect(status().isNotFound())
//                    .andExpect(jsonPath("$.message", Matchers.containsString("not found")));
//
//            verify(queryService, times(1)).getCompanyMedia(eq(existingId));
//        }
//    }
//
//    @Nested
//    @DisplayName("List Company Media By Company API")
//    class ListCompanyMediaByCompanyApiTests {
//
//        @Test
//        @DisplayName("Should list company media by companyId (async)")
//        void testListCompanyMediaByCompany_Success() throws Exception {
//            // Arrange
//            String companyId = UUID.randomUUID().toString();
//
//            QueryCompanyMediaResponseDto item = QueryCompanyMediaResponseDto.builder()
//                    .id(UUID.randomUUID().toString())
//                    .mediaTitle("Title")
//                    .mediaDescription("Desc")
//                    .mediaType("IMAGE")
//                    .mediaPath("http://example.com/media.png")
//                    .companyId(companyId)
//                    .build();
//
//            when(queryService.listCompanyMediaByCompany(eq(companyId))).thenReturn(QueryCompanyMediaListResponseDto.builder()
//                    .items(List.of(item))
//                    .build());
//
//            // Act
//            MvcResult result = mockMvc.perform(get("/")
//                            .param("companyId", companyId))
//                    .andExpect(request().asyncStarted())
//                    .andReturn();
//
//            // Complete async
//            mockMvc.perform(asyncDispatch(result))
//                    .andExpect(status().isOk())
//                    .andExpect(jsonPath("$.items", Matchers.hasSize(1)))
//                    .andExpect(jsonPath("$.items[0].companyId").value(companyId));
//
//            verify(queryService, times(1)).listCompanyMediaByCompany(eq(companyId));
//        }
//
//        @Test
//        @DisplayName("Should return 5xx when listCompanyMediaByCompany missing companyId")
//        void testListCompanyMediaByCompany_MissingCompanyId_Fail() throws Exception {
//            mockMvc.perform(get("/"))
//                    .andExpect(status().is5xxServerError());
//
//            verify(queryService, never()).listCompanyMediaByCompany(any());
//        }
//    }
//
//    @Nested
//    @DisplayName("Delete Company Media API")
//    class DeleteCompanyMediaApiTests {
//
//        @Test
//        @DisplayName("Should delete company media and return no content (async)")
//        void testDeleteCompanyMedia_Valid_Success() throws Exception {
//            String id = UUID.randomUUID().toString();
//            doNothing().when(deleteService).deleteCompanyMedia(eq(id));
//
//            MvcResult result = mockMvc.perform(delete("/" + id))
//                    .andExpect(request().asyncStarted())
//                    .andReturn();
//
//            mockMvc.perform(asyncDispatch(result))
//                    .andExpect(status().isNoContent());
//
//            verify(deleteService, times(1)).deleteCompanyMedia(eq(id));
//        }
//
//        @Test
//        @DisplayName("Should return 400 when delete id invalid")
//        void testDeleteCompanyMedia_InvalidId_BadRequest() throws Exception {
//            mockMvc.perform(delete("/not-a-uuid"))
//                    .andExpect(status().isBadRequest());
//
//            verify(deleteService, never()).deleteCompanyMedia(any());
//        }
//
//        @Test
//        @DisplayName("Should return 404 when delete target not found (async)")
//        void testDeleteCompanyMedia_NotFound() throws Exception {
//            String id = UUID.randomUUID().toString();
//            doThrow(new DomainException(DomainCode.RESOURCE_NOT_FOUND, "not found"))
//                    .when(deleteService).deleteCompanyMedia(eq(id));
//
//            MvcResult result = mockMvc.perform(delete("/" + id))
//                    .andExpect(request().asyncStarted())
//                    .andReturn();
//
//            mockMvc.perform(asyncDispatch(result))
//                    .andExpect(status().isNotFound())
//                    .andExpect(jsonPath("$.message", Matchers.containsString("not found")));
//
//            verify(deleteService, times(1)).deleteCompanyMedia(eq(id));
//        }
//    }
//}
