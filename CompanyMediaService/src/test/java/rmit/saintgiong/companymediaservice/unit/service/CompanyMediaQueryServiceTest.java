package rmit.saintgiong.companymediaservice.unit.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import rmit.saintgiong.mediaservice.common.storage.GcsStorageProperties;
import rmit.saintgiong.mediaservice.common.storage.ObjectStorageService;
import rmit.saintgiong.mediaservice.domain.mappers.CompanyMediaMapper;
import rmit.saintgiong.mediaservice.domain.repositories.entities.CompanyMediaEntity;
import rmit.saintgiong.mediaservice.domain.services.internal.CompanyMediaQueryService;
import rmit.saintgiong.mediaservice.domain.validators.CompanyMediaBaseValidator;
import rmit.saintgiong.mediaapi.internal.common.dto.response.QueryCompanyMediaResponseDto;

import java.net.URL;
import java.time.Duration;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CompanyMediaQueryServiceTest {

    @Mock
    private CompanyMediaMapper mapper;

    @Mock
    private CompanyMediaBaseValidator<Void> baseValidator;

    @Mock
    private ObjectStorageService objectStorageService;

    @Mock
    private GcsStorageProperties gcsProps;

    @InjectMocks
    private CompanyMediaQueryService queryService;

    private UUID existingId;
    private CompanyMediaEntity existingEntity;

    @BeforeEach
    void setUp() {
        existingId = UUID.randomUUID();
        UUID companyId = UUID.randomUUID();
        existingEntity = CompanyMediaEntity.builder()
                .id(existingId)
                .mediaTitle("Name")
                .mediaDescription("Desc")
                .mediaType("IMAGE")
                .mediaUrl("company-media/company/" + companyId + "/obj.png")
                .companyId(companyId)
                .build();
    }

    @Test
    void givenExistingId_whenGet_thenReturnDtoWithSignedUrl() throws Exception {
        when(gcsProps.getSignedUrlTtlMinutes()).thenReturn(30L);
        QueryCompanyMediaResponseDto dto = QueryCompanyMediaResponseDto.builder()
                .id(existingId.toString())
                .mediaTitle(existingEntity.getMediaTitle())
                .mediaDescription(existingEntity.getMediaDescription())
                .mediaType(existingEntity.getMediaType())
                // mapper maps object reference into mediaPath initially
                .mediaPath(existingEntity.getMediaUrl())
                .companyId(existingEntity.getCompanyId().toString())
                .build();

        URL signed = new URL("https://storage.googleapis.com/signed-url");

        when(baseValidator.assertExistsById(existingId)).thenReturn(existingEntity);
        when(mapper.toQueryResponse(existingEntity)).thenReturn(dto);
        when(objectStorageService.signUrl(eq(existingEntity.getMediaUrl()), any(Duration.class))).thenReturn(signed);

        QueryCompanyMediaResponseDto result = queryService.getCompanyMedia(existingId.toString());

        assertNotNull(result);
        assertEquals(existingId.toString(), result.getId());
        assertEquals("https://storage.googleapis.com/signed-url", result.getMediaPath());

        verify(baseValidator, times(1)).assertExistsById(existingId);
        verify(mapper, times(1)).toQueryResponse(existingEntity);
        verify(objectStorageService, times(1)).signUrl(eq(existingEntity.getMediaUrl()), any(Duration.class));
    }

    @Test
    void givenInvalidUuidString_whenGet_thenThrowIllegalArgumentException() {
        String bad = "not-a-uuid";
        assertThrows(IllegalArgumentException.class, () -> queryService.getCompanyMedia(bad));
        verifyNoInteractions(baseValidator);
        verifyNoInteractions(mapper);
        verifyNoInteractions(objectStorageService);
    }
}
